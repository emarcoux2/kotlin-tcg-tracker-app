package com.example.tcgtracker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tcgtracker.db.PokemonCardRepository

//class PokemonCardsViewModelFactory(
//    private val repository: PokemonCardRepository
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(PokemonCardsViewModel::class.java)) {
//            return PokemonCardsViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}

/**
 * Factory for creating instances of PokemonCardSetsViewModel and PokemonCardsViewModel.
 *
 * @property repository - Repository providing access to Pokemon card data.
 *
 * This factory allows multiple related ViewModels to share the same repository instance.
 *
 * Throws IllegalArgumentException if an unsupported ViewModel class is requested.
 */
class PokemonCardSetsViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PokemonCardSetsViewModel::class.java) ->
                PokemonCardSetsViewModel(repository) as T
            modelClass.isAssignableFrom(PokemonCardsViewModel::class.java) ->
                PokemonCardsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

/**
 * Factory for creating instances of PokemonCardsViewModel associated with a series.
 *
 * @property repository - Repository providing access to Pokemon card data.
 *
 * Throws IllegalArgumentException if an unsupported ViewModel class is requested.
 */
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

/**
 * Factory for creating instances of AddPokemonCardToCollectionViewModel.
 *
 * @property repository - Repository providing access to Pokemon card data.
 *
 * This ViewModel handles the logic for adding cards to the user's collection.
 *
 * Throws IllegalArgumentException if an unsupported ViewModel class is requested.
 */
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

/**
 *
 */
class MyPokemonCardsViewModelFactory(
    private val repository: PokemonCardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyPokemonCardsViewModel::class.java)) {
            return MyPokemonCardsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}