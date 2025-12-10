package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.PokemonCardEntity

/**
 * Data Access Object (DAO) for the PokemonCardEntity table.
 *
 * This interface defines CRUD for managing a user's Pokemon Cards in
 * the local Room database.
 * DAOs are the main interfaces used to interact with database data.
 */
@Dao
interface PokemonCardDao {

    /**
     * Inserts a new card into the database.
     *
     * If the card already exists, the existing record will be replaced
     * with the new data.
     *
     * @param card - The PokemonCardEntity object that will be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: PokemonCardEntity)

    /**
     * Retrieves all cards stored in the database.
     *
     * Returns a full list of every PokemonCardEntity currently saved
     * in the pokemonCards table.
     *
     * @return A list of all the card entities.
     */
    @Query("SELECT * FROM pokemonCards")
    suspend fun getAllCards(): List<PokemonCardEntity>

    /**
     * Retrieves a card from the database based on the card's unique ID.
     *
     * Room will search the pokemonCards table for a row that contains the
     * primary key to match with the provided cardId. It will return the
     * corresponding entity if it's found, otherwise, null is returned.
     *
     * @param cardId - String - The ID of the card to search for
     * @return The matching card entity, or null if no card exists with that ID
     */
    @Query("SELECT * FROM pokemonCards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): PokemonCardEntity?

    /**
     * Updates the Favourite state of a specific card.
     *
     * This only modifies the isFavourite field of the card with the
     * matching primary key. Room will update this record while leaving
     * the other fields unchanged.
     *
     * This is triggered when a logged-in user toggles the Favourite button.
     *
     * @param card - The updated card entity to save to the database.
     */
    @Update
    suspend fun updateCard(card: PokemonCardEntity)

    /**
     * Deletes a single card from the database using the primary key contained
     * in the provided [PokemonCardEntity].
     *
     * Room matches the entity's `ID` value against the table's primary key
     * and removes the corresponding row.
     *
     * This is for when you already have the full entity object in memory.
     *
     * @param card - The card entity to delete.
     */
    @Delete
    suspend fun deleteCard(card: PokemonCardEntity)

    /**
     * Deletes a card from the database based on the card's unique ID.
     *
     * This method does a direct SQL DELETE without needing a full entity object,
     * so this is more efficient when only an ID is known.
     *
     * @param cardId - String - The ID of the card to delete.
     */
    @Query("DELETE FROM pokemonCards WHERE id = :cardId")
    suspend fun deleteCardById(cardId: String)

    /**
     * Deletes all cards from the database.
     *
     * This operation clears all records in the pokemonCards table.
     */
    @Query("DELETE FROM pokemonCards")
    suspend fun deleteAll()
}