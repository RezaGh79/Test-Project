package com.reza.sampleproject.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.usecase.FilterJobsByCategoryUseCase
import com.reza.sampleproject.domain.usecase.GetJobCategoriesUseCase
import com.reza.sampleproject.domain.usecase.SearchJobsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.neshan.common.model.LatLng

class MainViewModel(
    private val searchJobsUseCase: SearchJobsUseCase,
    private val filterJobsByCategoryUseCase: FilterJobsByCategoryUseCase,
    private val getJobCategoriesUseCase: GetJobCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var allJobs: List<Job> = emptyList()
    private var currentRadiusKm: Float = 5f
    private var currentCenter: LatLng? = null

    fun updateRadius(radiusKm: Int) {
        currentRadiusKm = radiusKm.toFloat()
        _uiState.value = _uiState.value.copy(radiusKm = radiusKm)
    }

    fun searchJobs(center: LatLng, radiusKm: Int) {
        currentCenter = center
        currentRadiusKm = radiusKm.toFloat()
        
        viewModelScope.launch {
            try {
                val jobs = searchJobsUseCase(center, radiusKm.toFloat())
                allJobs = jobs
                val categories = getJobCategoriesUseCase(jobs)
                
                _uiState.value = _uiState.value.copy(
                    allJobs = jobs,
                    filteredJobs = jobs,
                    categories = categories,
                    isShowingResults = true,
                    radiusKm = radiusKm
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun filterByCategory(category: String?) {
        val filteredJobs = filterJobsByCategoryUseCase(allJobs, category)
        
        _uiState.value = _uiState.value.copy(
            filteredJobs = filteredJobs,
            selectedCategory = category
        )
    }

    fun getCurrentRadiusKm(): Float = currentRadiusKm
}

data class MainUiState(
    val allJobs: List<Job> = emptyList(),
    val filteredJobs: List<Job> = emptyList(),
    val categories: List<String> = emptyList(),
    val isShowingResults: Boolean = false,
    val error: String? = null,
    val radiusKm: Int = 5,
    val selectedCategory: String? = null
)

