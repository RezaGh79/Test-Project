package com.reza.sampleproject

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.usecase.GetUserLastLocationUseCase
import com.reza.sampleproject.presentation.main.MainScreen
import com.reza.sampleproject.presentation.main.MainViewModel
import com.reza.sampleproject.presentation.map.MapJobRenderer
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Label
import org.neshan.mapsdk.model.Marker

class MainActivity : AppCompatActivity() {

    private val requestCode = 123

    private lateinit var map: MapView
    private lateinit var mapRenderer: MapJobRenderer

    private var userLocation: Location? = null

    private lateinit var composeView: ComposeView
    private val viewModel: MainViewModel by viewModel()
    private val getUserLastLocationUseCase: GetUserLastLocationUseCase by inject()

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
        mapRenderer.moveCameraTo(LatLng(35.767234, 51.330743), 14f)
    }

    private fun initViews() {
        map = findViewById(R.id.map)
        mapRenderer = MapJobRenderer(this, map)
        composeView = findViewById(R.id.compose_view)

        map.setOnMarkerClickListener(object : MapView.OnMarkerClickListener {
            override fun OnMarkerClicked(marker: Marker?) {
                val title = marker?.title ?: return
                runOnUiThread {
                    val job = viewModel.uiState.value.allJobs.find { it.title == title }
                        ?: return@runOnUiThread
                    focusOnJob(job)
                }
            }
        })

        map.setOnLabelClickListener(object : MapView.OnLabelClickListener {
            override fun OnLabelClicked(label: Label?) {
                val text = label?.text ?: return
                runOnUiThread {
                    val job = viewModel.uiState.value.allJobs.find { it.title == text }
                        ?: return@runOnUiThread
                    focusOnJob(job)
                }
            }
        })
    }

    private fun showJobDetails(title: String) {
        val job = viewModel.uiState.value.allJobs.find { it.title == title } ?: return

        val message = buildString {
            append(getString(R.string.job_title_label))
            append(job.title)
            append("\n")
            append(getString(R.string.job_category_label))
            append(job.category)
        }

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.job_details_title))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok), null)
            .show()
    }

    private fun focusOnJob(job: Job) {
        mapRenderer.moveCameraTo(job.latLng, 16f, 0.5f)
        showJobDetails(job.title)
    }

    private fun updateMapMarkers(jobs: List<Job>) {
        mapRenderer.renderJobs(jobs)
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
                    val center = userLocation?.let { LatLng(it.latitude, it.longitude) }
                        ?: LatLng(35.767234, 51.330743)
                    viewModel.searchJobs(center, radius)
                },
                onStateChange = { state ->
                    updateMapMarkers(state.filteredJobs)
                    if (userLocation != null) {
                        drawUserAreaCircle()
                    }
                }
            )
        }
    }

    fun focusOnUserLocation(view: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                requestUserLastLocation()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), requestCode
                )
            }
        } else {
            requestUserLastLocation()
        }
    }

    private fun requestUserLastLocation() {
        lifecycleScope.launch {
            val location = getUserLastLocationUseCase()
            if (location != null) {
                userLocation = location
                onLocationChange()
            }
        }
    }

    private fun onLocationChange() {
        if (userLocation != null) {
            mapRenderer.moveCameraTo(
                LatLng(userLocation!!.latitude, userLocation!!.longitude),
                14f,
                0.5f
            )
            drawUserAreaCircle()
            mapRenderer.updateUserLocation(userLocation!!)
        }
    }

    private fun drawUserAreaCircle() {
        val location = userLocation ?: return
        val radiusKm = viewModel.getCurrentRadiusKm()
        mapRenderer.drawUserArea(location, radiusKm)
    }
}

