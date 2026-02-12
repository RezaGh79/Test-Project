package com.reza.sampleproject.data.repository

import com.reza.sampleproject.data.fake.JobsGenerator
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.repository.JobRepository
import org.neshan.common.model.LatLng

class JobRepositoryImpl(
    private val jobsGenerator: JobsGenerator = JobsGenerator()
) : JobRepository {

    override suspend fun getJobs(center: LatLng, radiusKm: Float, count: Int): List<Job> {
        return jobsGenerator.generate(center, radiusKm, count)
    }
}

