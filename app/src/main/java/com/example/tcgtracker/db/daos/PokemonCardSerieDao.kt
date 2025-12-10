package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.PokemonCardSerieEntity

@Dao
interface PokemonCardSerieDao {
    /**
     * Inserts a single series into the database.
     *
     * Replaces the series with the same primary key if it exists.
     *
     * @param serie - The series entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: PokemonCardSerieEntity)

    /**
     * Retrieves all stored series from the database.
     *
     * @return A list containing every series entity in the `pokemonCardSeries` table.
     */
    @Query("SELECT * FROM pokemonCardSerie")
    suspend fun getAllSeries(): List<PokemonCardSerieEntity>

    /**
     * Retrieves a single series entity by its unique ID.
     *
     * @param serieId - The ID of the series to fetch.
     * @return The matching series entity, or `null` if none exists.
     */
    @Query("SELECT * FROM pokemonCardSerie WHERE id = :serieId")
    suspend fun getSerieById(serieId: String): PokemonCardSerieEntity?

    /**
     * Updates an existing series entry in the database.
     *
     * The series must already exist; Room matches it using its primary key.
     *
     * @param serie - The updated series entity.
     */
    @Update
    suspend fun updateSerie(serie: PokemonCardSerieEntity)

    /**
     * Deletes a specific serie from the database.
     *
     * Room matches the row to delete using the entity’s primary key.
     *
     * @param serie - The series entity to remove.
     */
    @Delete
    suspend fun deleteSerie(serie: PokemonCardSerieEntity)

    /**
     * Deletes a single series entry by its ID.
     *
     * @param serieId - The ID of the series to delete.
     */
    @Query("DELETE FROM pokemonCardSerie WHERE id = :serieId")
    suspend fun deleteSerieById(serieId: String)

    /**
     * Deletes all series entries from the database.
     */
    @Query("DELETE FROM pokemonCardSerie")
    suspend fun deleteAllSeries()
}