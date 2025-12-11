package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Pokémon card from the API getting stored in the local database.
 *
 * @property id - The unique identifier of the card.
 * @property name - The name of the card.
 * @property setId - The ID of the set this card belongs to.
 * @property rarity - The rarity of the card.
 * @property imageUrl - The URL of the card's image.
 */
@Entity(tableName = "apiPokemonCards")
data class ApiPokemonCardEntity(
    @PrimaryKey val id: String?,
    val name: String?,
    val setId: String?,
    val setName: String?,
    val setLogo: String?,
    val rarity: String?,
    val types: List<String>?,
    val description: String?,
    val imageUrl: String?,
    var isFavourite: Boolean
)