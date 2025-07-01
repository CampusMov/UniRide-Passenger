package com.campusmov.uniride.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class IntersectionDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)