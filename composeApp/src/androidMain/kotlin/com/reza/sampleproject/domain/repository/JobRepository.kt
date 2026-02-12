package com.reza.sampleproject.domain.repository

import com.reza.sampleproject.domain.model.Job
import org.neshan.common.model.LatLng

interface JobRepository {
    suspend fun getJobs(center: LatLng, radiusKm: Float, count: Int = 20): List<Job>
}


