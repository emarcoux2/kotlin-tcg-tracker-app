package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardSetSeriesPreview(
    val id: String,
    val name: String
)
