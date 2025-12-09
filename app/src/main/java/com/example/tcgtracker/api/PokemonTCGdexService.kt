package com.example.tcgtracker.api

import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.Serie
import net.tcgdex.sdk.models.SerieResume
import net.tcgdex.sdk.models.Set
import net.tcgdex.sdk.models.SetResume

/**
 * Provides access to The Pokemon TCGdex API for retrieving cards, sets, and series information.
 *
 * This service will:
 * - Fetch lists of Pokemon cards, series, and sets
 * - Retrieve details for individual cards, series, and sets
 *
 */
class PokemonTCGdexService(
    private val tcgdex: TCGdex = TCGdex("en")
) {
    suspend fun fetchAllCards(): Array<CardResume> = tcgdex.fetchCards() ?: emptyArray()

    suspend fun fetchCardById(cardId: String): Card? = tcgdex.fetchCard(cardId)

    suspend fun fetchAllSets(): Array<SetResume>? = tcgdex.fetchSets() ?: emptyArray()

    suspend fun fetchSetById(setId: String): Set? = tcgdex.fetchSet(setId)

    suspend fun fetchAllSeries(): Array<SerieResume>? = tcgdex.fetchSeries() ?: emptyArray()

    suspend fun fetchSeriesById(seriesId: String): Serie? = tcgdex.fetchSerie(seriesId)
}