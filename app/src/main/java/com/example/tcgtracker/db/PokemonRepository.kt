package com.example.tcgtracker.db
//
//import com.example.tcgtracker.db.daos.PokemonCardDao
//import com.example.tcgtracker.db.daos.PokemonCardSerieDao
//import com.example.tcgtracker.db.daos.PokemonCardSetDao
//import com.example.tcgtracker.db.entities.PokemonCardEntity
//import com.example.tcgtracker.db.entities.PokemonCardSerieEntity
//import com.example.tcgtracker.db.entities.PokemonCardSetEntity
//import com.google.firebase.Firebase
//import com.google.firebase.firestore.firestore
//import kotlinx.coroutines.tasks.await
//import net.tcgdex.sdk.Extension
//import net.tcgdex.sdk.Quality
//
///**
// *
// */
//class PokemonRepository(
//    private val service: PokemonTCGdexService,
//    private val pokemonCardDao: PokemonCardDao,
//    private val pokemonCardSetDao: PokemonCardSetDao,
//    private val pokemonCardSerieDao: PokemonCardSerieDao
//) {
//
//    private val db = Firebase.firestore
//    private val cardCollection = db.collection("pokemonCards")
//
//    suspend fun getAllCards(): List<PokemonCardEntity> {
//        val local = pokemonCardDao.getAllCards()
//        if (local.isNotEmpty()) return local
//
//        val remote = cardCollection.get().await()
//        if (!remote.isEmpty) {
//            val firestoreCards = remote.toObjects(PokemonCardEntity::class.java)
//            pokemonCardDao.insertAllCards(firestoreCards)
//            return firestoreCards
//        }
//
//        val apiCards = service.fetchAllCards().map { card ->
//            PokemonCardEntity(
//                id = card.id,
//                name = card.name,
//                setId = card.set.id ?: "",
//                serieId = "",
//                imageUrl = card.getImageUrl(Quality.HIGH, Extension.PNG)
//            )
//        }
//
//        pokemonCardDao.insertAllCards(apiCards)
//        apiCards.forEach {
//            cardCollection.document(it.id!!).set(it)
//        }
//
//        return apiCards
//    }
//
//    suspend fun getCardById(cardId: String): PokemonCardEntity? {
//        pokemonCardDao.getCardById(cardId)?.let { return it }
//
//        val fsDoc = cardCollection.document(cardId).get().await()
//        if (fsDoc.exists()) {
//            val entity = fsDoc.toObject(PokemonCardEntity::class.java)
//            if (entity != null) {
//                pokemonCardDao.insertCard(entity)
//                return entity
//            }
//        }
//
//        val card = service.fetchCardById(cardId) ?: return null
//
//        val entity = PokemonCardEntity(
//            id = card.id,
//            name = card.name,
//            setId = card.set.id,
//            serieId = "",
//            imageUrl = card.getImageUrl(Quality.HIGH, Extension.PNG)
//        )
//
//        pokemonCardDao.insertCard(entity)
//        cardCollection.document(cardId).set(entity)
//
//        return entity
//    }
//
//    suspend fun insertOrUpdateCard(card: PokemonCardEntity) {
//        pokemonCardDao.insertCard(card)
//        cardCollection.document(card.id!!).set(card)
//    }
//
//    suspend fun updateCard(card: PokemonCardEntity) {
//        pokemonCardDao.updateCard(card)
//        cardCollection.document(card.id!!).set(card)
//    }
//
//    suspend fun deleteCard(card: PokemonCardEntity) {
//        pokemonCardDao.deleteCard(card)
//        cardCollection.document(card.id!!).delete()
//    }
//
//    suspend fun deleteCardById(id: String) {
//        pokemonCardDao.deleteCardById(id)
//        cardCollection.document(id).delete()
//    }
//
//    suspend fun deleteAllCards() {
//        pokemonCardDao.deleteAll()
//        cardCollection.get().await().documents.forEach { it.reference.delete() }
//    }
//}