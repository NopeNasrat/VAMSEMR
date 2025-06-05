package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Kód prevzatý a následne upravený zo cvičení VAPMZ.

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("SELECT * from profils WHERE id = :id")
    fun getItem(id: Int): Flow<Item?>

    @Query("SELECT * from profils ORDER BY name ASC")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT id FROM profils ORDER BY id ASC")
    suspend fun getAllIds(): List<Int>

}

@Dao
interface MazeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maze: compMazes)

    @Update
    suspend fun update(maze: compMazes)

    @Delete
    suspend fun delete(maze: compMazes)

    @Query("SELECT * FROM mazes WHERE id = :id")
    fun getMaze(id: Int): Flow<compMazes?>

    @Query("SELECT * FROM mazes ORDER BY id ASC")
    fun getAllMazes(): Flow<List<compMazes>>

    @Query("SELECT id FROM mazes ORDER BY id ASC")
    suspend fun getAllMazeIds(): List<Int>
}
