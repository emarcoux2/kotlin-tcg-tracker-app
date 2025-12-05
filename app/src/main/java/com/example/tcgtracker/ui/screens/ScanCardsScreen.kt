package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgtracker.components.ScreenLabel

/**
 * This screen allows a logged-in user to scan a physical card using the device camera.
 *
 * @param navController - The object responsible for navigation between composable screens.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanCardsScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenLabel("Scan Card")

        FloatingActionButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}