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
 *
 */
class MyPokemonCardsViewModel(
    private val repository: PokemonCardRepository
) : ViewModel() {

    val userCards: StateFlow<List<UserPokemonCardEntity>> =
        repository.getAllUserCardsFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /**
     *
     */
    fun toggleFavourite(card: UserPokemonCardEntity) {
        val updatedCard = card.copy(isFavourite = !card.isFavourite)
        viewModelScope.launch {
            repository.upsertUserCard(updatedCard)
        }
    }

    /**
     *
     */
    fun deleteCard(card: UserPokemonCardEntity) {
        viewModelScope.launch {
            repository.deleteUserCard(card)
        }
    }
}