package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.api.PokemonTCGdexService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.CardResume

/**
 * This ViewModel is responsible for managing the state and actions of the
 * All Pokemon Cards screen.
 *
 * This ViewModel will:
 * - Handle managing UI state for scanning cards
 * - Coordinate actions triggered by the UI, like saving scanned card data
 *   or fetching related info from the API or local database.
 *
 * Will be implemented in the future.
 */
class PokemonCardsViewModel(
    private val service: PokemonTCGdexService = PokemonTCGdexService()
) : ViewModel() {
    private var _allPokemonCardPreviews = MutableStateFlow<List<CardResume>>(emptyList())
    var allPokemonCardPreviews: StateFlow<List<CardResume>> = _allPokemonCardPreviews

    private val _loadedCards = MutableStateFlow<Map<String, Card>>(emptyMap())
    val loadedCards: StateFlow<Map<String, Card>> = _loadedCards

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPokemonCardPreviews()
    }

    fun loadPokemonCardPreviews() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val cardPreviews = service.fetchAllCards()
                _allPokemonCardPreviews.value = cardPreviews.toList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchFullCard(cardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_loadedCards.value.containsKey(cardId)) {
                try {
                    val card = service.fetchCardById(cardId)
                    card?.let {
                        _loadedCards.value = _loadedCards.value + (cardId to it)
                    }
                } catch (_: Exception) { }
            }
        }
    }
}