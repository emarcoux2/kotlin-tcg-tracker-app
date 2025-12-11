package com.example.tcgtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tcgtracker.db.daos.ApiPokemonCardDao
import com.example.tcgtracker.db.daos.PokemonCardSerieDao
import com.example.tcgtracker.db.daos.PokemonCardSetDao
import com.example.tcgtracker.db.daos.UserPokemonCardDao
import com.example.tcgtracker.db.entities.ApiPokemonCardEntity
import com.example.tcgtracker.db.entities.PokemonCardSerieEntity
import com.example.tcgtracker.db.entities.PokemonCardSetEntity
import com.example.tcgtracker.db.entities.UserPokemonCardEntity

/**
 * The main database holder for the app, which gives access to the DAOs and managing
 * the app's persistent data.
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