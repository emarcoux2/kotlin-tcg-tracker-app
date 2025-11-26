package com.example.tcgtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.tcgtracker.components.navigation.MainBottomNavBar
import com.example.tcgtracker.screens.AllPokemonCardsScreen
import com.example.tcgtracker.screens.PokemonCardDetailsScreen
import com.example.tcgtracker.screens.PokemonCardSetDetailsScreen
import com.example.tcgtracker.screens.PokemonCardSetScreen
import com.example.tcgtracker.screens.ScanCardsScreen
import com.example.tcgtracker.ui.theme.TCGTrackerTheme

/**
 * Displays the bottom navbar on certain screens. The user currently begins on
 * the All Pokemon Cards screen.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCGTrackerTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                val showBottomBar = when (currentDestination) {
                    "allPokemonCardsScreen",
                    "scanCardsScreen",
                    "pokemonCardSetScreen",
                    "pokemonCardDetailsScreen",
                    "pokemonCardSetDetailsScreen" -> true

                    else -> false
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("TCG Tracker") }
                        )
                    },
                    bottomBar = {
                        if (showBottomBar) {
                            MainBottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "allPokemonCardsScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
//                        composable("accountScreen") { AccountScreen() }
                        composable("allPokemonCardsScreen") { AllPokemonCardsScreen(navController) }
//                        composable("favouritePokemonCardsScreen") { FavouritePokemonCardsScreen() }
//                        composable("myPokemonCardsScreen") { MyPokemonCardsScreen() }
                        composable("pokemonCardDetailsScreen") { PokemonCardDetailsScreen(navController) }
                        composable("pokemonCardSetScreen") { PokemonCardSetScreen(navController) }
                        composable("pokemonCardSetDetailsScreen") { PokemonCardSetDetailsScreen(navController) }
                        composable("scanCardsScreen") { ScanCardsScreen(navController) }
                    }
                }
            }
        }
    }
}