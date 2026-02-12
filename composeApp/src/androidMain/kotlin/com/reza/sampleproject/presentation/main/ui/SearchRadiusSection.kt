package com.reza.sampleproject.presentation.main.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reza.sampleproject.R

@Composable
fun SearchRadiusSection(
    radius: Int,
    onRadiusChange: (Int) -> Unit,
    onSearchClick: () -> Unit
) {
    var radiusMenuExpanded by remember { mutableStateOf(false) }

    Text(
        text = stringResource(id = R.string.search_radius_label),
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
            Text(text = radius.toString())
        }
        DropdownMenu(
            expanded = radiusMenuExpanded,
            onDismissRequest = { radiusMenuExpanded = false }
        ) {
            (1..20).forEach { value ->
                DropdownMenuItem(
                    text = { Text(text = value.toString()) },
                    onClick = {
                        onRadiusChange(value)
                        radiusMenuExpanded = false
                    }
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = onSearchClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Text(text = stringResource(id = R.string.search_button))
    }
}


