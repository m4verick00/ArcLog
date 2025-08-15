package com.arclogbook.feature.metadata.domain

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.arclogbook.database.dao.CityDao
import com.arclogbook.database.dao.MetadataDao
import com.arclogbook.database.entity.MetadataExtractionEntity
import com.arclogbook.network.service.GeocodingService
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.GpsDirectory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileInputStream
import java.time.Instant
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetadataExtractor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val metadataDao: MetadataDao,
    private val cityDao: CityDao,
    private val geocodingService: GeocodingService
) {

    suspend fun extractMetadata(
        uri: Uri,
        enableOnlineGeocoding: Boolean = false
    ): Result<MetadataExtractionEntity> = withContext(Dispatchers.IO) {
        try {
            val file = getFileFromUri(uri) ?: return@withContext Result.failure(
                IllegalArgumentException("Cannot access file from URI")
            )
            
            val extraction = buildMetadataExtraction(file, enableOnlineGeocoding)
            metadataDao.insertExtraction(extraction)
            
            Result.success(extraction)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun buildMetadataExtraction(
        file: File,
        enableOnlineGeocoding: Boolean
    ): MetadataExtractionEntity {
        val extractionId = UUID.randomUUID().toString()
        val sha256Hash = calculateSha256(file)
        val md5Hash = calculateMd5(file)
        
        // Extract basic EXIF data
        val exifInterface = ExifInterface(file.absolutePath)
        val exifData = extractExifData(exifInterface)
        
        // Extract GPS coordinates
        val gpsData = extractGpsData(exifInterface)
        
        // Find nearest city (offline)
        val nearestCity = gpsData?.let { (lat, lng) ->
            cityDao.findNearestCity(lat, lng)?.let { city ->
                "${city.name}, ${city.countryCode}"
            }
        }
        
        // Optional online reverse geocoding
        val onlineGeoResult = if (enableOnlineGeocoding && gpsData != null) {
            try {
                val response = geocodingService.reverseGeocode(gpsData.first, gpsData.second)
                if (response.isSuccessful) {
                    response.body()?.displayName
                } else null
            } catch (e: Exception) {
                null
            }
        } else null
        
        // Extract camera info
        val cameraModel = exifInterface.getAttribute(ExifInterface.TAG_MODEL)
        val cameraMake = exifInterface.getAttribute(ExifInterface.TAG_MAKE)
        val softwareUsed = exifInterface.getAttribute(ExifInterface.TAG_SOFTWARE)
        
        // Extract timestamps
        val dateTimeOriginal = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
            ?.let { parseExifDateTime(it) }
        
        val gpsTimestamp = gpsData?.let { 
            exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP)?.let { 
                parseExifDateTime(it) 
            }
        }
        
        // Calculate PII risk level
        val piiRiskLevel = calculatePiiRisk(gpsData, cameraModel, cameraMake, softwareUsed)
        
        // Generate similarity hashes (simplified - in production use actual perceptual hashing)
        val similarityHashes = generateSimilarityHashes(file)
        
        return MetadataExtractionEntity(
            id = extractionId,
            filePath = file.absolutePath,
            fileName = file.name,
            fileSize = file.length(),
            mimeType = getMimeType(file),
            extractedAt = Instant.now(),
            hasGpsData = gpsData != null,
            latitude = gpsData?.first,
            longitude = gpsData?.second,
            altitude = exifInterface.getAltitude(0.0).takeIf { it != 0.0 },
            gpsTimestamp = gpsTimestamp,
            nearestCity = nearestCity,
            onlineGeoResult = onlineGeoResult,
            cameraModel = cameraModel,
            cameraMake = cameraMake,
            softwareUsed = softwareUsed,
            dateTimeOriginal = dateTimeOriginal,
            sha256Hash = sha256Hash,
            md5Hash = md5Hash,
            fileCreatedAt = Instant.ofEpochMilli(file.lastModified()),
            fileModifiedAt = Instant.ofEpochMilli(file.lastModified()),
            exifData = exifData,
            piiRiskLevel = piiRiskLevel,
            similarity = similarityHashes
        )
    }

    private fun getFileFromUri(uri: Uri): File? {
        return when (uri.scheme) {
            "file" -> File(uri.path!!)
            "content" -> {
                // In production, copy content URI to temporary file
                // For now, return null for content URIs
                null
            }
            else -> null
        }
    }

    private fun calculateSha256(file: File): String {
        return FileInputStream(file).use { DigestUtils.sha256Hex(it) }
    }

    private fun calculateMd5(file: File): String {
        return FileInputStream(file).use { DigestUtils.md5Hex(it) }
    }

    private fun extractExifData(exifInterface: ExifInterface): Map<String, String> {
        val exifData = mutableMapOf<String, String>()
        
        val tags = listOf(
            ExifInterface.TAG_MAKE,
            ExifInterface.TAG_MODEL,
            ExifInterface.TAG_SOFTWARE,
            ExifInterface.TAG_DATETIME_ORIGINAL,
            ExifInterface.TAG_GPS_LATITUDE,
            ExifInterface.TAG_GPS_LONGITUDE,
            ExifInterface.TAG_GPS_ALTITUDE,
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.TAG_ISO_SPEED_RATINGS,
            ExifInterface.TAG_FOCAL_LENGTH,
            ExifInterface.TAG_F_NUMBER,
            ExifInterface.TAG_EXPOSURE_TIME,
            ExifInterface.TAG_WHITE_BALANCE
        )
        
        tags.forEach { tag ->
            exifInterface.getAttribute(tag)?.let { value ->
                exifData[tag] = value
            }
        }
        
        return exifData
    }

    private fun extractGpsData(exifInterface: ExifInterface): Pair<Double, Double>? {
        val latLong = FloatArray(2)
        return if (exifInterface.getLatLong(latLong)) {
            Pair(latLong[0].toDouble(), latLong[1].toDouble())
        } else null
    }

    private fun parseExifDateTime(dateTime: String): Instant? {
        return try {
            // EXIF datetime format: "yyyy:MM:dd HH:mm:ss"
            val parts = dateTime.split(" ")
            if (parts.size == 2) {
                val date = parts[0].replace(":", "-")
                val time = parts[1]
                Instant.parse("${date}T${time}Z")
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun calculatePiiRisk(
        gpsData: Pair<Double, Double>?,
        cameraModel: String?,
        cameraMake: String?,
        softwareUsed: String?
    ): String {
        var riskScore = 0
        
        // GPS data significantly increases PII risk
        if (gpsData != null) riskScore += 3
        
        // Camera serial numbers or device-specific info
        if (cameraModel != null || cameraMake != null) riskScore += 1
        
        // Software that might include user info
        if (softwareUsed != null) riskScore += 1
        
        return when {
            riskScore >= 4 -> "HIGH"
            riskScore >= 2 -> "MEDIUM"
            else -> "LOW"
        }
    }

    private fun generateSimilarityHashes(file: File): Map<String, String> {
        // In production, implement actual perceptual hashing
        // For now, return simplified hashes
        return mapOf(
            "aHash" -> file.hashCode().toString(),
            "pHash" -> file.name.hashCode().toString(),
            "dHash" -> file.length().toString()
        )
    }

    private fun getMimeType(file: File): String {
        val extension = file.extension.lowercase()
        return when (extension) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "pdf" -> "application/pdf"
            "mp4" -> "video/mp4"
            "mov" -> "video/quicktime"
            else -> "application/octet-stream"
        }
    }
}