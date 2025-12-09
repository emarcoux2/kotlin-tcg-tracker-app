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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSerie(serie: PokemonCardSerieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSeries(series: List<PokemonCardSerieEntity>)

    // READ
    @Query("SELECT * FROM pokemonCardSeries")
    suspend fun getAllSeries(): List<PokemonCardSerieEntity>

    @Query("SELECT * FROM pokemonCardSeries WHERE id = :serieId")
    suspend fun getSerieById(serieId: String): PokemonCardSerieEntity?

    // UPDATE
    @Update
    suspend fun updateSerie(serie: PokemonCardSerieEntity)

    // DELETE
    @Delete
    suspend fun deleteSerie(serie: PokemonCardSerieEntity)

    @Query("DELETE FROM pokemonCardSeries WHERE id = :serieId")
    suspend fun deleteSerieById(serieId: String)

    @Query("DELETE FROM pokemonCardSeries")
    suspend fun deleteAllSeries()
}