package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.db.PokemonCardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.tcgdex.sdk.models.Set as TcgSet
import net.tcgdex.sdk.models.SetResume

/**
 * ViewModel for managing the state and actions of the "Pokemon Card Sets" screen.
 *
 * Responsibilities:
 * Loading and exposing a list of all Pokemon card set previews from the repository.
 * Fetching full details for individual card sets on demand.
 * Exposing loading and error states for UI feedback.
 *
 * @property repository - Repository providing access to both local and remote Pokemon card set data.
 *
 * State flows exposed:
 * allPokemonCardSetPreviews: List of card set previews for display.
 * loadedCardSets: Map of fully loaded card sets keyed by set ID.
 * loading: Current loading state.
 * error: Error messages, if any.
 *
 * Functions:
 * loadPokemonCardSetPreviews: Fetches all card set previews asynchronously.
 * fetchFullCardSet: Loads full details for a specific card set if not already loaded.
 *
 */
class PokemonCardSetsViewModel(
    private val repository: PokemonCardRepository
) : ViewModel() {

    private var _allPokemonCardSetPreviews = MutableStateFlow<List<SetResume>>(emptyList())
    val allPokemonCardSetPreviews: StateFlow<List<SetResume>> = _allPokemonCardSetPreviews

    private val _loadedCardSets = MutableStateFlow<Map<String, TcgSet>>(emptyMap())
    val loadedCardSets: StateFlow<Map<String, TcgSet>> = _loadedCardSets

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadPokemonCardSetPreviews()
    }

    /**
     * Fetches all card set previews asynchronously.
     */
    fun loadPokemonCardSetPreviews() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val cardSetPreviews = repository.fetchAllSets()
                _allPokemonCardSetPreviews.value = cardSetPreviews!!.toList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Loads full details for a specific card set if not already loaded.
     */
    fun fetchFullCardSet(cardSetId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_loadedCardSets.value.containsKey(cardSetId)) {
                try {
                    val fullSet = repository.fetchSetById(cardSetId)
                    fullSet.let { set ->
                        _loadedCardSets.value = (_loadedCardSets.value + (cardSetId to set)) as Map<String, TcgSet>
                    }
                } catch (_: Exception) { }
            }
        }
    }
}