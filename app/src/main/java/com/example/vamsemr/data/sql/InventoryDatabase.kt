package com.example.inventory.data

// Kód prevzatý a následne upravený zo cvičení VAPMZ.

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class, compMazes::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun mazeDao(): MazeDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "profils_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }

        }

    }

}