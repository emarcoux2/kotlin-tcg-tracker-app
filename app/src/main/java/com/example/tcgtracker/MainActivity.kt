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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tcgtracker.api.PokemonTCGdexService
import com.example.tcgtracker.components.navigation.AccountBottomNavBar
import com.example.tcgtracker.components.navigation.KebabMenu
import com.example.tcgtracker.components.navigation.MainBottomNavBar
import com.example.tcgtracker.components.navigation.PokemonCardSetsBottomNavBar
import com.example.tcgtracker.db.AppDatabase
import com.example.tcgtracker.db.PokemonCardRepository
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
import com.example.tcgtracker.ui.screens.SignInScreen
//import com.example.tcgtracker.ui.screens.ScanCardsScreen
import com.example.tcgtracker.ui.theme.TCGTrackerTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
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

                // FirebaseAuth state
                var firebaseUser by remember { mutableStateOf<FirebaseUser?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                var repository by remember { mutableStateOf<PokemonCardRepository?>(null) }

                // Load FirebaseAuth user async
                LaunchedEffect(Unit) {
                    firebaseUser = FirebaseAuth.getInstance().currentUser

                    // Initialize repository only after we know the user
                    val firestoreDb = Firebase.firestore
                    val localDb = AppDatabase.getInstance(applicationContext)
                    repository = PokemonCardRepository(
                        apiPokemonCardDao = localDb.apiPokemonCardDao(),
                        userPokemonCardDao = localDb.userPokemonCardDao(),
                        apiService = PokemonTCGdexService(),
                        firestore = firestoreDb,
                        userId = firebaseUser?.uid ?: ""
                    )

                    isLoading = false
                }

                val showMainBottomNavBar = when (currentDestination) {
                    "allPokemonCardsScreen",
                    "pokemonCardSeriesScreen",
                    "allPokemonCardSetsScreen",
                    "pokemonCardDetailsScreen",
                    "pokemonCardSetDetailsScreen",
                    "addPokemonCardToCollectionScreen" -> true
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
                        if (showMainBottomNavBar) MainBottomNavBar(navController)
                        if (showAccountBottomNavBar) AccountBottomNavBar(navController)
                        if (showPokemonCardSetsBottomNavBar) PokemonCardSetsBottomNavBar(navController)
                    }
                ) { innerPadding ->

                    if (isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = if (firebaseUser != null) "allPokemonCardsScreen" else "signInScreen",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // SignIn Screen
                            composable("signInScreen") {
                                SignInScreen(
                                    onSignInSuccess = {
                                        // Update firebaseUser state so NavHost re-composes with correct startDestination
                                        firebaseUser = FirebaseAuth.getInstance().currentUser
                                        // Navigate safely
                                        navController.navigate("allPokemonCardsScreen") {
                                            popUpTo("signInScreen") { inclusive = true }
                                        }
                                    }
                                )
                            }

                            // Main App Screens (always declared)
                            composable("allPokemonCardsScreen") {
                                repository?.let {
                                    AllPokemonCardsScreen(
                                        navController = navController,
                                        repository = it
                                    )
                                }
                            }

                            composable("pokemonCardDetailsScreen/{cardId}") { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                                repository?.let {
                                    PokemonCardDetailsScreen(
                                        navController = navController,
                                        cardId = cardId,
                                        repository = it
                                    )
                                }
                            }

                            composable("pokemonCardSeriesScreen") { AllPokemonCardSeriesScreen(navController) }
                            composable("allPokemonCardSetsScreen") { AllPokemonCardSetsScreen(navController) }
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

                            // Account screens
                            composable("accountScreen") { AccountScreen() }
                            composable("favouritePokemonCardsScreen") { FavouritePokemonCardsScreen() }
                            composable("myPokemonCardsScreen") { MyPokemonCardsScreen() }
                        }
                    }
                }
            }
        }
    }
}