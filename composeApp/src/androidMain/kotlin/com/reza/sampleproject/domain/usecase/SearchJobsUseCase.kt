package com.reza.sampleproject.domain.usecase

import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.repository.JobRepository
import org.neshan.common.model.LatLng

class SearchJobsUseCase(
    private val repository: JobRepository
) {
    suspend operator fun invoke(center: LatLng, radiusKm: Float, count: Int = 20): List<Job> {
        return repository.getJobs(center, radiusKm, count)
    }
}


