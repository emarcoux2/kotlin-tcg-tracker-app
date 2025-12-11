package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.CardResume

/**
 * This ViewModel is responsible for managing the state and actions of the
 * All Pokemon Cards screen.
 *
 * This ViewModel will:
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

    fun loadUserCardsCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userCardsList = repository.getUserPokemonCardsCollection()
                _userPokemonCards.value = userCardsList.associateBy { it.cardId }
            } catch (_: Exception) { }
        }
    }

    fun addToUserCardsCollection(cardId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCardToUserCollection(cardId)
            loadUserCardsCollection()
        }
    }

    fun getCardImageUrl(cardId: String, preview: CardResume): String {
        loadedApiCards.value[cardId]?.imageUrl?.let {
            if (it.isNotBlank()) return it
        }

        return preview.image?.let {
            preview.getImageUrl(Quality.HIGH, Extension.PNG)
        } ?: ""
    }
}