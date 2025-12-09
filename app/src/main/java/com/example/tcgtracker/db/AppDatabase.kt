package com.example.tcgtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tcgtracker.db.daos.PokemonCardDao
import com.example.tcgtracker.db.daos.PokemonCardSerieDao
import com.example.tcgtracker.db.daos.PokemonCardSetDao
import com.example.tcgtracker.db.entities.PokemonCardEntity
import com.example.tcgtracker.db.entities.PokemonCardSerieEntity
import com.example.tcgtracker.db.entities.PokemonCardSetEntity

/**
 * The main database holder for the app, which gives access to the DAOs and managing
 * the app's persistent data.
 *
 */
@Database(
    entities = [
        PokemonCardEntity::class,
        PokemonCardSetEntity::class,
        PokemonCardSerieEntity::class
   ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonCardDao(): PokemonCardDao
    abstract fun pokemonCardSetDao(): PokemonCardSetDao
    abstract fun pokemonCardSerieDao(): PokemonCardSerieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "TCGTracker"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}