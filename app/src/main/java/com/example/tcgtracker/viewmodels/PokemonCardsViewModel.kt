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
     * Loads a list of all Pokemon card previews from the repository asynchronously.
     *
     * Updates _allPokemonCardPreviews with the fetched previews and sets _error]
     * to null on success.
     * Updates _loading state while the operation is in progress.
     *
     * If an exception occurs, sets _error with the exception message.
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
     * Fetches full details for a specific Pokemon card by its ID if not already loaded.
     *
     * Updates _loadedApiCards by adding the loaded card to the map keyed by cardId.
     * Any exceptions during fetching are silently ignored.
     *
     * @param cardId - The ID of the card to fetch details for.
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
     * Loads the current user's Pokemon card collection from the repository.
     *
     * Updates _userPokemonCards with a map of card ID to UserPokemonCardEntity.
     * Any exceptions during fetching are silently ignored.
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
     * Adds a specific card to the user's collection and refreshes the collection.
     *
     * @param cardId - The ID of the card to add.
     *
     * This function calls loadUserCardsCollection after adding to ensure
     * the UI reflects the updated collection.
     */
    fun addToUserCardsCollection(cardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCardToUserCollection(cardId)
            loadUserCardsCollection()
        }
    }

    /**
     * Returns the most appropriate image URL for a card.
     *
     * Prioritizes the fully loaded API card image if available; otherwise falls back to the preview image.
     *
     * Returns an empty string if no image URL is found.
     *
     * @param cardId - The ID of the card.
     * @param preview - The CardResume preview object for fallback image retrieval.
     *
     * @return - The URL of the card image as a String, or an empty string.
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