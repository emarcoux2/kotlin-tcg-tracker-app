package com.example.tcgtracker.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import com.example.tcgtracker.R
import com.example.tcgtracker.destinations.Destination

// navbar when on Favourites screen
@Composable
fun FavouritePokemonCardsBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
    val ic_scan = painterResource(id = R.drawable.ic_scan)

    NavigationBar {
        listOf(
            Destination.MyPokemonCardsScreen to ic_my_cards,
            Destination.ScanCardsScreen to ic_scan,
        ).forEach { (destination, iconPainter) ->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = { navController.navigate(destination.route) },
                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

// navbar when on PokemonCardDetails screen
@Composable
fun CardDetailsBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
    val ic_scan = painterResource(id = R.drawable.ic_scan)
    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)

    NavigationBar {
        listOf(
            Destination.ScanCardsScreen to ic_scan,
            Destination.MyPokemonCardsScreen to ic_my_cards,
            Destination.AllPokemonCardsScreen to ic_all_cards
        ).forEach { (destination, iconPainter) ->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = { navController.navigate(destination.route) },
                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

// navbar when on AllPokemonCards screen
@Composable
fun AllPokemonCardsBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
    val ic_scan = painterResource(id = R.drawable.ic_scan)

    NavigationBar {
        listOf(
            Destination.MyPokemonCardsScreen to ic_my_cards,
            Destination.ScanCardsScreen to ic_scan
        ).forEach { (destination, iconPainter) ->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = { navController.navigate(destination.route) },
                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

// navbar when on PokemonCardSets screen
@Composable
fun PokemonCardSetBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
    val ic_scan = painterResource(id = R.drawable.ic_scan)
    val ic_search = painterResource(id = R.drawable.ic_search)
    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)

    NavigationBar {
        listOf(
            Destination.MyPokemonCardsScreen to ic_my_cards,
            Destination.AllPokemonCardsScreen to ic_all_cards,
            Destination.ScanCardsScreen to ic_scan,
        ).forEach { (destination, iconPainter) ->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = { navController.navigate(destination.route) },
                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}

// navbar when on MyPokemonCards screen (logged in users only)
@Composable
fun MyPokemonCardsBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_scan = painterResource(id = R.drawable.ic_scan)
    val ic_search = painterResource(id = R.drawable.ic_search)
    val ic_favourite = painterResource(id = R.drawable.ic_favourite)

    NavigationBar {
        listOf(
            Destination.FavouritePokemonCardsScreen to ic_favourite,
            Destination.ScanCardsScreen to ic_scan,
        ).forEach { (destination, iconPainter) ->
            NavigationBarItem(
                selected = currentDestination == destination.route,
                onClick = { navController.navigate(destination.route) },
                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}