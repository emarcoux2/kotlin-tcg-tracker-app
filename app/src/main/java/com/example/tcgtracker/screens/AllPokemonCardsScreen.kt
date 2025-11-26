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
 * Displays all Pokemon cards.
 *
 * @param navController - The object responsible for navigation between composable screens.
 */
@Composable
fun AllPokemonCardsScreen(navController: NavController) {
    val cards = listOf(
        R.drawable.ic_card_details,
        R.drawable.ic_card_details,
        R.drawable.ic_card_details,
        R.drawable.ic_card_details,
        R.drawable.ic_card_details,
        R.drawable.ic_card_details
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cards) { cardRes ->
            Column(
                modifier = Modifier
                    .clickable { navController.navigate("pokemonCardDetailsScreen") }
            ) {
                Image(
                    painter = painterResource(id = cardRes),
                    contentDescription = "Card",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                )
                Text(
                    text = "Placeholder",
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }

    ScreenLabel("All Pokemon Cards")
}