package com.reza.sampleproject.presentation.main.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reza.sampleproject.domain.model.Job

@Composable
fun JobsList(
    jobs: List<Job>,
    onJobClick: (Job) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        items(jobs) { job ->
            JobRow(
                job = job,
                onClick = { onJobClick(job) }
            )
        }
    }
}


