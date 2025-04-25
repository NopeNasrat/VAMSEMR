package com.example.vamsemr.core

import com.example.vamsemr.data.Cell
import com.example.vamsemr.data.Maze
import kotlin.random.Random

class MazeGenerator() {
    private var maze = Maze(1,1)

    fun resetMaze(width: Int, height: Int) {
        maze = Maze(width,height)
    }

    fun generateMaze(x: Int = 0, y: Int = 0) {
        maze.maze[y][x].visited = true
        val directions = listOf("N", "S", "E", "W").shuffled()

        for (direction in directions) {
            val (nx, ny) = when (direction) {
                "N" -> Pair(x, y - 1)
                "S" -> Pair(x, y + 1)
                "E" -> Pair(x + 1, y)
                "W" -> Pair(x - 1, y)
                else -> continue
            }

            if (ny in 0 until maze.height && nx in 0 until maze.width && !maze.maze[ny][nx].visited) {
                when (direction) {
                    "N" -> {
                        maze.maze[y][x].top = false
                        maze.maze[ny][nx].bottom = false
                    }
                    "S" -> {
                        maze.maze[y][x].bottom = false
                        maze.maze[ny][nx].top = false
                    }
                    "E" -> {
                        maze.maze[y][x].right = false
                        maze.maze[ny][nx].left = false
                    }
                    "W" -> {
                        maze.maze[y][x].left = false
                        maze.maze[ny][nx].right = false
                    }
                }
                generateMaze(nx, ny)
            }
        }
    }

    fun printMaze() {
        for (y in 0 until maze.height) {
            // top wall
            for (x in 0 until maze.width) {
                print(
                    if (maze.maze[y][x].top)
                        "+---"
                    else
                        "+   "
                )
            }
            println("+")

            // left wall or space
            for (x in 0 until maze.width) {
                print(
                    if (maze.maze[y][x].left)
                        "|   "
                    else
                        "    "
                )
            }
            // end wall
            println("|")
        }

        // bottom wall
        for (x in 0 until maze.width) {
            print("+---")
        }

        // end bottom
        println("+")
    }
}
