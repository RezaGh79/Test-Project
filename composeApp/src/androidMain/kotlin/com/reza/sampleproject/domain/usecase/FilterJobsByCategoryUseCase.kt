package com.reza.sampleproject.domain.usecase

import com.reza.sampleproject.domain.model.Job

class FilterJobsByCategoryUseCase {
    operator fun invoke(jobs: List<Job>, category: String?): List<Job> {
        return if (category == null) {
            jobs
        } else {
            jobs.filter { it.category == category }
        }
    }
}


