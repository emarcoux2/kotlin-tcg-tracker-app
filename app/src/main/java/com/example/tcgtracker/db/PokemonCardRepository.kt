package com.example.tcgtracker.db

import com.example.tcgtracker.api.PokemonTCGdexService
import com.example.tcgtracker.db.daos.ApiPokemonCardDao
import com.example.tcgtracker.db.daos.UserPokemonCardDao
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.PokemonCardSetEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import net.tcgdex.sdk.Extension
import net.tcgdex.sdk.Quality
import net.tcgdex.sdk.models.Card
import net.tcgdex.sdk.models.CardResume
import net.tcgdex.sdk.models.SetResume
import net.tcgdex.sdk.models.Set as TcgSet

/**
 * Repository responsible for orchestrating Pokémon card data from both local storage (Room)
 * and remote sources (API and Firestore). Acts as the single source of truth for card data
 * and centralizes caching, synchronization, and user-specific collection updates.
 *
 * @property apiPokemonCardDao - DAO for accessing cached API card metadata.
 * @property userPokemonCardDao - DAO for accessing user-owned card records.
 * @property apiService - Remote service for fetching Pokémon card data from the TCGdex API.
 * @property firestore - Firestore instance used to persist user collection data remotely.
 * @property userId - Unique identifier of the authenticated user.
 */
class PokemonCardRepository(
    private val apiPokemonCardDao: ApiPokemonCardDao,
    private val userPokemonCardDao: UserPokemonCardDao,
    private val apiService: PokemonTCGdexService,
    private val firestore: FirebaseFirestore,
    private val userId: String
) {

    private var allCardsCache: List<CardResume> = emptyList()

    // ======== LOADING ========
    /**
     *
     */
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

    /**
     *
     */
    suspend fun loadAllCards() {
        if (allCardsCache.isEmpty()) {
            allCardsCache = apiService.fetchAllCards().toList()
        }
    }

    // ========= GETS AND FETCHES =========
    /**
     *
     */
    suspend fun fetchAllCards(): Array<CardResume> {
        return apiService.fetchAllCards()
    }

    /**
     *
     */
    suspend fun fetchAllSets(): List<SetResume>? {
        return apiService.fetchAllSets()?.toList()
    }

    /**
     *
     */
    suspend fun fetchSetById(cardSetId: String): TcgSet? {
        return apiService.fetchSetById(cardSetId)
    }

    /**
     *
     */
    fun getAllUserCardsFlow(): Flow<List<UserPokemonCardEntity>> {
        return userPokemonCardDao.getAllUserCardsFlow()
    }

    /**
     *
     */
    suspend fun getAllUserCards(): List<UserPokemonCardEntity> {
        return userPokemonCardDao.getAllUserCards()
    }

    /**
     *
     */
    suspend fun getUserCard(cardId: String): UserPokemonCardEntity? {
        return userPokemonCardDao.getUserCard(cardId)
    }

    /**
     *
     */
    suspend fun getUserPokemonCard(cardId: String) =
        userPokemonCardDao.getUserCard(cardId)

    /**
     *
     */
    suspend fun getUserPokemonCardsCollection() =
        userPokemonCardDao.getAllUserCards()

    // ======== INSERTS AND UPSERTS ========
    /**
     *
     */
    suspend fun insertCards(cards: List<UserPokemonCardEntity>) {
        userPokemonCardDao.insertUserCards(cards)
    }

    /**
     *
     */
    suspend fun insertCard(card: UserPokemonCardEntity) {
        userPokemonCardDao.insertUserCard(card)
    }

    /**
     *
     */
    suspend fun upsertUserCard(card: UserPokemonCardEntity) {
        // Insert/update locally
        userPokemonCardDao.insertUserCard(card)

        // Sync to Firestore
        firestore.collection("users")
            .document(userId)
            .collection("cards")
            .document(card.cardId)
            .set(card)
    }

    /**
     *
     */
    suspend fun upsertUserCards(cards: List<UserPokemonCardEntity>) {
        // Insert/update locally
        userPokemonCardDao.insertUserCards(cards)

        // Sync to Firestore
        val batch = firestore.batch()
        val userCardsRef = firestore.collection("users").document(userId).collection("cards")
        cards.forEach { card ->
            val docRef = userCardsRef.document(card.cardId)
            batch.set(docRef, card)
        }
        batch.commit().await()
    }

    // ======== ADDS ========
    /**
     *
     */
    suspend fun addPokemonCardsToUserCollection(cardIds: List<String>) {
        if (cardIds.isEmpty()) return

        val userCards = mutableListOf<UserPokemonCardEntity>()

        for (id in cardIds) {
            val apiCard = loadApiCard(id)
                ?: throw IllegalStateException("Card $id not found in API")

            userCards += UserPokemonCardEntity(
                cardId = id,
                name = apiCard.name,
                isFavourite = false,
                isOwnedByUser = true
            )
        }

        // insert many cards to Room
        userPokemonCardDao.insertUserCards(userCards)

        // batch insert to Firestore
        val batch = firestore.batch()
        val col = firestore.collection("users")
            .document(userId)
            .collection("collection")

        userCards.forEach { card ->
            val doc = col.document(card.cardId)
            batch.set(doc, card)
        }

        batch.commit().await()
    }

    /**
     *
     */
    suspend fun addCardToUserCollection(cardId: String) {
        val apiCard = loadApiCard(cardId)
            ?: throw IllegalStateException("Card not found in the API.")

        val newEntry = UserPokemonCardEntity(
            cardId = cardId,
            name = apiCard.name ?: "",
            imageUrl = apiCard.imageUrl,
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

    // ======= SEARCH ========
    /**
     *
     */
    suspend fun searchCardsByName(query: String): List<Card> {
        loadAllCards()

        if (query.isBlank()) return emptyList()

        val filtered = allCardsCache.filter { it.name.contains(query, ignoreCase = true) }

        return filtered.mapNotNull { resume ->
            try {
                apiService.fetchCardById(resume.id)
            } catch (e: Exception) {
                null
            }
        }
    }

    // ======== DELETE ========
    /**
     *
     */
    suspend fun deleteUserCard(card: UserPokemonCardEntity) {
        // Delete locally
        userPokemonCardDao.deleteUserCard(card.localId)

        // Delete remotely
        firestore.collection("users")
            .document(userId)
            .collection("cards")
            .document(card.cardId)
            .delete()
    }
}