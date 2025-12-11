package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgtracker.db.PokemonCardRepository
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for managing the user's personal Pokemon card collection.
 *
 * Responsibilities:
 * Exposing the user's collection as a reactive StateFlow.
 * Toggling the favourite status of a card.
 * Deleting a card from the user's collection.
 *
 * @property repository - Repository providing access to the user's Pokemon card data.
 *
 * State flows exposed:
 * userCards: List of the user's Pokemon cards, updated in real time.
 *
 * Functions:
 * toggleFavourite: Flips the isFavourite status of a given card and updates the repository.
 * deleteCard: Removes a given card from the user's collection in the repository.
 *
 */
class MyPokemonCardsViewModel(
    private val repository: PokemonCardRepository
) : ViewModel() {

    val userCards: StateFlow<List<UserPokemonCardEntity>> =
        repository.getAllUserCardsFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     * Toggles the isFavourite status of the specified user card and updates it in the repository.
     *
     * The card's isFavourite property is flipped, and the updated card is saved asynchronously.
     *
     * @param card - The UserPokemonCardEntity whose favourite status will be toggled.
     */
    fun toggleFavourite(card: UserPokemonCardEntity) {
        val updatedCard = card.copy(isFavourite = !card.isFavourite)
        viewModelScope.launch {
            repository.upsertUserCard(updatedCard)
        }
    }

    /**
     * Deletes the specified card from the user's collection in the repository.
     *
     * The removal is performed asynchronously and updates the underlying data source.
     *
     * @param card - The UserPokemonCardEntity to be removed from the collection.
     */
    fun deleteCard(card: UserPokemonCardEntity) {
        viewModelScope.launch {
            repository.deleteUserCard(card)
        }
    }
}