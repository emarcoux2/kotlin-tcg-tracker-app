package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tcgtracker.db.PokemonCardRepository

class PokemonCardsViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonCardsViewModel::class.java)) {
            return PokemonCardsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PokemonCardSetsViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonCardsViewModel::class.java)) {
            return PokemonCardsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PokemonCardSeriesViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonCardsViewModel::class.java)) {
            return PokemonCardsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class AddPokemonCardToCollectionViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPokemonCardToCollectionViewModel::class.java)) {
            return AddPokemonCardToCollectionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}