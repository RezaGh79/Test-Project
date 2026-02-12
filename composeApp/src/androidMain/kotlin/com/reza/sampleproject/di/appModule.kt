package com.reza.sampleproject.di

import com.reza.sampleproject.data.repository.JobRepositoryImpl
import com.reza.sampleproject.domain.repository.JobRepository
import com.reza.sampleproject.domain.usecase.FilterJobsByCategoryUseCase
import com.reza.sampleproject.domain.usecase.GetJobCategoriesUseCase
import com.reza.sampleproject.domain.usecase.SearchJobsUseCase
import com.reza.sampleproject.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<JobRepository> { JobRepositoryImpl() }

    factory { SearchJobsUseCase(get()) }
    factory { FilterJobsByCategoryUseCase() }
    factory { GetJobCategoriesUseCase() }

    viewModel { MainViewModel(get(), get(), get()) }
}