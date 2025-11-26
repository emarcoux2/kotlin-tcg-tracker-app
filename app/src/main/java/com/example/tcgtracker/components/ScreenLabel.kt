package com.example.tcgtracker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a label at the top of the screen describing what screen the user is viewing.
 *
 * @param text - A String that displays the name of the screen being viewed.
 */
@Composable
fun ScreenLabel(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}