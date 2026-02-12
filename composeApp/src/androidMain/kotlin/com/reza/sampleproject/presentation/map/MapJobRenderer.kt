package com.reza.sampleproject.presentation.map

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.carto.graphics.Bitmap
import com.carto.graphics.Color
import com.carto.styles.LineStyleBuilder
import com.carto.styles.MarkerStyle
import com.carto.styles.MarkerStyleBuilder
import com.carto.styles.TextStyle
import com.carto.styles.TextStyleBuilder
import com.carto.utils.BitmapUtils
import com.reza.sampleproject.R
import com.reza.sampleproject.domain.model.Job
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Circle
import org.neshan.mapsdk.model.Label
import org.neshan.mapsdk.model.Marker

class MapJobRenderer(
    private val context: Context,
    private val map: MapView
) {

    private val currentMarkers = ArrayList<Marker>()
    private val currentLabels = ArrayList<Label>()
    private val markerJobMap = mutableMapOf<Marker, Job>()
    private val labelJobMap = mutableMapOf<Label, Job>()
    private var areaCircle: Circle? = null
    private var userMarker: Marker? = null

    private val markerStyle: MarkerStyle by lazy { createMarkerStyle() }
    private val textStyle: TextStyle by lazy { createTextStyle() }

    fun moveCameraTo(location: LatLng, zoom: Float, duration: Float = 0f) {
        map.moveCamera(location, duration)
        map.setZoom(zoom, duration)
    }

    fun drawUserArea(location: Location, radiusKm: Float) {
        val center = LatLng(location.latitude, location.longitude)
        val radiusMeters = (radiusKm * 1000f).toDouble()

        val fillColor = Color(0x55228BE6)
        val outlineBuilder = LineStyleBuilder().apply {
            color = Color(0xFFFFFFFF.toInt())
            width = 3f
        }

        val circle = Circle(center, radiusMeters, fillColor, outlineBuilder.buildStyle())

        areaCircle?.let { map.removeCircle(it) }
        areaCircle = circle
        map.addCircle(circle)
    }

    fun updateUserLocation(location: Location) {
        val iconBitmap = getBitmapFromVectorDrawable(
            drawableId = R.drawable.baseline_my_location_24,
            tintColor = 0xFF2196F3.toInt()
        ) ?: return

        val styleBuilder = MarkerStyleBuilder().apply {
            size = 30f
            bitmap = iconBitmap
        }

        val position = LatLng(location.latitude, location.longitude)

        userMarker?.let { map.removeMarker(it) }

        val marker = Marker(position, styleBuilder.buildStyle())
        map.addMarker(marker)
        userMarker = marker
    }

    fun renderJobs(jobs: List<Job>) {
        clearMarkers()

        for (job in jobs) {
            val marker = Marker(job.latLng, markerStyle).apply {
                title = job.title
            }
            map.addMarker(marker)
            currentMarkers.add(marker)
            markerJobMap[marker] = job

            val label = Label(job.latLng, textStyle, job.title)
            map.addLabel(label)
            currentLabels.add(label)
            labelJobMap[label] = job
        }
    }

    fun clearMarkers() {
        for (marker in currentMarkers) {
            map.removeMarker(marker)
        }
        currentMarkers.clear()
        markerJobMap.clear()

        for (label in currentLabels) {
            map.removeLabel(label)
        }
        currentLabels.clear()
        labelJobMap.clear()
    }

    fun getJobForMarker(marker: Marker?): Job? {
        if (marker == null) return null
        return markerJobMap[marker]
    }

    fun getJobForLabel(label: Label?): Job? {
        if (label == null) return null
        return labelJobMap[label]
    }

    private fun createMarkerStyle(): MarkerStyle {
        val androidBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pin)
        val cartoBitmap = if (androidBitmap != null) {
            BitmapUtils.createBitmapFromAndroidBitmap(androidBitmap)
        } else {
            null
        }
        val styleBuilder = MarkerStyleBuilder()
        styleBuilder.size = 40f
        if (cartoBitmap != null) {
            styleBuilder.bitmap = cartoBitmap
        }
        return styleBuilder.buildStyle()
    }

    private fun createTextStyle(): TextStyle {
        val textStyleBuilder = TextStyleBuilder()
        textStyleBuilder.fontSize = 12f
        textStyleBuilder.color = Color(0xFF000000.toInt())
        textStyleBuilder.strokeColor = Color(0xFFFFFFFF.toInt())
        textStyleBuilder.strokeWidth = 2f
        return textStyleBuilder.buildStyle()
    }

    fun getBitmapFromVectorDrawable(drawableId: Int, tintColor: Int? = null): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
        val canvas = Canvas(bitmap)
        if (tintColor != null) {
            drawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        }
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return BitmapUtils.createBitmapFromAndroidBitmap(bitmap)
    }
}


