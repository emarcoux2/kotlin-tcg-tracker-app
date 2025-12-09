package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Pokémon card stored in the local database.
 *
 * @property id - The unique identifier of the card.
 * @property name - The name of the card.
 * @property setId - The ID of the set this card belongs to.
 * @property serieId - The ID of the series this card belongs to.
 * @property imageUrl - The URL of the card's image.
 */
@Entity(tableName = "pokemonCards")
data class PokemonCardEntity(
    @PrimaryKey val id: String?,
    val name: String?,
    val setId: String?,
    val serieId: String?,
    val imageUrl: String?
)