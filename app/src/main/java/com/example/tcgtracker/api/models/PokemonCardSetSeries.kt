package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardSetSeries(
    val id: String,
    val name: String,
    val sets: List<PokemonCardSetPreview> = emptyList()
)
