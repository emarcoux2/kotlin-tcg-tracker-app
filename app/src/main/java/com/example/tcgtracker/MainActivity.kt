package com.example.tcgtracker

import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tcgtracker.components.navigation.AccountBottomNavBar
import com.example.tcgtracker.components.navigation.KebabMenu
import com.example.tcgtracker.components.navigation.MainBottomNavBar
import com.example.tcgtracker.components.navigation.PokemonCardSetsBottomNavBar
import com.example.tcgtracker.ui.screens.AccountScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardSeriesScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardsScreen
import com.example.tcgtracker.ui.screens.FavouritePokemonCardsScreen
import com.example.tcgtracker.ui.screens.MyPokemonCardsScreen
import com.example.tcgtracker.ui.screens.PokemonCardDetailsScreen
import com.example.tcgtracker.ui.screens.PokemonCardSetDetailsScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardSetsScreen
import com.example.tcgtracker.ui.screens.PokemonCardSeriesDetailsScreen
import com.example.tcgtracker.ui.screens.PokemonCardSetsBySeriesScreen
import com.example.tcgtracker.ui.screens.ScanCardsScreen
import com.example.tcgtracker.ui.theme.TCGTrackerTheme
import kotlinx.coroutines.launch

/**
 * Displays the bottom navbar on certain screens. The guest user currently begins on
 * the All Pokemon Cards screen, while logged-in users begin on the Sign In screen.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TCGTrackerTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination?.route

                val showMainBottomNavBar = when (currentDestination) {
                    "allPokemonCardsScreen",
                    "pokemonCardSeriesScreen",
                    "allPokemonCardSetsScreen",
                    "pokemonCardDetailsScreen",
                    "pokemonCardSetDetailsScreen" -> true

                    else -> false
                }

                val showPokemonCardSetsBottomNavBar =
                    currentDestination?.startsWith("pokemonCardSetsBySeriesScreen") == true

                val showAccountBottomNavBar = when (currentDestination) {
                    "accountScreen",
                    "favouritePokemonCardsScreen",
                    "myPokemonCardsScreen" -> true

                    else -> false
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text("TCG Tracker") },
                            actions = {
                                KebabMenu(
                                    navController = navController,
                                    onShowMessage = { message ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(message)
                                        }
                                    }
                                )
                            }
                        )
                    },
                    bottomBar = {
                        if (showMainBottomNavBar) {
                            MainBottomNavBar(navController)
                        }

                        if (showAccountBottomNavBar) {
                            AccountBottomNavBar(navController)
                        }

                        if (showPokemonCardSetsBottomNavBar) {
                            PokemonCardSetsBottomNavBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "allPokemonCardsScreen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("accountScreen") { AccountScreen() }
                        composable("favouritePokemonCardsScreen") { FavouritePokemonCardsScreen() }
                        composable("myPokemonCardsScreen") { MyPokemonCardsScreen() }
                        composable("scanCardsScreen") { ScanCardsScreen(navController) }
                        composable("allPokemonCardsScreen") { AllPokemonCardsScreen(navController) }
                        composable("pokemonCardDetailsScreen/{cardId}") { backStackEntry ->
                            val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                            PokemonCardDetailsScreen(navController = navController, cardId = cardId)
                        }
                        composable("pokemonCardSeriesScreen") { AllPokemonCardSeriesScreen(navController) }
//                        composable("seriesDetails/{id}") { backStackEntry ->
//                            val id = backStackEntry.arguments?.getString("id")!!
//                            PokemonCardSeriesDetailsScreen(id = id)
//                        }
                        composable("allPokemonCardSetsScreen") {
                            AllPokemonCardSetsScreen(navController)
                        }
                        composable("pokemonCardSetsBySeriesScreen/{seriesId}") { backStackEntry ->
                            val seriesId = backStackEntry.arguments?.getString("seriesId")
                            PokemonCardSetsBySeriesScreen(navController, cardSeriesId = seriesId)
                        }
                        composable(
                            "pokemonCardSetDetailsScreen/{setId}",
                            arguments = listOf(navArgument("setId") { type = NavType.StringType })
                        ) {
                            val setId = it.arguments?.getString("setId")!!
                            PokemonCardSetDetailsScreen(navController, setId)
                        }
                    }
                }
            }
        }
    }
}