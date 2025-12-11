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
     *
     */
    @Query("SELECT * FROM apiPokemonCards WHERE id = :id LIMIT 1")
    suspend fun getCardById(id: String): ApiPokemonCardEntity?

    /**
     *
     */
    @Query("SELECT * FROM apiPokemonCards")
    suspend fun getAllCards(): List<ApiPokemonCardEntity>

    /**
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: ApiPokemonCardEntity)

    /**
     *
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<ApiPokemonCardEntity>)
}