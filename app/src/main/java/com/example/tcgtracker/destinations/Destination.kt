package com.example.tcgtracker.destinations

/**
 * Represents the navigation destinations of the app.
 *
 * This open class is meant to define specific screens or destinations for navigation.
 * Each object provides the route and label.
 *
 * @property route - A String used by the NavController to navigate to.
 * @property label - A human-readable name for the destination, useful for UI elements
 * like a bottom nav bar or navigable menu.
 */
open class Destination(val route: String, val label: String) {
    object SignIn: Destination("signInScreen", "Sign In")
    object Account: Destination("accountScreen", "Account")
    object FavouritePokemonCards: Destination("favouritePokemonCardsScreen", "Favourites")
    object MyPokemonCards: Destination("myPokemonCardsScreen", "My Pokemon Cards")
//    object ScanCards: Destination("scanCardsScreen", "Scan Card")

    object AddPokemonCardsToCollectionScreen: Destination("addPokemonCardsToCollectionScreen", "Add Cards")
    object AllPokemonCards: Destination("allPokemonCardsScreen", "All Pokemon Cards")
    object PokemonCardDetails: Destination("pokemonCardDetailsScreen/{cardId}", "Details")
    object AllPokemonCardSets: Destination("allPokemonCardSetsScreen", "All Card Sets")
    object PokemonCardSetDetails: Destination("pokemonCardSetDetailsScreen/{cardSetId}", "Details")
    object AllPokemonCardSeries: Destination("pokemonCardSeriesScreen", "All Card Series")
    object PokemonCardSeriesDetails: Destination("pokemonCardSeriesDetails", "Details")
    object PokemonCardSetsBySeries: Destination("pokemonCardSetsBySeries/{cardSeriesId}", "All Pokemon Cards")
}