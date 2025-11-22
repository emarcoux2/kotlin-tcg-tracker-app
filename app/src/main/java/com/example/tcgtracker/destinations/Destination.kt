package com.example.tcgtracker.destinations

open class Destination(val route: String, val label: String) {
    object AllPokemonCards: Destination("allPokemonCardsScreen", "All Pokemon Cards")
    object FavouritePokemonCards: Destination("favouritePokemonCardsScreen", "Favourites")
    object ScanCards: Destination("scanCardsScreen", "Scan Card")
    object PokemonCardDetails: Destination("pokemonCardDetailsScreen/{cardId}", "Details")
    object PokemonCardSet: Destination("pokemonCardSetScreen", "Pokemon Card Set")
    object PokemonCardSetDetails: Destination("pokemonCardSetDetailsScreen", "Details")
    object MyPokemonCards: Destination("myPokemonCardsScreen", "My Pokemon Cards")
    object Account: Destination("accountScreen", "Account")
}