package com.reza.sampleproject.presentation.main.ui

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.reza.sampleproject.R
import com.reza.sampleproject.domain.model.Job

class JobDetailsDialog(
    private val context: Context
) {
    fun show(job: Job) {
        val message = buildString {
            append(context.getString(R.string.job_title_label))
            append(job.title)
            append("\n")
            append(context.getString(R.string.job_category_label))
            append(job.category)
        }

        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.job_details_title))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.ok), null)
            .show()
    }
}
