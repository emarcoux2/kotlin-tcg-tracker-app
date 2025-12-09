package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Pokémon card set stored in the local database.
 *
 * @property id - The unique identifier of the set.
 * @property name - The name of the set.
 * @property setId - The ID of the set itself (may match `id`).
 * @property serieId - The ID of the series this set belongs to.
 * @property logoUrl - The URL of the set's logo.
 */
@Entity(tableName = "pokemonCardSet")
data class PokemonCardSetEntity(
    @PrimaryKey val id: String?,
    val name: String?,
    val setId: String?,
    val serieId: String?,
    val logoUrl: String?
)