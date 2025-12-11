package com.example.tcgtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.tcgtracker.api.PokemonTCGdexService
import com.example.tcgtracker.components.navigation.AccountBottomNavBar
import com.example.tcgtracker.components.navigation.KebabMenu
import com.example.tcgtracker.components.navigation.MainBottomNavBar
import com.example.tcgtracker.components.navigation.PokemonCardSetsBottomNavBar
import com.example.tcgtracker.db.AppDatabase
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.ui.screens.AccountScreen
import com.example.tcgtracker.ui.screens.AddPokemonCardsToCollectionScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardSeriesScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardsScreen
import com.example.tcgtracker.ui.screens.FavouritePokemonCardsScreen
import com.example.tcgtracker.ui.screens.MyPokemonCardsScreen
import com.example.tcgtracker.ui.screens.PokemonCardDetailsScreen
import com.example.tcgtracker.ui.screens.PokemonCardSetDetailsScreen
import com.example.tcgtracker.ui.screens.AllPokemonCardSetsScreen
import com.example.tcgtracker.ui.screens.PokemonCardSetsBySeriesScreen
import com.example.tcgtracker.ui.screens.SignInScreen
import com.example.tcgtracker.ui.theme.TCGTrackerTheme
import com.example.tcgtracker.viewmodels.MyPokemonCardsViewModel
import com.example.tcgtracker.viewmodels.MyPokemonCardsViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

/**
 * Main entry point of the TCG Tracker app.
 *
 * This activity is responsible for:
 * Initializing the Compose UI with edge-to-edge support.
 * Setting up the app theme, scaffold, top app bar, and dynamic bottom navigation bars.
 * Managing Firebase Authentication state and loading the current user.
 * Initializing the PokemonCardRepository with local and remote data sources for
 *      the signed-in user.
 * Hosting the navigation graph for the app's screens, including:
 * Pokemon card lists, details, sets, and series.
 * User account screens such as favourites and personal collection.
 * Sign-in flow for unauthenticated users.
 * The bottom navigation bars are conditionally displayed based on the current
 *      navigation destination:
 * MainBottomNavBar for primary card browsing screens.
 * PokemonCardSetsBottomNavBar for card sets filtered by series.
 * AccountBottomNavBar for account-related screens.
 *
 * Firebase Authentication and repository initialization are handled asynchronously using LaunchedEffect.
 *
 * Screens are composed via NavHost, and arguments are safely extracted from back stack entries.
 *
 * Uses Material3 components and supports coroutines for snackbars and async operations.
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

                // FirebaseAuth state
                var firebaseUser by remember { mutableStateOf<FirebaseUser?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var repository by remember { mutableStateOf<PokemonCardRepository?>(null) }

                // Load FirebaseAuth user async
                LaunchedEffect(Unit) {
                    firebaseUser = FirebaseAuth.getInstance().currentUser

                    if (firebaseUser != null) {
                        val firestoreDb = Firebase.firestore
                        val localDb = AppDatabase.getInstance(applicationContext)

                        repository = PokemonCardRepository(
                            apiPokemonCardDao = localDb.apiPokemonCardDao(),
                            userPokemonCardDao = localDb.userPokemonCardDao(),
                            apiService = PokemonTCGdexService(),
                            firestore = firestoreDb,
                            userId = firebaseUser!!.uid
                        )
                    }

                    isLoading = false
                }

                val showMainBottomNavBar = currentDestination in listOf(
                    "allPokemonCardsScreen",
                    "pokemonCardSeriesScreen",
                    "allPokemonCardSetsScreen",
                    "pokemonCardDetailsScreen",
                    "pokemonCardSetDetailsScreen",
                    "addPokemonCardToCollectionScreen"
                )

                val showPokemonCardSetsBottomNavBar =
                    currentDestination?.startsWith("pokemonCardSetsBySeriesScreen") == true

                val showAccountBottomNavBar = currentDestination in listOf(
                    "accountScreen",
                    "favouritePokemonCardsScreen",
                    "myPokemonCardsScreen"
                )

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
                        if (showMainBottomNavBar) MainBottomNavBar(navController)
                        if (showAccountBottomNavBar) AccountBottomNavBar(navController)
                        if (showPokemonCardSetsBottomNavBar) PokemonCardSetsBottomNavBar(
                            navController
                        )
                    }
                ) { innerPadding ->
                    if (firebaseUser != null && repository != null) {
                        NavHost(
                            navController = navController,
                            startDestination = "allPokemonCardsScreen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // All Cards Screen
                            composable("allPokemonCardsScreen") {
                                AllPokemonCardsScreen(navController, repository!!)
                            }

                            // Card Details Screen
                            composable("pokemonCardDetailsScreen/{cardId}") { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                                PokemonCardDetailsScreen(navController, cardId, repository!!)
                            }

                            // Sets & Series
                            composable("pokemonCardSeriesScreen") { AllPokemonCardSeriesScreen(navController) }
                            composable("allPokemonCardSetsScreen") { AllPokemonCardSetsScreen(navController) }
                            composable("pokemonCardSetsBySeriesScreen/{seriesId}") { backStackEntry ->
                                val seriesId = backStackEntry.arguments?.getString("seriesId")
                                PokemonCardSetsBySeriesScreen(navController, cardSeriesId = seriesId)
                            }
                            composable("pokemonCardSetDetailsScreen/{setId}") { backStackEntry ->
                                val setId = backStackEntry.arguments?.getString("setId") ?: return@composable
                                PokemonCardSetDetailsScreen(navController, setId, repository!!)
                            }

                            composable("addPokemonCardsToCollectionScreen") {
                                AddPokemonCardsToCollectionScreen(navController, repository!!)
                            }

                            // Account Screens
                            composable("accountScreen") { AccountScreen() }
                            composable("favouritePokemonCardsScreen") { FavouritePokemonCardsScreen() }
                            composable("myPokemonCardsScreen") {
                                val viewModel: MyPokemonCardsViewModel = viewModel(
                                    factory = MyPokemonCardsViewModelFactory(repository!!)
                                )
                                MyPokemonCardsScreen(viewModel)
                            }
                        }
                    } else {
                        // Not signed in: show sign-in
                        NavHost(
                            navController = navController,
                            startDestination = "signInScreen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("signInScreen") {
                                SignInScreen(
                                    onSignInSuccess = {
                                        navController.navigate("allPokemonCardsScreen") {
                                            popUpTo("signInScreen") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}