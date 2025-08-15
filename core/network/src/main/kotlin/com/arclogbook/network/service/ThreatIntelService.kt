package com.arclogbook.network.service

import com.arclogbook.network.model.CisaKevResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ThreatIntelService {
    
    @GET("sites/default/files/feeds/known_exploited_vulnerabilities.json")
    @Headers(
        "User-Agent: ArcLogbook/1.0 (Security Research Tool)",
        "Accept: application/json"
    )
    suspend fun getCisaKnownExploitedVulns(): Response<CisaKevResponse>
}