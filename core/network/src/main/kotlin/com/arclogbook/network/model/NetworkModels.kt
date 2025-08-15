package com.arclogbook.network.model

import com.google.gson.annotations.SerializedName

data class CisaKevResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("catalogVersion")
    val catalogVersion: String,
    @SerializedName("dateReleased")
    val dateReleased: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("vulnerabilities")
    val vulnerabilities: List<CisaVulnerability>
)

data class CisaVulnerability(
    @SerializedName("cveID")
    val cveId: String,
    @SerializedName("vendorProject")
    val vendorProject: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("vulnerabilityName")
    val vulnerabilityName: String,
    @SerializedName("dateAdded")
    val dateAdded: String,
    @SerializedName("shortDescription")
    val shortDescription: String,
    @SerializedName("requiredAction")
    val requiredAction: String,
    @SerializedName("dueDate")
    val dueDate: String,
    @SerializedName("notes")
    val notes: String
)

data class NominatimResponse(
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("address")
    val address: NominatimAddress?,
    @SerializedName("lat")
    val latitude: String,
    @SerializedName("lon")
    val longitude: String
)

data class NominatimAddress(
    @SerializedName("house_number")
    val houseNumber: String?,
    @SerializedName("road")
    val road: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("town")
    val town: String?,
    @SerializedName("village")
    val village: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("postcode")
    val postcode: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_code")
    val countryCode: String?
)