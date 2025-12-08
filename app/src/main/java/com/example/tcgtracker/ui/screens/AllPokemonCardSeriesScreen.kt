package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.viewmodels.PokemonCardSeriesViewModel
import net.tcgdex.sdk.Extension

/**
 * Displays all Pokemon card series.
 *
 * @param navController - The object responsible for navigation between composable screens.
 * @param viewModel -
 */
@Composable
fun AllPokemonCardSeriesScreen(
    navController: NavController,
    viewModel: PokemonCardSeriesViewModel = viewModel()
) {

    val pokemonCardSeries by viewModel.allPokemonCardSeries.collectAsState()
    val fullCardSeries by viewModel.loadedCardSeries.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    if (loading) {
        Box(Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    error?.let {
        Box(Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Text("Error: $it")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pokemonCardSeries) { setResume ->
            val logoUrl = setResume.getLogoUrl(Extension.PNG)
            val name = setResume.name

            Column(
                modifier = Modifier
                    .clickable {
                        setResume.id.let { navController.navigate("allPokemonCardSetsScreen/$it") }
                    }
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = setResume.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                )
                Text(
                    text = setResume.name,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}