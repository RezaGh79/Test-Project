package com.reza.sampleproject.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getLastKnownLocation(): Location?
}

