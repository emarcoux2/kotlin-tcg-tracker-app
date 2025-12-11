package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tcgtracker.db.entities.UserPokemonCardEntity

@Dao
interface UserPokemonCardDao {
    @Query("SELECT * FROM userPokemonCards")
    suspend fun getAllUserCards(): List<UserPokemonCardEntity>

    @Query("SELECT * FROM userPokemonCards WHERE cardId = :cardId LIMIT 1")
    suspend fun getUserCard(cardId: String): UserPokemonCardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCard(card: UserPokemonCardEntity)

    @Query("DELETE FROM userPokemonCards WHERE localId = :localId")
    suspend fun deleteUserCard(localId: UserPokemonCardEntity)
}