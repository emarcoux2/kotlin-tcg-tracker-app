package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModel
import net.tcgdex.sdk.Extension

/**
 * Displays all Pokemon card sets by the selected series.
 */
@Composable
fun PokemonCardSetsBySeriesScreen(
    navController: NavController,
    viewModel: PokemonCardSetsViewModel = viewModel(),
    cardSeriesId: String?
) {
    val allPokemonCardSetPreviews by viewModel.allPokemonCardSetPreviews.collectAsState()
    val fullCardSets by viewModel.loadedCardSets.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(allPokemonCardSetPreviews) {
        allPokemonCardSetPreviews.forEach { preview ->
            preview.id.let { viewModel.fetchFullCardSet(it) }
        }
    }

    val filteredCardSets = fullCardSets.values.filter { set ->
        set.serie.id == cardSeriesId
    }

    if (loading || fullCardSets.isEmpty()) {
        Box(
            Modifier.fillMaxSize(),
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
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(filteredCardSets) { set ->
            val logoUrl = set.getLogoUrl(Extension.PNG)

            Column(
                modifier = Modifier.clickable {
                    navController.navigate("pokemonCardSetDetailsScreen/${set.id}")
                }
            ) {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = set.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                )

                Text(
                    text = set.name ?: "",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}