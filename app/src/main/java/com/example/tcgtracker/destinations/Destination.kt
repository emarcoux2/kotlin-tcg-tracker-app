package com.example.tcgtracker.destinations

open class Destination(val route: String, val label: String) {
    object AllPokemonCardsScreen: Destination("allPokemonCardsScreen", "All Pokemon Cards")
    object FavouritePokemonCardsScreen: Destination("favouritePokemonCardsScreen", "Favourites")
    object ScanCardsScreen: Destination("scanCardsScreen", "Scan Card")
    object PokemonCardDetailsScreen: Destination("pokemonCardDetailsScreen", "Details")
    object PokemonCardSetScreen: Destination("pokemonCardSetScreen", "Pokemon Card Set")
    object PokemonCardSetDetailsScreen: Destination("pokemonCardSetDetailsScreen", "Details")
    object MyPokemonCardsScreen: Destination("myPokemonCardsScreen", "My Pokemon Cards")
}