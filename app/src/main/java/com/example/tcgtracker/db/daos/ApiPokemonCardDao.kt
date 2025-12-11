package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity

/**
 * Data Access Object (DAO) for the PokemonCardEntity table.
 *
 * This interface defines CRUD for managing a user's Pokemon Cards in
 * the local Room database.
 * DAOs are the main interfaces used to interact with database data.
 */
@Dao
interface ApiPokemonCardDao {

    /**
     * Retrieves a single API card entity by its unique ID.
     *
     * @param id - The unique identifier of the card.
     * @return - The ApiPokemonCardEntity matching the given ID, or null if not found.
     */
    @Query("SELECT * FROM apiPokemonCards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: String): ApiPokemonCardEntity?

    /**
     * Retrieves all API card entities from the local database.
     *
     * @return - A list of all ApiPokemonCardEntity stored locally.
     */
    @Query("SELECT * FROM apiPokemonCards")
    suspend fun getAllCards(): List<ApiPokemonCardEntity>

    /**
     * Inserts a single API card entity into the database.
     *
     * If a card with the same ID already exists, it will be replaced.
     *
     * @param card - The ApiPokemonCardEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: ApiPokemonCardEntity)

    /**
     * Inserts multiple API card entities into the database at once.
     *
     * Existing cards with matching IDs will be replaced.
     *
     * @param cards - List of ApiPokemonCardEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<ApiPokemonCardEntity>)
}