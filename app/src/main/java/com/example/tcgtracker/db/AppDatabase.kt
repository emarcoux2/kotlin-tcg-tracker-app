package com.example.tcgtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tcgtracker.db.daos.ApiPokemonCardDao
import com.example.tcgtracker.db.daos.UserPokemonCardDao
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.PokemonCardSerieEntity
import com.example.tcgtracker.db.entities.PokemonCardSetEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity

/**
 * The main Room database for the application, providing access to all DAOs
 * and managing the app's persistent data for Pokémon cards.
 *
 * This database serves as the single source of truth for local storage, including:
 * Cached API card metadata (ApiPokemonCardEntity)
 * Pokémon card sets (PokemonCardSetEntity)
 * Pokémon card series (PokemonCardSerieEntity)
 * User-owned Pokémon cards (UserPokemonCardEntity)
 *
 * @property version - The database version (used for migrations).
 * @property exportSchema - Whether the database schema should be exported.
 *
 */
@Database(
    entities = [
        ApiPokemonCardEntity::class,
        PokemonCardSetEntity::class,
        PokemonCardSerieEntity::class,
        UserPokemonCardEntity::class
   ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun apiPokemonCardDao(): ApiPokemonCardDao
    abstract fun userPokemonCardDao(): UserPokemonCardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TCGTracker"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}