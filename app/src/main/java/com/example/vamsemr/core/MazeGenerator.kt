package com.example.vamsemr.core

import kotlin.random.Random

class MazeGenerator(private val width: Int, private val height: Int) {
    private val maze = Array(height) { Array(width) { Cell() } }

    private data class Cell(
        var visited: Boolean = false,
        var top: Boolean = true,
        var bottom: Boolean = true,
        var left: Boolean = true,
        var right: Boolean = true
    )

    fun generateMaze(x: Int = 0, y: Int = 0) {
        maze[y][x].visited = true
        val directions = listOf("N", "S", "E", "W").shuffled()

        for (direction in directions) {
            val (nx, ny) = when (direction) {
                "N" -> Pair(x, y - 1)
                "S" -> Pair(x, y + 1)
                "E" -> Pair(x + 1, y)
                "W" -> Pair(x - 1, y)
                else -> continue
            }

            if (ny in 0 until height && nx in 0 until width && !maze[ny][nx].visited) {
                when (direction) {
                    "N" -> {
                        maze[y][x].top = false
                        maze[ny][nx].bottom = false
                    }
                    "S" -> {
                        maze[y][x].bottom = false
                        maze[ny][nx].top = false
                    }
                    "E" -> {
                        maze[y][x].right = false
                        maze[ny][nx].left = false
                    }
                    "W" -> {
                        maze[y][x].left = false
                        maze[ny][nx].right = false
                    }
                }
                generateMaze(nx, ny)
            }
        }
    }

    fun printMaze() {
        for (y in 0 until height) {
            // Print top walls
            for (x in 0 until width) {
                print(if (maze[y][x].top) "+---" else "+   ")
            }
            println("+")

            // Print left walls and spaces
            for (x in 0 until width) {
                print(if (maze[y][x].left) "|   " else "    ")
            }
            println("|")
        }

        // Print bottom border
        for (x in 0 until width) {
            print("+---")
        }
        println("+")
    }
}
