package com.example.vamsemr.data
import com.example.vamsemr.data.Cell



data class Maze(
    val width: Int,
    val height: Int,
    val maze: Array<Array<Cell>>
) {
    // sekundarny konstruktor
    constructor(width: Int, height: Int) : this(
        width,
        height,
        Array(height) { Array(width) { Cell() } }
    )
}




/*
data class Maze(
    val width: Int,
    val height: Int,
) {
    val maze = Array(height) { Array(width) { Cell() } }
}
*/