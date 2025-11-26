package com.example.tcgtracker.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tcgtracker.R
import com.example.tcgtracker.components.ScreenLabel

/**
 * Displays all Pokemon card sets.
 *
 * @param navController - The object responsible for navigation between composable screens.
 */
@Composable
fun PokemonCardSetScreen(navController: NavController) {
    val cardSets = listOf(
        R.drawable.ic_all_card_sets,
        R.drawable.ic_all_card_sets,
        R.drawable.ic_all_card_sets,
        R.drawable.ic_all_card_sets,
        R.drawable.ic_all_card_sets,
        R.drawable.ic_all_card_sets,
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cardSets) { setRes ->
            Column(
                modifier = Modifier
                    .clickable { navController.navigate("pokemonCardSetDetailsScreen") }
            ) {
                Image(
                    painter = painterResource(id = setRes),
                    contentDescription = "Card Set",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Text(
                    text = "Placeholder",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }

    ScreenLabel("Pokemon Card Set")
}