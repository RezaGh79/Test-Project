package com.reza.sampleproject.data.repository

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.reza.sampleproject.domain.repository.LocationRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    override suspend fun getLastKnownLocation(): Location? {
        return try {
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (continuation.isActive) {
                            continuation.resume(location)
                        }
                    }
                    .addOnFailureListener {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
            }
        } catch (_: SecurityException) {
            null
        }
    }
}

