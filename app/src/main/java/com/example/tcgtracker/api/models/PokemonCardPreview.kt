package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardPreview(
    val id: String,
    val localId: String? = null,
    val name: String,
    val image: String? = null,
    val rarity: String? = null,
    val category: String,
    val set: PokemonCardSetPreview
)
