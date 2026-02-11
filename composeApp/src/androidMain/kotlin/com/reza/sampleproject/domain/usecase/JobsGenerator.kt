package com.reza.sampleproject.domain.usecase

import com.reza.sampleproject.domain.model.Job
import org.neshan.common.model.LatLng
import java.lang.Math.toRadians
import kotlin.math.PI
import kotlin.math.cos

class JobsGenerator {

    fun generate(center: LatLng, radiusKm: Float, count: Int = 20): List<Job> {
        val jobsByCategory: Map<String, List<String>> = mapOf(
            "خدمات خانگی" to listOf(
                "نظافت منزل",
                "پرستار کودک",
                "نگهداری سالمند"
            ),
            "آموزشی" to listOf(
                "تدریس ریاضی",
                "تدریس فیزیک",
                "تدریس زبان انگلیسی"
            ),
            "ساختمانی" to listOf(
                "نجاری",
                "نقاشی ساختمان",
                "کاشی‌کاری"
            ),
            "فنی" to listOf(
                "تعمیرکار موبایل",
                "تعمیرکار لوازم خانگی",
                "نصاب کولر گازی"
            ),
            "حمل و نقل" to listOf(
                "جابجایی اثاثیه",
                "پیک موتوری",
                "راننده درون‌شهری"
            )
        )

        val categories = jobsByCategory.keys.toList()
        val result = ArrayList<Job>()

        repeat(count) { index ->
            val dist = kotlin.random.Random.nextDouble() * radiusKm
            val angle = kotlin.random.Random.nextDouble() * 2 * PI

            val latOffset = (dist * cos(angle)) / 111.0
            val cosLat = cos(toRadians(center.latitude))
            val lngOffset = (dist * kotlin.math.sin(angle)) / (111.0 * cosLat)

            val jobLocation = LatLng(center.latitude + latOffset, center.longitude + lngOffset)

            val category = categories.random()
            val titlesForCategory = jobsByCategory[category].orEmpty()
            val jobTitle = if (titlesForCategory.isNotEmpty()) {
                titlesForCategory.random()
            } else {
                "درخواست خدمات"
            }

            val id = index + 1
            val fullTitle = "$jobTitle (#$id)"
            result.add(Job(id, fullTitle, category, jobLocation))
        }

        return result
    }
}


