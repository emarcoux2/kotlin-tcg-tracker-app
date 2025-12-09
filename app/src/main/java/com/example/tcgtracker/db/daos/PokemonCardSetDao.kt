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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(card: PokemonCardSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSets(cards: List<PokemonCardSetEntity>)

    // READ
    @Query("SELECT * FROM pokemonCardSets")
    suspend fun getAllSets(): List<PokemonCardSetEntity>

    @Query("SELECT * FROM pokemonCardSets WHERE id = :setId")
    suspend fun getSetById(setId: String): PokemonCardSetEntity?

    // UPDATE
    @Update
    suspend fun updateSet(set: PokemonCardSetEntity)

    // DELETE
    @Delete
    suspend fun deleteSet(set: PokemonCardSetEntity)

    @Query("DELETE FROM pokemonCardSets WHERE id = :setId")
    suspend fun deleteSetById(setId: String)

    @Query("DELETE FROM pokemonCardSets")
    suspend fun deleteAllSets()
}