package com.reza.sampleproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.domain.usecase.JobsGenerator
import com.reza.sampleproject.presentation.main.MainScreen
import com.reza.sampleproject.presentation.map.MapJobRenderer
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Label
import org.neshan.mapsdk.model.Marker

class MainActivity : AppCompatActivity() {

    private val requestCode = 123

    private lateinit var map: MapView
    private lateinit var mapRenderer: MapJobRenderer

    private var userLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var radiusKm: Float = 5f

    private lateinit var composeView: ComposeView
    private val allJobs = ArrayList<Job>()
    private val jobsGenerator = JobsGenerator()
    private var jobsState by mutableStateOf<List<Job>>(emptyList())
    private var categoriesState by mutableStateOf<List<String>>(emptyList())
    private var isShowingResults by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source)
    }

    override fun onStart() {
        super.onStart()

        initLayoutReference()
        initLocation()

    }

    fun initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == this.requestCode) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                requestUserLastLocation()
            }
        }
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
                    val job = allJobs.find { it.title == title } ?: return@runOnUiThread
                    focusOnJob(job)
                }
            }
        })

        map.setOnLabelClickListener(object : MapView.OnLabelClickListener {
            override fun OnLabelClicked(label: Label?) {
                val text = label?.text ?: return
                runOnUiThread {
                    val job = allJobs.find { it.title == text } ?: return@runOnUiThread
                    focusOnJob(job)
                }
            }
        })
    }

    private fun showJobDetails(title: String) {
        val job = allJobs.find { it.title == title } ?: return

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

    private fun generateJobs() {
        allJobs.clear()

        val center = userLocation?.let { LatLng(it.latitude, it.longitude) }
            ?: LatLng(35.767234, 51.330743)

        val jobs = jobsGenerator.generate(center, radiusKm)
        allJobs.addAll(jobs)
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
                jobs = jobsState,
                categories = categoriesState,
                isShowingResults = isShowingResults,
                onRadiusChange = { newRadius ->
                    radiusKm = newRadius.toFloat()
                    drawUserAreaCircle()
                },
                onSearch = { radius ->
                    radiusKm = radius.toFloat()
                    drawUserAreaCircle()
                    generateJobs()
                    categoriesState = allJobs.map { it.category }.distinct()
                    jobsState = allJobs.toList()
                    isShowingResults = true
                    updateMapMarkers(allJobs)
                },
                onFilterSelected = { category ->
                    if (category == null) {
                        jobsState = allJobs.toList()
                        updateMapMarkers(allJobs)
                    } else {
                        val filtered = allJobs.filter { it.category == category }
                        jobsState = filtered
                        updateMapMarkers(filtered)
                    }
                },
                onJobClick = { job ->
                    focusOnJob(job)
                },
                onCurrentLocationClick = {
                    focusOnUserLocation(null)
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


    @SuppressLint("MissingPermission")
    private fun requestUserLastLocation() {

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                this.userLocation = location
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
        mapRenderer.drawUserArea(location, radiusKm)
    }
}
