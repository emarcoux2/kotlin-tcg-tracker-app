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
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.viewmodels.PokemonCardsViewModel
import com.example.tcgtracker.R
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.viewmodels.PokemonCardsViewModelFactory

/**
 * Displays the details of an individual Pokemon card.
 *
 * @param navController - The object responsible for navigation between composable screens.
 * @param viewModel - The PokemonCardsViewModel providing the state and actions
 * for this screen.
 * @param cardId - The ID of the card that should be displayed.
 */
@Composable
fun PokemonCardDetailsScreen(
    navController: NavController,
    cardId: String,
    viewModel: PokemonCardsViewModel = viewModel(),
    repository: PokemonCardRepository
) {
    val viewModel: PokemonCardsViewModel = viewModel(
        factory = PokemonCardsViewModelFactory(repository)
    )

    val apiCardMap by viewModel.loadedApiCards.collectAsState()
    val userPokemonCardMap by viewModel.userPokemonCards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(cardId) {
        if (!userPokemonCardMap.containsKey(cardId)) {
            viewModel.fetchFullCard(cardId)
        }
    }

    val apiCard = apiCardMap[cardId]
    val userPokemonCard = userPokemonCardMap[cardId]

    if (loading || apiCard == null) {
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

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Image
            apiCard.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    placeholder = painterResource(R.drawable.ic_card_details),
                    error = painterResource(R.drawable.ic_card_details),
                    contentDescription = apiCard.name ?: "Unknown Card",
                    modifier = Modifier.fillMaxWidth().aspectRatio(0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Card Info
            Text(
                text = "Card Name: ${apiCard.name ?: "Unknown"}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = apiCard.description ?: "No description available",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Set Logo
            apiCard.setLogo?.let { logoUrl ->
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

                    apiCard?.setLogo?.let { logoUrl ->
                        AsyncImage(
                            model = logoUrl,
                            placeholder = painterResource(R.drawable.ic_all_card_sets),
                            error = painterResource(R.drawable.ic_all_card_sets),
                            contentDescription = apiCard.setName ?: "Unknown Set",
                            modifier = Modifier
                                .size(250.dp)
                                .clickable {
                                    apiCard.setId?.let { setId ->
                                        navController.navigate("pokemonCardSetDetailsScreen/$setId")
                                    }
                                }
                        )
                    }

                    Text(
                        text = apiCard.setName ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .clickable {
                                apiCard.setId?.let { setId ->
                                    navController.navigate("pokemonCardSetDetailsScreen/$setId")
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add to Collection Button
            if (userPokemonCard == null) {
                Button(onClick = { viewModel.addToUserCardsCollection(cardId) }) {
                    Text("Add to My Collection")
                }
            } else {
                Text(
                    text = "You already own this card!",
                    style = MaterialTheme.typography.bodyMedium
                )
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