package com.reza.sampleproject.di

import com.google.android.gms.location.LocationServices
import com.reza.sampleproject.data.repository.JobRepositoryImpl
import com.reza.sampleproject.data.repository.LocationRepositoryImpl
import com.reza.sampleproject.domain.repository.JobRepository
import com.reza.sampleproject.domain.repository.LocationRepository
import com.reza.sampleproject.domain.usecase.FilterJobsByCategoryUseCase
import com.reza.sampleproject.domain.usecase.GetJobCategoriesUseCase
import com.reza.sampleproject.domain.usecase.GetUserLastLocationUseCase
import com.reza.sampleproject.domain.usecase.SearchJobsUseCase
import com.reza.sampleproject.presentation.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<JobRepository> { JobRepositoryImpl() }

    factory { SearchJobsUseCase(get()) }
    factory { FilterJobsByCategoryUseCase() }
    factory { GetJobCategoriesUseCase() }
    factory { GetUserLastLocationUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get()) }
}