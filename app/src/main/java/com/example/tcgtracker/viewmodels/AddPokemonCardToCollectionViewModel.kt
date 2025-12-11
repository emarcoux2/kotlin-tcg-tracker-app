package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.db.PokemonCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.tcgdex.sdk.models.CardResume

/**
 *
 */
class AddPokemonCardToCollectionViewModel(
    private val repository: PokemonCardRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<CardResume>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _selected = MutableStateFlow<Set<String>>(emptySet())
    val selected = _selected.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /**
     *
     */
    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.value = true
                val all = repository.fetchAllCards()     // Array<CardResume>
                val filtered = all.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                _searchResults.value = filtered
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     *
     */
    fun toggleSelected(id: String) {
        _selected.update { current ->
            if (id in current) current - id else current + id
        }
    }

    /**
     *
     */
    fun addSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ids = _selected.value.toList()
                repository.addPokemonCardsToUserCollection(ids)
                _selected.value = emptySet()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}