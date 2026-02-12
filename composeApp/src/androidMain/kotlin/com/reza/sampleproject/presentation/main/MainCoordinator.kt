package com.reza.sampleproject.presentation.main

import android.location.Location
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.presentation.map.MapJobRenderer
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Label
import org.neshan.mapsdk.model.Marker

class MainCoordinator(
    private val mapRenderer: MapJobRenderer
) {

    private var userLocation: Location? = null

    fun bindMap(map: MapView, onJobClick: (Job) -> Unit) {
        map.setOnMarkerClickListener(object : MapView.OnMarkerClickListener {
            override fun OnMarkerClicked(marker: Marker?) {
                val job = mapRenderer.getJobForMarker(marker) ?: return
                onJobClick(job)
            }
        })

        map.setOnLabelClickListener(object : MapView.OnLabelClickListener {
            override fun OnLabelClicked(label: Label?) {
                val job = mapRenderer.getJobForLabel(label) ?: return
                onJobClick(job)
            }
        })
    }

    fun applyLocation(location: Location, radiusKm: Float) {
        userLocation = location
        mapRenderer.moveCameraTo(
            LatLng(location.latitude, location.longitude),
            14f,
            0.5f
        )
        mapRenderer.drawUserArea(location, radiusKm)
        mapRenderer.updateUserLocation(location)
    }

    fun applyJobs(jobs: List<Job>) {
        mapRenderer.renderJobs(jobs)
    }

    fun updateUserArea(radiusKm: Float) {
        val location = userLocation ?: return
        mapRenderer.drawUserArea(location, radiusKm)
    }

    fun getUserLocation(): Location? = userLocation
}

