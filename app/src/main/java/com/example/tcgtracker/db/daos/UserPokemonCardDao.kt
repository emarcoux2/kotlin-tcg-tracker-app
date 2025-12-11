package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing the user's Pokémon card collection
 * in the local Room database.
 *
 * Provides methods to query, insert, update, and delete user-owned cards.
*/
@Dao
interface UserPokemonCardDao {

    /**
     * Returns a Flow that emits the list of all user-owned Pokémon cards.
     * This allows observing changes to the user's collection in real-time.
     *
     * @return - Flow of a list of [UserPokemonCardEntity].
     */
    @Query("SELECT * FROM userPokemonCards")
    fun getAllUserCardsFlow(): Flow<List<UserPokemonCardEntity>>

    /**
     * Retrieves all user-owned Pokémon cards as a one-time suspend function.
     *
     * @return - List of UserPokemonCardEntity.
     */
    @Query("SELECT * FROM userPokemonCards")
    suspend fun getAllUserCards(): List<UserPokemonCardEntity>

    /**
     * Retrieves a specific user-owned card by its API card ID.
     *
     * @param cardId - The ID of the card to fetch.
     * @return - The matching UserPokemonCardEntity or null if not found.
     */
    @Query("SELECT * FROM userPokemonCards WHERE cardId = :cardId LIMIT 1")
    suspend fun getUserCard(cardId: String): UserPokemonCardEntity?

    /**
     * Inserts or updates a single user-owned card in the local database.
     *
     * If a card with the same cardId already exists, it will be replaced.
     *
     * @param card - The UserPokemonCardEntity to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCard(card: UserPokemonCardEntity)

    /**
     * Inserts or updates multiple user-owned cards in the local database.
     *
     * Existing cards with matching cardId values will be replaced.
     *
     * @param cards - List of UserPokemonCardEntity to insert or update.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCards(cards: List<UserPokemonCardEntity>)

    /**
     * Updates the favourite status of a user-owned card.
     *
     * @param card - The UserPokemonCardEntity with updated favourite information.
     */
    @Update
    suspend fun updateFavouriteCard(card: UserPokemonCardEntity)

    /**
     * Deletes a specific user-owned card by its local database ID.
     *
     * @param localId - The primary key of the card to delete.
     */
    @Query("DELETE FROM userPokemonCards WHERE localId = :localId")
    suspend fun deleteUserCard(localId: Long)
}