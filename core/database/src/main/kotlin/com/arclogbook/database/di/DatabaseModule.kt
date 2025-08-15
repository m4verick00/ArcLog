package com.arclogbook.database.di

import android.content.Context
import androidx.room.Room
import com.arclogbook.database.ArcLogbookDatabase
import com.arclogbook.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabasePassphrase(): ByteArray {
        // In production, this should come from Android Keystore
        return "arclogbook_secure_key_2023".toByteArray()
    }
    
    @Provides
    @Singleton
    fun provideArcLogbookDatabase(
        @ApplicationContext context: Context,
        passphrase: ByteArray
    ): ArcLogbookDatabase {
        val factory = SupportFactory(SQLiteDatabase.getBytes(passphrase))
        
        return Room.databaseBuilder(
            context,
            ArcLogbookDatabase::class.java,
            ArcLogbookDatabase.DATABASE_NAME
        )
        .openHelperFactory(factory)
        .createFromAsset("cities.db") // Offline city database
        .build()
    }
    
    @Provides
    fun provideIncidentDao(database: ArcLogbookDatabase): IncidentDao = database.incidentDao()
    
    @Provides
    fun provideThreatDao(database: ArcLogbookDatabase): ThreatDao = database.threatDao()
    
    @Provides
    fun provideVulnDao(database: ArcLogbookDatabase): VulnDao = database.vulnDao()
    
    @Provides
    fun provideMetadataDao(database: ArcLogbookDatabase): MetadataDao = database.metadataDao()
    
    @Provides
    fun provideCityDao(database: ArcLogbookDatabase): CityDao = database.cityDao()
    
    @Provides
    fun provideConnectorDao(database: ArcLogbookDatabase): ConnectorDao = database.connectorDao()
}