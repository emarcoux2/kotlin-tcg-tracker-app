package com.example.tcgtracker.destinations

/**
 * Represents the navigation destinations of the app.
 *
 * This open class is meant to define specific screens or destinations for navigation.
 * Each object provides the route and label.
 *
 * @property route - A String used by the NavController to navigate to.
 * @property label - A human-readable name for the destination, useful for UI elements
 * like a bottom nav bar.
 */
open class Destination(val route: String, val label: String) {
    object AllPokemonCards: Destination("allPokemonCardsScreen", "All Pokemon Cards")
    object FavouritePokemonCards: Destination("favouritePokemonCardsScreen", "Favourites")
    object ScanCards: Destination("scanCardsScreen", "Scan Card")
    object PokemonCardDetails: Destination("pokemonCardDetailsScreen/{cardId}", "Details")
    object PokemonCardSet: Destination("pokemonCardSetScreen", "Pokemon Card Set")
    object PokemonCardSetDetails: Destination("pokemonCardSetDetailsScreen/{cardSetId}", "Details")
    object MyPokemonCards: Destination("myPokemonCardsScreen", "My Pokemon Cards")
    object Account: Destination("accountScreen", "Account")
}