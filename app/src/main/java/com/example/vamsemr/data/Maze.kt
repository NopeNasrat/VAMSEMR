package com.example.vamsemr.data
import com.example.vamsemr.data.Cell


/**
 * Data class bludiska
 *
 * @param width sirka bludiska
 * @param height vyska bludiska
 * @param maze 2D bludisko s CELL
 *
 * @constructor sekndarny construktor na vytvorenie prazdneho bludiska zadanim iba width a height
 *
 * @author Bc. Fabo Peter
 */
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