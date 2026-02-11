package com.reza.sampleproject.domain.model

import org.neshan.common.model.LatLng

data class Job(
    val id: Int,
    val title: String,
    val category: String,
    val latLng: LatLng
)


