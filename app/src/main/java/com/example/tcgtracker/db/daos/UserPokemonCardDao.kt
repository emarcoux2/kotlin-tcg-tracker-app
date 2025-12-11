package com.example.tcgtracker.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tcgtracker.db.entities.UserPokemonCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPokemonCardDao {
    @Query("SELECT * FROM userPokemonCards")
    fun getAllUserCardsFlow(): Flow<List<UserPokemonCardEntity>>
    @Query("SELECT * FROM userPokemonCards")
    suspend fun getAllUserCards(): List<UserPokemonCardEntity>

    @Query("SELECT * FROM userPokemonCards WHERE cardId = :cardId LIMIT 1")
    suspend fun getUserCard(cardId: String): UserPokemonCardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCard(card: UserPokemonCardEntity) {}

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCards(cards: List<UserPokemonCardEntity>)

    @Update
    suspend fun updateFavouriteCard(card: UserPokemonCardEntity)

    @Query("DELETE FROM userPokemonCards WHERE localId = :localId")
    suspend fun deleteUserCard(localId: Long)
}