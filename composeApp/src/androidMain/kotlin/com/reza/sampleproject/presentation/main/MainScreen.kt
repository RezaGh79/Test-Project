package com.reza.sampleproject.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reza.sampleproject.domain.model.Job

@Composable
fun MainScreen(
    jobs: List<Job>,
    categories: List<String>,
    isShowingResults: Boolean,
    onSearch: (Int) -> Unit,
    onFilterSelected: (String?) -> Unit,
    onJobClick: (Job) -> Unit,
    onCurrentLocationClick: () -> Unit
) {
    var radius by remember { mutableStateOf(5) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var radiusMenuExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = onCurrentLocationClick,
                    containerColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (!isShowingResults) {
                        Text(
                            text = "شعاع جستجو (کیلومتر)",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box {
                            Button(
                                onClick = { radiusMenuExpanded = true },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "$radius")
                            }
                            DropdownMenu(
                                expanded = radiusMenuExpanded,
                                onDismissRequest = { radiusMenuExpanded = false }
                            ) {
                                (1..20).forEach { value ->
                                    DropdownMenuItem(
                                        text = { Text(text = value.toString()) },
                                        onClick = {
                                            radius = value
                                            radiusMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onSearch(radius) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "جستجو")
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ElevatedFilterChip(
                                selected = selectedCategory == null,
                                onClick = {
                                    selectedCategory = null
                                    onFilterSelected(null)
                                },
                                label = { Text(text = "همه") }
                            )
                            categories.forEach { category ->
                                ElevatedFilterChip(
                                    selected = selectedCategory == category,
                                    onClick = {
                                        selectedCategory = category
                                        onFilterSelected(category)
                                    },
                                    label = { Text(text = category) }
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            items(jobs) { job ->
                                JobRow(job = job, onClick = { onJobClick(job) })
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun JobRow(
    job: Job,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Transparent)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = job.title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = job.category,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


