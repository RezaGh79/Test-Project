package com.reza.sampleproject.domain.usecase

import com.reza.sampleproject.domain.model.Job

class GetJobCategoriesUseCase {
    operator fun invoke(jobs: List<Job>): List<String> {
        return jobs.map { it.category }.distinct()
    }
}


