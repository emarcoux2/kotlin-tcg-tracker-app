package com.example.tcgtracker.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.viewmodels.AddPokemonCardToCollectionViewModel
import com.example.tcgtracker.viewmodels.AddPokemonCardToCollectionViewModelFactory
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import com.example.tcgtracker.R

/**
 * Composable screen for searching and adding Pokemon cards to the user's collection.
 *
 * Responsibilities:
 * Provides a search bar to filter cards by name.
 * Displays search results in a grid with card images, names, and selection checkboxes.
 * Tracks selected cards and allows adding them to the user's collection via the ViewModel.
 * Shows loading and error states during data fetching.
 * Integrates navigation to return to the previous screen after adding cards.
 *
 * @param navController - NavController used to navigate back after adding cards.
 * @param repository - PokemonCardRepository providing access to Pokemon card data for the ViewModel.
 *
 * State observed from AddPokemonCardToCollectionViewModel:
 * searchResults: Filtered list of card previews based on the current query.
 * selected: Set of currently selected card IDs.
 * loading: Indicates if data is being loaded.
 * error: Displays error messages if a search or fetch fails.
 */
@Composable
fun AddPokemonCardsToCollectionScreen(
    navController: NavController,
    repository: PokemonCardRepository
) {
    val vm: AddPokemonCardToCollectionViewModel = viewModel(
        factory = AddPokemonCardToCollectionViewModelFactory(repository)
    )

    val results by vm.searchResults.collectAsState()
    val selected by vm.selected.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    vm.search(it)
                },
                label = { Text("Search by card name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator()
            }

            if (error != null) {
                Text("Error: $error", color = Color.Red)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(results) { card ->
                    Log.d("DEBUG_IMAGE", "id=${card.id}, name=${card.name}, image=${card.image}")
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = card.getImageUrl(Quality.HIGH, Extension.PNG),
                            contentDescription = card.name,
                            placeholder = painterResource(R.drawable.ic_all_cards),
                            error = painterResource(R.drawable.ic_all_cards),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.7f)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = card.name,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            Checkbox(
                                checked = selected.contains(card.id),
                                onCheckedChange = { vm.toggleSelected(card.id) }
                            )
                        }
                    }
                }
            }

            if (selected.isNotEmpty()) {
                Button(
                    onClick = {
                        vm.addSelected()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text("Add (${selected.size}) to My Cards")
                }
            }
        }

        FloatingActionButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(end = 16.dp, bottom = 30.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}