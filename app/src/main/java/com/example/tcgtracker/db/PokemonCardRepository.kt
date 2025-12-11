package com.example.tcgtracker.db

import com.example.tcgtracker.api.PokemonTCGdexService
import com.example.tcgtracker.db.daos.ApiPokemonCardDao
import com.example.tcgtracker.db.daos.UserPokemonCardDao
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import com.google.firebase.firestore.FirebaseFirestore
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.CardResume

/**
 *
 */
class PokemonCardRepository(
    private val apiPokemonCardDao: ApiPokemonCardDao,
    private val userPokemonCardDao: UserPokemonCardDao,
    private val apiService: PokemonTCGdexService,
    private val firestore: FirebaseFirestore,
    private val userId: String
) {
    suspend fun loadApiCard(cardId: String): ApiPokemonCardEntity? {
        apiPokemonCardDao.getCardById(cardId)?.let { return it }

        val remote = apiService.fetchCardById(cardId)

        val entity = ApiPokemonCardEntity(
            id = remote?.id ?: "",
            name = remote?.name ?: "",
            setId = remote?.set?.id ?: "",
            setName = remote?.set?.name ?: "",
            setLogo = remote?.set?.getLogoUrl(Extension.PNG) ?: "",
            description = remote?.description,
            rarity = remote?.rarity,
            imageUrl = remote?.getImageUrl(Quality.HIGH, Extension.PNG),
            isFavourite = false
        )

        apiPokemonCardDao.insertCard(entity)
        return entity
    }

    suspend fun addCardToUserCollection(cardId: String) {
        val apiCard = loadApiCard(cardId)
            ?: throw IllegalStateException("Card not found in the API.")

        val newEntry = UserPokemonCardEntity(
            cardId = cardId,
            name = apiCard.name ?: "",
            isFavourite = false,
            isOwnedByUser = true
        )

        userPokemonCardDao.insertUserCard(newEntry)

        firestore.collection("users")
            .document(userId)
            .collection("collection")
            .document(cardId)
            .set(newEntry)
    }

    suspend fun fetchAllCards(): Array<CardResume> {
        return apiService.fetchAllCards()
    }

    suspend fun getUserPokemonCard(cardId: String) =
        userPokemonCardDao.getUserCard(cardId)

    suspend fun getUserPokemonCardsCollection() =
        userPokemonCardDao.getAllUserCards()

    suspend fun insertCards(cards: List<UserPokemonCardEntity>) {
        userPokemonCardDao.insertUserCards(cards)
    }

    suspend fun insertCard(card: UserPokemonCardEntity) {
        userPokemonCardDao.insertUserCard(card)
    }
}