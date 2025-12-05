package com.example.tcgtracker.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.api.PokemonTCGdexService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.tcgdex.sdk.internal.Model
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.SetResume

/**
 * This ViewModel is responsible for managing the state and actions of the
 * Pokemon Card Sets screen.
 *
 * This ViewModel will:
 *
 *
 * Will be implemented in the future.
 */
class PokemonCardSetViewModel(
    private val service: PokemonTCGdexService = PokemonTCGdexService()
) : ViewModel() {
    private var _allPokemonCardSetPreviews = MutableStateFlow<List<SetResume>>(emptyList())
    var allPokemonCardSetPreviews: StateFlow<List<SetResume>> = _allPokemonCardSetPreviews

    private val _loadedCardSets = MutableStateFlow<Map<String, Model>>(emptyMap())
    val loadedCardSets: StateFlow<Map<String, Model>> = _loadedCardSets

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPokemonCardSetPreviews()
    }

    fun loadPokemonCardSetPreviews() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val cardSetPreviews = service.fetchAllSets()
                _allPokemonCardSetPreviews.value = cardSetPreviews!!.toList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchFullCardSet(cardSetId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_loadedCardSets.value.containsKey(cardSetId)) {
                try {
                    val card = service.fetchSetById(cardSetId)
                    card?.let {
                        _loadedCardSets.value = _loadedCardSets.value + (cardSetId to it)
                    }
                } catch (_: Exception) { }
            }
        }
    }
}