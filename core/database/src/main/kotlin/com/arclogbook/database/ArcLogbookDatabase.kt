package com.arclogbook.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.arclogbook.database.dao.*
import com.arclogbook.database.entity.*
import com.arclogbook.database.util.Converters

@Database(
    entities = [
        IncidentEntity::class,
        ThreatIndicatorEntity::class,
        VulnerabilityEntity::class,
        MetadataExtractionEntity::class,
        CityEntity::class,
        ConnectorCredentialEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ArcLogbookDatabase : RoomDatabase() {
    abstract fun incidentDao(): IncidentDao
    abstract fun threatDao(): ThreatDao
    abstract fun vulnDao(): VulnDao
    abstract fun metadataDao(): MetadataDao
    abstract fun cityDao(): CityDao
    abstract fun connectorDao(): ConnectorDao
    
    companion object {
        const val DATABASE_NAME = "arclogbook.db"
    }
}