package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.PokemonCardEntity

@Dao
interface PokemonCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: PokemonCardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCards(cards: List<PokemonCardEntity>)

    // READ
    @Query("SELECT * FROM pokemonCards")
    suspend fun getAllCards(): List<PokemonCardEntity>

    @Query("SELECT * FROM pokemonCards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): PokemonCardEntity?

    // UPDATE
    @Update
    suspend fun updateCard(card: PokemonCardEntity)

    // DELETE
    @Delete
    suspend fun deleteCard(card: PokemonCardEntity)

    @Query("DELETE FROM pokemonCards WHERE id = :cardId")
    suspend fun deleteCardById(cardId: String)

    @Query("DELETE FROM pokemonCards")
    suspend fun deleteAll()
}