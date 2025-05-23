package com.example.vamsemr.core

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vamsemr.data.Cell
import com.example.vamsemr.data.GameViewModel
import com.example.vamsemr.data.Maze
import kotlin.random.Random

/*
class MazeGenerator() {
    private var maze = Maze(1,1)*/



@Composable
fun resetMaze(gameViewModel: GameViewModel) {
    gameViewModel.resetMaze()
}

@Composable
fun movePlayer(
    gameViewModel: GameViewModel,
    smer: Smer
) {
    val maze = gameViewModel.Maze.value

    var playerX = -1
    var playerY = -1
    loop@ for (y in 0 until maze.height) {
        for (x in 0 until maze.width) {
            if (maze.maze[y][x].player) {
                playerX = x
                playerY = y
                break@loop
            }
        }
    }
    if (playerX == -1 || playerY == -1) return // hráč neexistuje

    val canMove = when(smer) {
        Smer.UP -> playerY > 0 && !maze.maze[playerY][playerX].top
        Smer.DOWN -> playerY < maze.height - 1 && !maze.maze[playerY][playerX].bottom
        Smer.LEFT -> playerX > 0 && !maze.maze[playerY][playerX].left
        Smer.RIGHT -> playerX < maze.width - 1 && !maze.maze[playerY][playerX].right
    }
    if (!canMove) return

    maze.maze[playerY][playerX].player = false
    when (smer) {
        Smer.UP -> maze.maze[playerY - 1][playerX].player = true
        Smer.DOWN -> maze.maze[playerY + 1][playerX].player = true
        Smer.LEFT -> maze.maze[playerY][playerX - 1].player = true
        Smer.RIGHT -> maze.maze[playerY][playerX + 1].player = true
    }
    gameViewModel.updateMaze(maze)
}


fun randomFinish(maze: Maze) {
    for (row in maze.maze) {
        for (cell in row) {
            cell.finish = false
            cell.flood = -1
        }
    }

    val finishX = Random.nextInt(maze.width)
    val finishY = Random.nextInt(maze.height)

    maze.maze[finishY][finishX].finish = true
    maze.maze[finishY][finishX].flood = 1
}


fun verifyMaze(maze: Maze): Boolean {
    var finishFound = false
    var playerFound = false

    for (row in maze.maze) {
        for (cell in row) {
            if (cell.finish) finishFound = true
            if (cell.player) playerFound = true
            if (finishFound && playerFound) {
                return true
            }
        }
    }
    return false
}




fun generatePlayer(maze: Maze) {
    val highFloods = maze.maze.flatten()
        .map { it.flood }
        .filter { it >= 150 }
    val maxFlood = if (highFloods.isNotEmpty()) {
        highFloods.random()
    } else {
        maze.maze.flatten()
            .map { it.flood }
            .maxOrNull() ?: return
    }

    // Vsetky bunky, ktore maju flood == maxFlood
    val candidates = mutableListOf<Pair<Int, Int>>()
    for (y in 0 until maze.height) {
        for (x in 0 until maze.width) {
            if (maze.maze[y][x].flood == maxFlood) {
                candidates.add(Pair(x, y))
            }
            maze.maze[y][x].player = false
        }
    }
    if (candidates.isEmpty()) return
    val (playerX, playerY) = candidates.random()
    maze.maze[playerY][playerX].player = true
}



fun floodMaze(maze: Maze) {
    var startX = -1
    var startY = -1
    loop@ for (y in 0 until maze.height) {
        for (x in 0 until maze.width) {
            if (maze.maze[y][x].finish) {
                startX = x
                startY = y
                break@loop
            }
        }
    }
    if (startX == -1 || startY == -1) return // finish nenajdeny


    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.add(Pair(startX, startY))
    maze.maze[startY][startX].flood = 1

    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        val currentFlood = maze.maze[y][x].flood

        // up
        if (!maze.maze[y][x].top && y > 0 && maze.maze[y - 1][x].flood == -1) {
            maze.maze[y - 1][x].flood = currentFlood + 1
            queue.add(Pair(x, y - 1))
        }
        // down
        if (!maze.maze[y][x].bottom && y < maze.height - 1 && maze.maze[y + 1][x].flood == -1) {
            maze.maze[y + 1][x].flood = currentFlood + 1
            queue.add(Pair(x, y + 1))
        }
        // right
        if (!maze.maze[y][x].right && x < maze.width - 1 && maze.maze[y][x + 1].flood == -1) {
            maze.maze[y][x + 1].flood = currentFlood + 1
            queue.add(Pair(x + 1, y))
        }
        // left
        if (!maze.maze[y][x].left && x > 0 && maze.maze[y][x - 1].flood == -1) {
            maze.maze[y][x - 1].flood = currentFlood + 1
            queue.add(Pair(x - 1, y))
        }
    }
}





fun generateMaze(
    x: Int = 0,
    y: Int = 0,
    maze: Maze
) {
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
            generateMaze(nx, ny, maze)
        }
    }
}




/*
@Composable
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
//}
*/