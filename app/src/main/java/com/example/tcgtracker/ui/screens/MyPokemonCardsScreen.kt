package com.example.tcgtracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tcgtracker.components.ScreenLabel
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import com.example.tcgtracker.viewmodels.MyPokemonCardsViewModel
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModel
import com.example.tcgtracker.viewmodels.PokemonCardSetsViewModelFactory
import com.example.tcgtracker.viewmodels.PokemonCardsViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import net.tcgdex.sdk.models.subs.CardItem

/**
 * Displays a logged-in user's personal collection of Pokemon cards.
 */
@Composable
fun MyPokemonCardsScreen(viewModel: MyPokemonCardsViewModel) {
    val userCards by viewModel.userCards.collectAsState()

    Column {
        ScreenLabel("My Cards")

        if (userCards.isEmpty()) {
            Text("No cards added yet")
        } else {
            LazyColumn {
                items(userCards) { card ->
                    CardItem(
                        card = card,
                        onToggleFavourite = { viewModel.toggleFavourite(card) },
                        onDelete = { viewModel.deleteCard(card) }
                    )
                }
            }
        }
    }
}

@Composable
fun CardItem(
    card: UserPokemonCardEntity,
    onToggleFavourite: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                // Card Image
                AsyncImage(
                    model = card.imageUrl,
                    contentDescription = card.name,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 16.dp)
                )

                Column {
                    Text(text = card.name ?: "Unknown Card", style = MaterialTheme.typography.titleMedium)
                    Text(text = "ID: ${card.cardId}", style = MaterialTheme.typography.bodySmall)
                }
            }

            // Buttons
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onToggleFavourite) {
                    Icon(
                        imageVector = if (card.isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Toggle Favourite"
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Card"
                    )
                }
            }
        }
    }
}