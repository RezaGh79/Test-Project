package com.reza.sampleproject.presentation.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reza.sampleproject.domain.model.Job
import com.reza.sampleproject.presentation.main.MainUiState
import com.reza.sampleproject.presentation.main.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onJobClick: (Job) -> Unit,
    onCurrentLocationClick: () -> Unit,
    onSearchClick: (Int) -> Unit,
    onStateChange: (MainUiState) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var radius by remember { mutableIntStateOf(uiState.radiusKm) }

    LaunchedEffect(uiState.radiusKm) {
        radius = uiState.radiusKm
    }

    LaunchedEffect(uiState) {
        onStateChange(uiState)
    }

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
                    if (!uiState.isShowingResults) {
                        SearchRadiusSection(
                            radius = radius,
                            onRadiusChange = {
                                radius = it
                                viewModel.updateRadius(it)
                            },
                            onSearchClick = { onSearchClick(radius) }
                        )
                    } else {
                        CategoryFilterRow(
                            categories = uiState.categories,
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelected = { category ->
                                viewModel.filterByCategory(category)
                            }
                        )
                        JobsList(
                            jobs = uiState.filteredJobs,
                            onJobClick = onJobClick
                        )
                    }
                }
            }
        }
    }
}
