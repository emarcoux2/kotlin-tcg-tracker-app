package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.api.PokemonTCGdexService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.tcgdex.sdk.models.Serie
import net.tcgdex.sdk.models.SerieResume

/**
 * This ViewModel is responsible for managing the state and actions of the
 * Pokemon Card Series screen.
 *
 */
class PokemonCardSeriesViewModel(
    private val service: PokemonTCGdexService = PokemonTCGdexService()
) : ViewModel() {
    private val _allPokemonCardSeries = MutableStateFlow<List<SerieResume>>(emptyList())
    val allPokemonCardSeries: StateFlow<List<SerieResume>> = _allPokemonCardSeries

    private val _loadedCardSeries = MutableStateFlow<Map<String, Serie>>(emptyMap())
    val loadedCardSeries: StateFlow<Map<String, Serie>> = _loadedCardSeries

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPokemonCardSeries()
    }

    fun loadPokemonCardSeries() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val pokemonCardSeries = service.fetchAllSeries()
                _allPokemonCardSeries.value = pokemonCardSeries!!.toList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchFullSeries(cardSeriesId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_loadedCardSeries.value.containsKey(cardSeriesId)) {
                try {
                    val serie = service.fetchSeriesById(cardSeriesId)
                    serie?.let {
                        _loadedCardSeries.value = _loadedCardSeries.value + (cardSeriesId to it)
                    }
                } catch (_: Exception) {}
            }
        }
    }
}