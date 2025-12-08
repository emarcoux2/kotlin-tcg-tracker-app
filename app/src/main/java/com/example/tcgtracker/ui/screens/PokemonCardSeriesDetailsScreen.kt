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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tcgtracker.viewmodels.PokemonCardSeriesViewModel
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModel
import net.tcgdex.sdk.Extension

@Composable
fun PokemonCardSeriesDetailsScreen (
    id: String,
    setsViewModel: PokemonCardSetsViewModel = viewModel(),
    seriesViewModel: PokemonCardSeriesViewModel = viewModel()
) {
    val allPokemonCardSetPreviews by setsViewModel.allPokemonCardSetPreviews.collectAsState()
    val fullCardSets by setsViewModel.loadedCardSets.collectAsState()
    val loading by setsViewModel.loading.collectAsState()
    val error by setsViewModel.error.collectAsState()

    val cardSeriesList by seriesViewModel.allPokemonCardSeries.collectAsState()
    val cardSeries = cardSeriesList.find { it.id == id }

    if (loading || cardSeries == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    error?.let {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: $it")
        }
        return
    }

    LaunchedEffect(allPokemonCardSetPreviews) {
        allPokemonCardSetPreviews.forEach { preview ->
            setsViewModel.fetchFullCardSet(preview.id)
        }
    }

    val setsInSerie = fullCardSets.values.filter { it.serie.id == id!! }

    Column(Modifier.fillMaxSize()) {

        Text(
            text = cardSeries.name,
            fontSize = 22.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(setsInSerie) { set ->

                val logoUrl = set.getLogoUrl(Extension.PNG)

                Column(
                    modifier = Modifier.clickable {
                        // Navigate to YOUR Set Details screen:
                        // (you already built this!)
                        // navController.navigate("setDetails/${set.id}")
                    }
                ) {
                    AsyncImage(
                        model = logoUrl,
                        contentDescription = set.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    set.name?.let {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}