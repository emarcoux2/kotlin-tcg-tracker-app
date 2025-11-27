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
import androidx.wear.compose.navigation.currentBackStackEntryAsState
import com.example.tcgtracker.R
import com.example.tcgtracker.destinations.Destination

/**
 * Represents an item for a Bottom Nav Bar.
 *
 * @property destination - The destination to go to when navigating.
 * @property icon - The Nav Bar icon.
 */
data class BottomNavItem(
    val destination: Destination,
    val icon: Painter
)

/**
 * Displays the main bottom navigation bar on all screens.
 *
 * @param navController - The object responsible for navigation between composable screens.
 */
@Composable
fun MainBottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)
    val ic_scan = painterResource(id = R.drawable.ic_scan)
    val ic_all_card_sets = painterResource(id = R.drawable.ic_all_card_sets)

    val items = listOf(
        BottomNavItem(Destination.AllPokemonCards, ic_all_cards),
        BottomNavItem(Destination.ScanCards, ic_scan),
        BottomNavItem(Destination.PokemonCardSet, ic_all_card_sets)
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
//// navbar when on Favourites screen
//@Composable
//fun FavouritePokemonCardsBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.MyPokemonCards to ic_my_cards,
//            Destination.ScanCards to ic_scan,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//// navbar when on PokemonCardDetails screen
//@Composable
//fun PokemonCardDetailsBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.ScanCards to ic_scan,
//            Destination.MyPokemonCards to ic_my_cards,
//            Destination.AllPokemonCards to ic_all_cards,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//// navbar when on AllPokemonCards screen
//@Composable
//fun AllPokemonCardsBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.MyPokemonCards to ic_my_cards,
//            Destination.ScanCards to ic_scan,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//// navbar when on PokemonCardSets screen
//@Composable
//fun PokemonCardSetBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_card_set_details = painterResource(id = R.drawable.ic_card_set_details)
//    val ic_search = painterResource(id = R.drawable.ic_search)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.MyPokemonCards to ic_all_cards,
//            Destination.ScanCards to ic_scan,
//            Destination.PokemonCardSetDetails to ic_card_set_details,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//// navbar when on MyPokemonCards screen (logged in users only)
//@Composable
//fun MyPokemonCardsBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_search = painterResource(id = R.drawable.ic_search)
//    val ic_favourite = painterResource(id = R.drawable.ic_favourite)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.FavouritePokemonCards to ic_favourite,
//            Destination.ScanCards to ic_scan,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//@Composable
//fun PokemonCardSetDetailsBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_all_cards = painterResource(id = R.drawable.ic_all_cards)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_all_card_sets = painterResource(id = R.drawable.ic_all_card_sets)
//    val ic_account = painterResource(id = R.drawable.ic_account)
//
//    NavigationBar {
//        listOf(
//            Destination.AllPokemonCards to ic_all_cards,
//            Destination.ScanCards to ic_scan,
//            Destination.PokemonCardSet to ic_all_card_sets,
//            Destination.Account to ic_account
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}
//
//@Composable
//fun AccountBottomNavBar(navController: NavController) {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination?.route
//
//    val ic_account = painterResource(id = R.drawable.ic_account)
//    val ic_scan = painterResource(id = R.drawable.ic_scan)
//    val ic_my_cards = painterResource(id = R.drawable.ic_my_cards)
//
//    NavigationBar {
//        listOf(
//            Destination.Account to ic_account,
//            Destination.ScanCards to ic_scan,
//            Destination.MyPokemonCards to ic_my_cards
//        ).forEach { (destination, iconPainter) ->
//            NavigationBarItem(
//                selected = currentDestination == destination.route,
//                onClick = { navController.navigate(destination.route) },
//                icon = { Icon(painter = iconPainter, contentDescription = destination.label) },
//                label = { Text(destination.label) }
//            )
//        }
//    }
//}