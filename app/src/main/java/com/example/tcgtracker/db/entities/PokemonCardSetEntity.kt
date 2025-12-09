package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonCardSet")
data class PokemonCardSetEntity(
    @PrimaryKey val id: String?,
    val name: String?,
    val setId: String?,
    val serieId: String?,
    val logoUrl: String?
)