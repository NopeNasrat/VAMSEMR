package com.example.inventory.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Kód prevzatý a následne upravený zo cvičení VAPMZ.

/**
 * Entity data class represents a single row in the database.
 */
@Entity(tableName = "profils")
data class Item(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val skore: Int,
    val games: Int
)


@Entity(tableName = "mazes")
data class compMazes(
    @PrimaryKey
    val id: Int = 0,
    val height: Int,
    val width: Int,
    val playerX: Int,
    val playerY: Int,
    val finishX: Int,
    val finishY: Int,
    val skore: Int,
    val skoregame: Int,
    val maze: String
)
