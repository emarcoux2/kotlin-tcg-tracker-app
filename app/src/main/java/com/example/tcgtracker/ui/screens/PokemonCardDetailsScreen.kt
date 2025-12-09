package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.viewmodels.PokemonCardsViewModel
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality

/**
 * Displays the details of an individual Pokemon card.
 *
 * @param navController - The object responsible for navigation between composable screens.
 * @param viewModel - The PokemonCardsViewModel providing the state and actions
 * for this screen.
 * @param cardId - 
 */
@Composable
fun PokemonCardDetailsScreen(
    navController: NavController,
    cardId: String,
    viewModel: PokemonCardsViewModel = viewModel()
) {
    val pokemonCardMap by viewModel.loadedCards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(cardId) {
        if (!pokemonCardMap.containsKey(cardId)) {
            viewModel.fetchFullCard(cardId)
        }
    }

    val card = pokemonCardMap[cardId]

    if (loading || card == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = card.getImageUrl(Quality.HIGH, Extension.JPG),
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Card Name: ${card.name}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Card Type: ${card.types?.joinToString()}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = card.description ?: "No description available",
                style = MaterialTheme.typography.bodySmall
            )
            card.set.let { cardSet ->
                val logoUrl = cardSet.getLogoUrl(Extension.PNG)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Belongs to:",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    AsyncImage(
                        model = logoUrl,
                        contentDescription = cardSet.name,
                        modifier = Modifier
                            .size(250.dp)
                            .clickable {
                                navController.navigate("pokemonCardSetDetailsScreen/${cardSet.id}")
                            }
                    )
                    Text(
                        text = cardSet.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable {
                                navController.navigate("pokemonCardSetDetailsScreen/${cardSet.id}")
                            }
                    )
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