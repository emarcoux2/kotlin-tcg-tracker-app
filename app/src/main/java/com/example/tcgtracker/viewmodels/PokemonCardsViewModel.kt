package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.CardResume

/**
 * ViewModel for managing the state and actions of the "All Pokemon Cards" screen.
 *
 * Responsibilities:
 * Loading and exposing a list of all Pokemon card previews from the repository.
 * Fetching full card details for individual cards on demand.
 * Loading and managing the user's personal Pokemon card collection.
 * Adding cards to the user's collection.
 * Exposing loading and error states for UI feedback.
 *
 * @property repository - Repository providing access to both local and remote Pokemon card data.
 *
 * State flows exposed:
 * allPokemonCardPreviews: List of card previews for display.
 * loadedApiCards: Map of fully loaded API card entities keyed by card ID.
 * userPokemonCards: Map of user's owned card entities keyed by card ID.
 * loading: Current loading state.
 * error: Error messages, if any.
 *
 * Functions:
 * loadCardPreviews: Fetches all card previews asynchronously.
 * fetchFullCard: Loads full details for a specific card if not already loaded.
 * loadUserCardsCollection: Fetches the user's Pokemon card collection.
 * addToUserCardsCollection: Adds a card to the user's collection and refreshes the collection.
 * getCardImageUrl: Returns the best available image URL for a card, prioritizing full API data.
 *
 */
class PokemonCardsViewModel(
    private val repository: PokemonCardRepository
) : ViewModel() {

    private val _allPokemonCardPreviews = MutableStateFlow<List<CardResume>>(emptyList())
    val allPokemonCardPreviews: StateFlow<List<CardResume>> = _allPokemonCardPreviews

    private val _loadedApiCards = MutableStateFlow<Map<String, ApiPokemonCardEntity>>(emptyMap())
    val loadedApiCards: StateFlow<Map<String, ApiPokemonCardEntity>> = _loadedApiCards

    private val _userPokemonCards = MutableStateFlow<Map<String, UserPokemonCardEntity>>(emptyMap())
    val userPokemonCards: StateFlow<Map<String, UserPokemonCardEntity>> = _userPokemonCards

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadCardPreviews()
        loadUserCardsCollection()
    }

    /**
     * Fetches all card previews asynchronously.
     */
    fun loadCardPreviews() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            try {
                val previews = repository.fetchAllCards()
                _allPokemonCardPreviews.value = previews.toList()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Loads full details for a specific card if not already loaded.
     */
    fun fetchFullCard(cardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!_loadedApiCards.value.containsKey(cardId)) {
                try {
                    val card = repository.loadApiCard(cardId)
                    card?.let {
                        _loadedApiCards.update { old -> old + (cardId to it) }
                    }
                } catch (_: Exception) { }
            }
        }
    }

    /**
     * Fetches the user's Pokemon card collection.
     */
    fun loadUserCardsCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userCardsList = repository.getUserPokemonCardsCollection()
                _userPokemonCards.value = userCardsList.associateBy { it.cardId }
            } catch (_: Exception) { }
        }
    }

    /**
     * Adds a card to the user's collection and refreshes the collection.
     */
    fun addToUserCardsCollection(cardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCardToUserCollection(cardId)
            loadUserCardsCollection()
        }
    }

    /**
     * Returns the best available image URL for a card, prioritizing full API data.
     */
    fun getCardImageUrl(cardId: String, preview: CardResume): String {
        loadedApiCards.value[cardId]?.imageUrl?.let {
            if (it.isNotBlank()) return it
        }

        return preview.image?.let {
            preview.getImageUrl(Quality.HIGH, Extension.PNG)
        } ?: ""
    }
}