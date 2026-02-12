package com.reza.sampleproject.domain.usecase

import android.location.Location
import com.reza.sampleproject.domain.repository.LocationRepository

class GetUserLastLocationUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(): Location? {
        return locationRepository.getLastKnownLocation()
    }
}

