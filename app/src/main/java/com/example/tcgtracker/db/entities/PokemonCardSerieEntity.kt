package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Pokémon card serie stored in the local database.
 *
 * @property id - The unique identifier of the serie.
 * @property name - The name of the serie.
 * @property setId - The ID of the set this serie is associated with.
 * @property serieId - The ID of the serie itself (may match `id`).
 * @property logoUrl - The URL of the serie logo.
 */
@Entity(tableName = "pokemonCardSerie")
data class PokemonCardSerieEntity(
    @PrimaryKey val id: String?,
    val name: String?,
    val setId: String?,
    val serieId: String?,
    val logoUrl: String?
)