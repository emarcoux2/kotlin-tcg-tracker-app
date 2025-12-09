package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonCards")
data class PokemonCardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val setId: String,
    val serieId: String,
    val imageUrl: String
)