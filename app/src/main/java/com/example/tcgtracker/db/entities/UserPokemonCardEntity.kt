package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userPokemonCards")
data class UserPokemonCardEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val cardId: String,
    val name: String,
    var isFavourite: Boolean = false,
    var isOwnedByUser: Boolean = false
)