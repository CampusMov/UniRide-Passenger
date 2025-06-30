package com.campusmov.uniride.data.datasource.remote.dto

import com.google.gson.annotations.SerializedName

data class RouteRequestDto(
    @SerializedName("startLatitude")
    val startLatitude: Double,
    @SerializedName("startLongitude")
    val startLongitude: Double,
    @SerializedName("endLatitude")
    val endLatitude: Double,
    @SerializedName("endLongitude")
    val endLongitude: Double
)