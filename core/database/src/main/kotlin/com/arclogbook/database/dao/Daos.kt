package com.arclogbook.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.arclogbook.database.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface IncidentDao {
    @Query("SELECT * FROM incidents ORDER BY createdAt DESC")
    fun getAllIncidents(): PagingSource<Int, IncidentEntity>
    
    @Query("SELECT * FROM incidents WHERE id = :id")
    suspend fun getIncidentById(id: String): IncidentEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentEntity)
    
    @Update
    suspend fun updateIncident(incident: IncidentEntity)
    
    @Delete
    suspend fun deleteIncident(incident: IncidentEntity)
    
    @Query("SELECT COUNT(*) FROM incidents WHERE status != 'CLOSED'")
    fun getOpenIncidentCount(): Flow<Int>
}

@Dao
interface ThreatDao {
    @Query("SELECT * FROM threat_indicators ORDER BY lastSeen DESC")
    fun getAllIndicators(): PagingSource<Int, ThreatIndicatorEntity>
    
    @Query("SELECT * FROM threat_indicators WHERE severity = :severity ORDER BY lastSeen DESC")
    fun getIndicatorsBySeverity(severity: String): PagingSource<Int, ThreatIndicatorEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndicators(indicators: List<ThreatIndicatorEntity>)
    
    @Query("SELECT COUNT(*) FROM threat_indicators WHERE severity IN ('HIGH', 'CRITICAL')")
    fun getHighSeverityCount(): Flow<Int>
    
    @Query("DELETE FROM threat_indicators WHERE lastSeen < :cutoff")
    suspend fun cleanupOldIndicators(cutoff: Instant)
}

@Dao
interface VulnDao {
    @Query("SELECT * FROM vulnerabilities ORDER BY publishedDate DESC")
    fun getAllVulnerabilities(): PagingSource<Int, VulnerabilityEntity>
    
    @Query("SELECT * FROM vulnerabilities WHERE patchStatus = :status ORDER BY cvssScore DESC")
    fun getVulnerabilitiesByPatchStatus(status: String): PagingSource<Int, VulnerabilityEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVulnerabilities(vulns: List<VulnerabilityEntity>)
    
    @Update
    suspend fun updateVulnerability(vuln: VulnerabilityEntity)
    
    @Query("SELECT COUNT(*) FROM vulnerabilities WHERE patchStatus = 'NEEDS_PATCH' AND priority IN ('HIGH', 'CRITICAL')")
    fun getCriticalPatchCount(): Flow<Int>
}

@Dao
interface MetadataDao {
    @Query("SELECT * FROM metadata_extractions ORDER BY extractedAt DESC")
    fun getAllExtractions(): PagingSource<Int, MetadataExtractionEntity>
    
    @Query("SELECT * FROM metadata_extractions WHERE hasGpsData = 1 ORDER BY extractedAt DESC")
    fun getExtractionsWithGps(): PagingSource<Int, MetadataExtractionEntity>
    
    @Query("SELECT * FROM metadata_extractions WHERE id = :id")
    suspend fun getExtractionById(id: String): MetadataExtractionEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExtraction(extraction: MetadataExtractionEntity)
    
    @Query("SELECT * FROM metadata_extractions WHERE piiRiskLevel = 'HIGH' ORDER BY extractedAt DESC")
    fun getHighRiskExtractions(): PagingSource<Int, MetadataExtractionEntity>
    
    @Query("SELECT COUNT(*) FROM metadata_extractions WHERE hasGpsData = 1")
    fun getGpsExtractionCount(): Flow<Int>
}

@Dao
interface CityDao {
    @Query("""
        SELECT * FROM cities 
        ORDER BY (
            (latitude - :lat) * (latitude - :lat) + 
            (longitude - :lng) * (longitude - :lng)
        ) ASC 
        LIMIT 1
    """)
    suspend fun findNearestCity(lat: Double, lng: Double): CityEntity?
    
    @Query("SELECT * FROM cities WHERE name LIKE :query LIMIT 10")
    suspend fun searchCities(query: String): List<CityEntity>
}

@Dao
interface ConnectorDao {
    @Query("SELECT * FROM connector_credentials WHERE isActive = 1")
    suspend fun getActiveCredentials(): List<ConnectorCredentialEntity>
    
    @Query("SELECT * FROM connector_credentials WHERE connectorId = :connectorId")
    suspend fun getCredentialsForConnector(connectorId: String): List<ConnectorCredentialEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCredential(credential: ConnectorCredentialEntity)
    
    @Update
    suspend fun updateCredential(credential: ConnectorCredentialEntity)
    
    @Delete
    suspend fun deleteCredential(credential: ConnectorCredentialEntity)
}