package com.campusmov.uniride.data.repository.location

import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.domain.location.model.PlacePrediction
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl(private val locationDataSource: LocationDataSource, private val placesClient: PlacesClient): LocationRepository {
    override fun getLocationUpdates(callback: (LatLng) -> Unit) {
        locationDataSource.getLocationUpdates(callback)
    }

    override suspend fun getPlacePredictions(query: String): List<PlacePrediction> {
        var request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
        return suspendCancellableCoroutine { continuation ->
            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val predictions = response.autocompletePredictions.map { prediction ->
                        PlacePrediction(
                            id = prediction.placeId,
                            fullText = prediction.getFullText(null).toString()
                        )
                    }
                    continuation.resume(predictions) {}
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }

    override suspend fun getPlaceDetails(placeId: String): Place {
        val request = FetchPlaceRequest.builder(
            placeId,
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        ).build()
        return suspendCancellableCoroutine { continuation ->
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    continuation.resume(response.place) {}
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
    }
}