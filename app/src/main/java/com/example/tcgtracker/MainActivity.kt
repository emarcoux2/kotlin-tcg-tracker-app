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
import com.example.tcgtracker.navigation.AccountBottomNavBar
import com.example.tcgtracker.navigation.AllPokemonCardsBottomNavBar
import com.example.tcgtracker.navigation.FavouritePokemonCardsBottomNavBar
import com.example.tcgtracker.navigation.MyPokemonCardsBottomNavBar
import com.example.tcgtracker.navigation.PokemonCardDetailsBottomNavBar
import com.example.tcgtracker.navigation.PokemonCardSetBottomNavBar
import com.example.tcgtracker.navigation.PokemonCardSetDetailsBottomNavBar
import com.example.tcgtracker.screens.AccountScreen
import com.example.tcgtracker.screens.AllPokemonCardsScreen
import com.example.tcgtracker.screens.FavouritePokemonCardsScreen
import com.example.tcgtracker.screens.MyPokemonCardsScreen
import com.example.tcgtracker.screens.PokemonCardDetailsScreen
import com.example.tcgtracker.screens.PokemonCardSetDetailsScreen
import com.example.tcgtracker.screens.PokemonCardSetScreen
import com.example.tcgtracker.screens.ScanCardsScreen
import com.example.tcgtracker.ui.theme.TCGTrackerTheme

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

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("TCG Tracker") }
                        )
                    },
                    bottomBar = {
                        when (currentDestination) {
                            "accountScreen" -> AccountBottomNavBar(navController)
                            "allPokemonCardsScreen" -> AllPokemonCardsBottomNavBar(navController)
                            "favouritePokemonCardsScreen" -> FavouritePokemonCardsBottomNavBar(navController)
                            "myPokemonCardsScreen" -> MyPokemonCardsBottomNavBar(navController)
                            "pokemonCardDetailsScreen" -> PokemonCardDetailsBottomNavBar(navController)
                            "pokemonCardSetScreen" -> PokemonCardSetBottomNavBar(navController)
                            "pokemonCardSetDetailsScreen" -> PokemonCardSetDetailsBottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "allPokemonCardsScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("accountScreen") { AccountScreen() }
                        composable("allPokemonCardsScreen") { AllPokemonCardsScreen() }
                        composable("favouritePokemonCardsScreen") { FavouritePokemonCardsScreen() }
                        composable("myPokemonCardsScreen") { MyPokemonCardsScreen() }
                        composable("pokemonCardDetailsScreen") { PokemonCardDetailsScreen() }
                        composable("pokemonCardSetScreen") { PokemonCardSetScreen() }
                        composable("pokemonCardSetDetailsScreen") { PokemonCardSetDetailsScreen() }
                        composable("scanCardsScreen") { ScanCardsScreen() }
                    }
                }
            }
        }
    }
}