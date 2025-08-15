package com.arclogbook.feature.intel.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arclogbook.database.dao.ThreatDao
import com.arclogbook.database.entity.ThreatIndicatorEntity
import com.arclogbook.network.service.ThreatIntelService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Instant
import java.util.*

@HiltWorker
class ThreatIntelSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val threatIntelService: ThreatIntelService,
    private val threatDao: ThreatDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = threatIntelService.getCisaKnownExploitedVulns()
            
            if (response.isSuccessful) {
                val cisaData = response.body()
                cisaData?.let { data ->
                    val threats = data.vulnerabilities.map { vuln ->
                        ThreatIndicatorEntity(
                            id = UUID.randomUUID().toString(),
                            type = "CVE",
                            value = vuln.cveId,
                            source = "CISA KEV",
                            severity = "HIGH", // CISA KEV are actively exploited
                            description = "${vuln.vulnerabilityName}: ${vuln.shortDescription}",
                            firstSeen = Instant.now(),
                            lastSeen = Instant.now(),
                            tags = listOf("cisa", "kev", "actively-exploited"),
                            confidence = 0.95f
                        )
                    }
                    
                    threatDao.insertIndicators(threats)
                }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}