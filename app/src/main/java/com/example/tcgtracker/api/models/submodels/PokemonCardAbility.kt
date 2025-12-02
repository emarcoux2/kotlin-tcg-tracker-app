package com.example.tcgtracker.api.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class PokemonCardAbility(
    val name: String,
    val effect: String
)
