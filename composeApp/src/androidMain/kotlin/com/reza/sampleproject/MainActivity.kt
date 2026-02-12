package com.reza.sampleproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.usecase.GetUserLastLocationUseCase
import com.reza.sampleproject.presentation.main.MainCoordinator
import com.reza.sampleproject.presentation.main.MainViewModel
import com.reza.sampleproject.presentation.main.ui.JobDetailsDialog
import com.reza.sampleproject.presentation.main.ui.MainScreen
import com.reza.sampleproject.presentation.map.MapJobRenderer
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView

class MainActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private lateinit var mapRenderer: MapJobRenderer
    private lateinit var coordinator: MainCoordinator

    private lateinit var composeView: ComposeView
    private val viewModel: MainViewModel by viewModel()
    private val getUserLastLocationUseCase: GetUserLastLocationUseCase by inject()
    private val jobDetailsDialog by lazy { JobDetailsDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)
    }

    override fun onStart() {
        super.onStart()
        initLayoutReference()
    }

    private fun initLayoutReference() {
        initViews()
        initMap()
        initCompose()
    }

    private fun initMap() {
        mapRenderer.moveCameraTo(LatLng(DEFAULT_LAT, DEFAULT_LON), 14f)
    }

    private fun initViews() {
        map = findViewById(R.id.map)
        mapRenderer = MapJobRenderer(this, map)
        coordinator = MainCoordinator(mapRenderer)
        composeView = findViewById(R.id.compose_view)
        coordinator.bindMap(map) { job -> focusOnJob(job) }
    }

    private fun showJobDetails(job: Job) {
        jobDetailsDialog.show(job)
    }

    private fun focusOnJob(job: Job) {
        mapRenderer.moveCameraTo(job.latLng, 16f, 0.5f)
        showJobDetails(job)
    }

    private fun updateMapMarkers(jobs: List<Job>) {
        coordinator.applyJobs(jobs)
    }

    private fun initCompose() {
        composeView.setContent {
            MainScreen(
                viewModel = viewModel,
                onJobClick = { job ->
                    focusOnJob(job)
                },
                onCurrentLocationClick = {
                    focusOnUserLocation(null)
                },
                onSearchClick = { radius ->
                    val center =
                        coordinator.getUserLocation()?.let { LatLng(it.latitude, it.longitude) }
                            ?: LatLng(DEFAULT_LAT, DEFAULT_LON)
                    viewModel.searchJobs(center, radius)
                },
                onStateChange = { state ->
                    updateMapMarkers(state.filteredJobs)
                    coordinator.updateUserArea(state.radiusKm.toFloat())
                }
            )
        }
    }

    fun focusOnUserLocation(view: View?) {
        if (hasLocationPermission()) {
            requestUserLastLocation()
        } else {
            requestLocationPermissions(REQUEST_CODE_LOCATION)
        }
    }

    private fun requestUserLastLocation() {
        lifecycleScope.launch {
            val location = getUserLastLocationUseCase()
            if (location != null) {
                coordinator.applyLocation(location, viewModel.getCurrentRadiusKm())
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_LOCATION = 123
        private const val DEFAULT_LAT = 35.767234
        private const val DEFAULT_LON = 51.330743
    }
}

