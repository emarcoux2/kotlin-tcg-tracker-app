package com.example.tcgtracker.api

import net.tcgdex.sdk.TCGdex
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.Serie
import net.tcgdex.sdk.models.SerieResume
import net.tcgdex.sdk.models.Set
import net.tcgdex.sdk.models.SetResume

/**
 * Service class for accessing the Pokemon TCGdex API.
 *
 * Provides methods to fetch lists and details of Pokemon cards, card sets, and series.
 * All network operations are suspend functions and should be called from a
 * coroutine or another suspend context.
 *
 * @property tcgdex - The underlying TCGdex API client. Defaults to English ("en") localization.
 */
class PokemonTCGdexService(
    private val tcgdex: TCGdex = TCGdex("en")
) {
    /**
     * Fetches all Pokemon cards from the API.
     *
     * @return - An array of CardResume representing all available cards.
     * Returns an empty array if no cards are found.
     */
    suspend fun fetchAllCards(): Array<CardResume> = tcgdex.fetchCards() ?: emptyArray()

    /**
     * Fetches a single Pokemon card by its unique ID.
     *
     * @param cardId - The unique identifier of the card to fetch.
     * @return - A Card object if found, or `null` if no card matches the given ID.
     */
    suspend fun fetchCardById(cardId: String): Card? = tcgdex.fetchCard(cardId)

    /**
     * Fetches all card sets from the API.
     *
     * @return - An array of SetResume representing all available card sets.
     * Returns an empty array if no sets are found.
     */
    suspend fun fetchAllSets(): Array<SetResume>? = tcgdex.fetchSets() ?: emptyArray()

    /**
     * Fetches a single card set by its unique ID.
     *
     * @param setId - The unique identifier of the set to fetch.
     * @return - A Set object if found, or `null` if no set matches the given ID.
     */
    suspend fun fetchSetById(setId: String): Set? = tcgdex.fetchSet(setId)

    /**
     * Fetches all series from the API.
     *
     * @return - An array of SerieResume representing all available series.
     * Returns an empty array if no series are found.
     */
    suspend fun fetchAllSeries(): Array<SerieResume>? = tcgdex.fetchSeries() ?: emptyArray()

    /**
     * Fetches a single series by its unique ID.
     *
     * @param seriesId - The unique identifier of the series to fetch.
     * @return - A Serie object if found, or `null` if no series matches the given ID.
     */
    suspend fun fetchSeriesById(seriesId: String): Serie? = tcgdex.fetchSerie(seriesId)
}