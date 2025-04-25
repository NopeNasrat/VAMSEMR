package com.example.vamsemr.data
import com.example.vamsemr.data.Cell

data class Maze(val width: Int, val height: Int) {
    val maze = Array(height) { Array(width) { Cell() } }
}
