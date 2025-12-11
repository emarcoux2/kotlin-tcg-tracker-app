package com.example.tcgtracker.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Pokémon card owned by the user in the local database.
 *
 * This entity stores information about individual user-owned cards, including
 * references to the corresponding API card, display data, and user-specific flags.
 *
 * @property localId - Auto-generated primary key for local database use.
 * @property cardId - The unique identifier of the card (matches the API card ID).
 * @property name - The name of the Pokémon card.
 * @property imageUrl - URL to the card's image.
 * @property isFavourite - Indicates whether the user has marked this card as a favorite.
 * @property isOwnedByUser - Indicates whether this card is part of the user's collection.
 */
@Entity(tableName = "userPokemonCards")
data class UserPokemonCardEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    val cardId: String,
    val name: String?,
    val imageUrl: String? = null,
    var isFavourite: Boolean = false,
    var isOwnedByUser: Boolean = false
)