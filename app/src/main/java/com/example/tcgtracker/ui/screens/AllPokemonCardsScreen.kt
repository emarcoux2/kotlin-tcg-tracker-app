package com.example.tcgtracker.ui.screens

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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import coil.compose.AsyncImage
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.navigation.NavHostController
import com.example.tcgtracker.R
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.viewmodels.PokemonCardsViewModel
import com.example.tcgtracker.viewmodels.PokemonCardsViewModelFactory
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality

/**
 * Displays all Pokemon cards.
 *
 * @param navController - The object responsible for navigation between composable screens.
 * @param viewModel - The PokemonCardsViewModel providing the state and actions
 * for this screen.
 */
@Composable
fun AllPokemonCardsScreen(
    navController: NavHostController,
    repository: PokemonCardRepository
) {
    val viewModel: PokemonCardsViewModel = viewModel(
        factory = PokemonCardsViewModelFactory(repository)
    )

    val cardPreviews by viewModel.allPokemonCardPreviews.collectAsState()
    val loadedApiCards by viewModel.loadedApiCards.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val listState = rememberLazyGridState()

    // Lazy-load full cards as they become visible
    LaunchedEffect(cardPreviews, listState.firstVisibleItemIndex) {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        visibleItems.forEach { info ->
            cardPreviews.getOrNull(info.index)?.let { viewModel.fetchFullCard(it.id) }
        }
    }

    Box(Modifier.fillMaxSize()) {
        when {
            loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            error != null -> Text("Error: $error", Modifier.align(Alignment.Center))
            else -> LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cardPreviews) { cardResume ->
                    val fullCard = loadedApiCards[cardResume.id]
                    val imageUrl = fullCard?.imageUrl
                        ?: cardResume.image?.let { cardResume.getImageUrl(Quality.HIGH, Extension.PNG) }
                        ?: ""

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("pokemonCardDetailsScreen/${cardResume.id}")
                            }
                    ) {
                        AsyncImage(
                            model = imageUrl.ifEmpty { null },
                            contentDescription = cardResume.name,
                            placeholder = androidx.compose.ui.res.painterResource(R.drawable.ic_all_cards),
                            error = androidx.compose.ui.res.painterResource(R.drawable.ic_all_cards),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.7f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(cardResume.name)
                    }
                }
            }
        }
    }
}