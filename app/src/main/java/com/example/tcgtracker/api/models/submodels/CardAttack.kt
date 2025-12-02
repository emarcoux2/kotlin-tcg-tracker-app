package com.example.tcgtracker.api.models.submodels

import kotlinx.serialization.Serializable

@Serializable
data class CardAttack(
    val cost: List<String> = emptyList(),
    val name: String,
    val damage: String? = null,
    val effect: String? = null
)