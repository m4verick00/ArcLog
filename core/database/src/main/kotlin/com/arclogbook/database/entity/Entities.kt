package com.arclogbook.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.time.Instant

@Entity(tableName = "incidents")
data class IncidentEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val severity: String, // LOW, MEDIUM, HIGH, CRITICAL
    val status: String, // OPEN, IN_PROGRESS, CLOSED
    val createdAt: Instant,
    val updatedAt: Instant,
    val tags: List<String>,
    val assignedTo: String?,
    val iocs: List<String> // Indicators of Compromise
)

@Entity(tableName = "threat_indicators")
data class ThreatIndicatorEntity(
    @PrimaryKey
    val id: String,
    val type: String, // IP, DOMAIN, URL, HASH, EMAIL
    val value: String,
    val source: String, // CISA, CERT, etc.
    val severity: String,
    val description: String,
    val firstSeen: Instant,
    val lastSeen: Instant,
    val tags: List<String>,
    val confidence: Float // 0.0 to 1.0
)

@Entity(tableName = "vulnerabilities")
data class VulnerabilityEntity(
    @PrimaryKey
    val id: String, // CVE-2023-1234
    val title: String,
    val description: String,
    val cvssScore: Float,
    val cvssVector: String,
    val publishedDate: Instant,
    val lastModifiedDate: Instant,
    val affectedProducts: List<String>,
    val references: List<String>,
    val patchStatus: String, // NOT_ASSESSED, NEEDS_PATCH, PATCHED, NOT_APPLICABLE
    val priority: String, // LOW, MEDIUM, HIGH, CRITICAL
    val assignedTo: String?
)

@Entity(
    tableName = "metadata_extractions",
    indices = [
        Index(value = ["filePath"]),
        Index(value = ["hasGpsData"]),
        Index(value = ["extractedAt"])
    ]
)
data class MetadataExtractionEntity(
    @PrimaryKey
    val id: String,
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val mimeType: String,
    val extractedAt: Instant,
    val hasGpsData: Boolean,
    val latitude: Double?,
    val longitude: Double?,
    val altitude: Double?,
    val gpsTimestamp: Instant?,
    val nearestCity: String?, // From offline geocoding
    val onlineGeoResult: String?, // From online reverse geocoding
    val cameraModel: String?,
    val cameraMake: String?,
    val softwareUsed: String?,
    val dateTimeOriginal: Instant?,
    val sha256Hash: String,
    val md5Hash: String,
    val fileCreatedAt: Instant?,
    val fileModifiedAt: Instant?,
    val exifData: Map<String, String>, // Raw EXIF tags
    val piiRiskLevel: String, // LOW, MEDIUM, HIGH
    val similarity: Map<String, String> // Hash types and values
)

@Entity(
    tableName = "cities",
    indices = [
        Index(value = ["latitude", "longitude"]),
        Index(value = ["countryCode"])
    ]
)
data class CityEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val asciiName: String,
    val latitude: Double,
    val longitude: Double,
    val countryCode: String,
    val admin1Code: String?, // State/Province
    val population: Int
)

@Entity(tableName = "connector_credentials")
data class ConnectorCredentialEntity(
    @PrimaryKey
    val id: String,
    val connectorId: String, // shodan, censys, hibp
    val name: String, // User-friendly name
    val baseUrl: String,
    val username: String?,
    val encryptedPassword: String?, // Encrypted with Android Keystore
    val apiKey: String?, // Encrypted with Android Keystore
    val oauthToken: String?, // Encrypted with Android Keystore
    val tokenExpiry: Instant?,
    val isActive: Boolean,
    val lastUsed: Instant?,
    val createdAt: Instant,
    val scopes: List<String>
)