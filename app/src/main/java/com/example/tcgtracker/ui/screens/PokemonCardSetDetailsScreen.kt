package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModel
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModelFactory
import com.example.tcgtracker.viewmodels.PokemonCardsViewModel
import net.tcgdex.sdk.Extension

/**
 * Displays details for a specific card set.
 *
 * @param navController - The NavController used for navigation between screens.
 * @param setId - The ID of the card set to display.
 * @param setsViewModel - The CardSetsViewModel providing state and actions for sets.
 */
@Composable
fun PokemonCardSetDetailsScreen(
    navController: NavController,
    setId: String,
    repository: PokemonCardRepository
) {
    val setsViewModel: PokemonCardSetsViewModel = viewModel(
        factory = PokemonCardSetsViewModelFactory(repository)
    )
    val cardsViewModel: PokemonCardsViewModel = viewModel(
        factory = PokemonCardSetsViewModelFactory(repository)
    )

    val loadedCardSets by setsViewModel.loadedCardSets.collectAsState()
    val loadedCards by cardsViewModel.loadedApiCards.collectAsState()
    val loading by setsViewModel.loading.collectAsState()
    val error by setsViewModel.error.collectAsState()

    LaunchedEffect(setId) {
        if (!loadedCardSets.containsKey(setId)) {
            setsViewModel.fetchFullCardSet(setId)
        }
    }

    val cardSet = loadedCardSets[setId]

    if (loading || cardSet == null) {
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

    val cardsInSetResume = cardSet.cards

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            AsyncImage(
                model = cardSet.getLogoUrl(Extension.PNG),
                contentDescription = cardSet.name,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.navigate("pokemonCardSetDetailsScreen/${cardSet.id}")
                    }
            )

            Text(
                text = "Set Name: ${cardSet.name}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Cards in Set: ${cardSet.cardCount.total}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Cards in this Set",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(cardsInSetResume) { cardResume ->
                    val fullCard = loadedCards[cardResume.id]

                    // Fetch card if not loaded
                    LaunchedEffect(cardResume.id) {
                        if (fullCard == null) {
                            cardsViewModel.fetchFullCard(cardResume.id)
                        }
                    }

                    Column(
                        modifier = Modifier
                            .clickable {
                                navController.navigate("pokemonCardDetailsScreen/${cardResume.id}")
                            }
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (fullCard != null) {
                            cardSet.logo?.let { logoUrl ->
                                AsyncImage(
                                    model = "$logoUrl/${Extension.PNG.value}",
                                    contentDescription = fullCard.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(0.7f)
                                )
                                fullCard.name?.let {
                                    Text(
                                        text = it,
                                        modifier = Modifier.padding(top = 4.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.7f)
                                    .background(Color.Gray)
                            )
                            Text(
                                text = cardResume.name,
                                modifier = Modifier.padding(top = 4.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

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
