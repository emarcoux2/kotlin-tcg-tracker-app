package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.PokemonCardSetEntity

@Dao
interface PokemonCardSetDao {

    /**
     * Inserts a single card set into the database.
     * If a set with the same ID already exists, it will be replaced.
     *
     * @param set - The PokemonCardSetEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(card: PokemonCardSetEntity)

    /**
     * Retrieves all card sets from the database.
     *
     * @return A list of all PokemonCardSetEntity objects stored in the database.
     */
    @Query("SELECT * FROM pokemonCardSet")
    suspend fun getAllSets(): List<PokemonCardSetEntity>

    /**
     * Retrieves a specific card set by its ID.
     *
     * @param setId - The unique ID of the card set to retrieve.
     * @return The PokemonCardSetEntity with the matching ID, or null if not found.
     */
    @Query("SELECT * FROM pokemonCardSet WHERE id = :setId")
    suspend fun getSetById(setId: String): PokemonCardSetEntity?

    /**
     * Updates an existing card set in the database.
     *
     * @param set - The PokemonCardSetEntity with updated data.
     */
    @Update
    suspend fun updateSet(set: PokemonCardSetEntity)

    /**
     * Deletes a specific card set from the database.
     *
     * @param set - The PokemonCardSetEntity to delete.
     */
    @Delete
    suspend fun deleteSet(set: PokemonCardSetEntity)

    /**
     * Deletes a card set from the database by its ID.
     *
     * @param setId - The unique ID of the card set to delete.
     */
    @Query("DELETE FROM pokemonCardSet WHERE id = :setId")
    suspend fun deleteSetById(setId: String)

    /**
     * Deletes all card sets from the database.
     */
    @Query("DELETE FROM pokemonCardSet")
    suspend fun deleteAllSets()
}