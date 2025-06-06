package com.example.vamsemr.data

/**
 * Data class na aktualneho vybrateho hraca
 *
 * @author Bc. Fabo Peter
 */
data class Player(
    val id: Int = 0,
    val name: String = "",
    val skore: Int = 0,
    val games: Int = 0
)