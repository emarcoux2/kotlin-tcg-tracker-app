package com.example.tcgtracker.api.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardVariants(
    val normal: Boolean? = null,
    val holo: Boolean? = null,
    val reverse: Boolean? = null,
    val firstEdition: Boolean? = null,
    val wPromo: Boolean? = null
)