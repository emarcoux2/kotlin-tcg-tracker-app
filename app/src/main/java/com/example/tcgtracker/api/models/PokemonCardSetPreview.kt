package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardSetPreview(
    val id: String,
    val name: String,
    val series: String? = null,
    val releaseDate: String? = null,
    val printedTotal: Int? = null
)
