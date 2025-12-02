package com.example.tcgtracker.api.models

import kotlinx.serialization.Serializable
import net.tcgdex.sdk.models.SetResume
import net.tcgdex.sdk.models.subs.CardAbility
import net.tcgdex.sdk.models.subs.CardAttack
import net.tcgdex.sdk.models.subs.CardVariants

@Serializable
data class PokemonCard(
    val id: String,
    val localId: String,
    val name: String,
    val image: String? = null,
    val rarity: String,
    val category: String,
    val variants: CardVariants? = null,
    val set: SetResume,
    val types: List<String>? = null,
    val description: String? = null,
    val suffix: String? = null,
    val abilities: List<CardAbility> = emptyList(),
    val attacks: List<CardAttack> = emptyList(),
    val effect: String? = null,
    val trainerType: String? = null,
    val energyType: String
)
