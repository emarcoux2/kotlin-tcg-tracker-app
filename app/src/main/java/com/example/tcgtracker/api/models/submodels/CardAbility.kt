package com.example.tcgtracker.api.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class CardAbility(
    val name: String,
    val effect: String
)
