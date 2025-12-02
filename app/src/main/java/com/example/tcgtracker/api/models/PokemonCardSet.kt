package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable
import net.tcgdex.sdk.models.CardResume

@Serializable
data class PokemonCardSet(
    val id: String,
    val name: String,
    val series: String? = null,
    val releaseDate: String? = null,
    val printedTotal: Int? = null,
    val cards: List<CardResume> = emptyList()
)
