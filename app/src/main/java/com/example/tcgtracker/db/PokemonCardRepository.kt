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
     * Loads full details for a specific card from local cache if available;
     * otherwise fetches from API.
     *
     * Saves the card to the local Room database after fetching from the API.
     *
     * @param cardId - The ID of the card to load.
     *
     * @return - The loaded ApiPokemonCardEntity, or null if the API does not return it.
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
     * Loads all card previews from the API and caches them locally in memory.
     * Cards will be immediately fetched if the cache is already populated.
     */
    suspend fun loadAllCards() {
        if (allCardsCache.isEmpty()) {
            allCardsCache = apiService.fetchAllCards().toList()
        }
    }

    // ========= GETS AND FETCHES =========
    /**
     * Fetches all Pokémon card previews from the API.
     * @return - An array of CardResume objects representing all cards.
     */
    suspend fun fetchAllCards(): Array<CardResume> {
        return apiService.fetchAllCards()
    }

    /**
     * Fetches all Pokémon card sets from the API.
     * @return - A list of SetResume objects, or null if none are available.
     */
    suspend fun fetchAllSets(): List<SetResume>? {
        return apiService.fetchAllSets()?.toList()
    }

    /**
     * Fetches full details for a specific card set by its ID.
     *
     * @param cardSetId - The ID of the set to fetch.
     * @return - The [TcgSet] object representing the set, or null if not found.
     */
    suspend fun fetchSetById(cardSetId: String): TcgSet? {
        return apiService.fetchSetById(cardSetId)
    }

    /**
     * Returns a Flow that emits the list of all user-owned cards.
     * This allows composables or ViewModels to observe real-time updates.
     *
     * @return - Flow of List<UserPokemonCardEntity>.
     */
    fun getAllUserCardsFlow(): Flow<List<UserPokemonCardEntity>> {
        return userPokemonCardDao.getAllUserCardsFlow()
    }

    /**
     * Retrieves all user-owned cards as a list.
     *
     * @return - List of UserPokemonCardEntity.
     */
    suspend fun getAllUserCards(): List<UserPokemonCardEntity> {
        return userPokemonCardDao.getAllUserCards()
    }

    /**
     * Retrieves a specific user-owned card by its card ID.
     *
     * @param cardId - The ID of the card.
     * @return - UserPokemonCardEntity if found, otherwise null.
     */
    suspend fun getUserCard(cardId: String): UserPokemonCardEntity? {
        return userPokemonCardDao.getUserCard(cardId)
    }

    /**
     * Retrieves a specific user-owned card by its card ID.
     *
     * @param cardId - The ID of the card.
     * @return - UserPokemonCardEntity if found, otherwise null.
     */
    suspend fun getUserPokemonCard(cardId: String) =
        userPokemonCardDao.getUserCard(cardId)

    /**
     * Returns the user's full collection of cards.
     *
     * @return - List of UserPokemonCardEntity.
     */
    suspend fun getUserPokemonCardsCollection() =
        userPokemonCardDao.getAllUserCards()

    // ======== INSERTS AND UPSERTS ========
    /**
     * Inserts multiple user-owned cards into local storage.
     *
     * @param cards - List of UserPokemonCardEntity to insert.
     */
    suspend fun insertCards(cards: List<UserPokemonCardEntity>) {
        userPokemonCardDao.insertUserCards(cards)
    }

    /**
     * Inserts or updates a user-owned card locally and synchronizes it to Firestore.
     *
     * @param card - The UserPokemonCardEntity to upsert.
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
     * Inserts or updates multiple user-owned cards locally and synchronizes
     * them to Firestore in a batch.
     *
     * @param cards - List of UserPokemonCardEntity to upsert.
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
     * Adds multiple Pokémon cards to the user's collection.
     *
     * Loads each card from the API if needed, inserts them into Room locally,
     * and batches them to Firestore for remote persistence.
     *
     * @param cardIds - List of card IDs to add.
     * @throws IllegalStateException - if any card cannot be found in the API.
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
     * Adds a single Pokémon card to the user's collection.
     *
     * Loads the card from the API if needed, inserts it into Room locally,
     * and persists it to Firestore.
     *
     * @param cardId - The ID of the card to add.
     * @throws IllegalStateException - if the card cannot be found in the API.
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
     * Searches for Pokémon cards by name.
     *
     * Uses the in-memory cache for all card previews and filters by the query string.
     *
     * Returns the full card details from the API for matching cards.
     *
     * @param query - The search string to filter cards by name.
     * @return - List of Card objects matching the query.
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
     * Deletes a user-owned card from both local storage and Firestore.
     *
     * @param card - The UserPokemonCardEntity to delete.
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