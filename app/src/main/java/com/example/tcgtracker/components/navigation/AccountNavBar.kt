package com.example.tcgtracker.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tcgtracker.R
import com.example.tcgtracker.destinations.Destination

data class AccountNavItem(
    val destination: Destination,
    val icon: Painter
)

@Composable
fun AccountBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_favourite = painterResource(id = R.drawable.ic_favourite)
    val ic_scan = painterResource(id = R.drawable.ic_scan)
    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)

    val items = listOf(
        AccountNavItem(Destination.FavouritePokemonCards, ic_favourite),
        AccountNavItem(Destination.ScanCards, ic_scan),
        AccountNavItem(Destination.MyPokemonCards, ic_my_cards)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.destination.route,
                onClick = {
                    navController.navigate(item.destination.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.destination.label
                    )
                },
                label = { Text(item.destination.label) }
            )
        }
    }
}