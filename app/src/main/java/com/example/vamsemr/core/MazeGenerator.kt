package com.example.vamsemr.core

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.data.compMazes
import com.example.vamsemr.data.Cell
import com.example.vamsemr.data.GameViewModel
import com.example.vamsemr.data.Maze
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.data.sql.MazeViewModel
import kotlin.random.Random

/*
class MazeGenerator() {
    private var maze = Maze(1,1)*/


/*
@Composable
fun resetMaze(gameViewModel: GameViewModel) {
    gameViewModel.resetMaze()
}*/

fun compressMaze(gameViewModel: GameViewModel,
                 mazeInfoViewModel: MazeInfoViewModel,
                 playerViewModel: PlayerViewModel,
                 mazeviewModel: MazeViewModel
): Boolean {
    var success = findFinish(gameViewModel, mazeInfoViewModel)
                && findPlayer(gameViewModel, mazeInfoViewModel)
    if (!success) return false

    val maze = gameViewModel.Maze.value
    val mazeString = mazeToString(maze)
    val comprimMazeString = binaryStringToCompressedString(mazeString)

    val datamaze = compMazes(id = playerViewModel.player.value.id,
        width = maze.width,
        height = maze.height,
        playerX = mazeInfoViewModel.MazeInfo.value.playerX,
        playerY = mazeInfoViewModel.MazeInfo.value.playerY,
        finishX = mazeInfoViewModel.MazeInfo.value.finishX,
        finishY = mazeInfoViewModel.MazeInfo.value.finishY,
        skore = playerViewModel.player.value.skore,
        skoregame = mazeInfoViewModel.MazeInfo.value.skorenow,
        maze = comprimMazeString
        )

    mazeviewModel.addOrUpdateMaze(datamaze)
    return true
}
fun binaryStringToCompressedString(binaryString: String): String {
    val paddedLength = ((binaryString.length + 7) / 8) * 8
    val paddedString = binaryString.padEnd(paddedLength, '0')

    val sb = StringBuilder()
    for (i in 0 until paddedLength step 8) {
        val byteString = paddedString.substring(i, i + 8)
        val byteValue = byteString.toInt(2)
        sb.append(byteValue.toChar())
    }
    return sb.toString()
}

fun mazeToString(maze: Maze): String {
    val sb = StringBuilder()
    for (y in 0 until maze.height) {
        for (x in 0 until maze.width) {
            val cell = maze.maze[y][x]
            sb.append(cellToString(cell))
        }
    }
    return sb.toString()
}
fun cellToString(cell: Cell): String {
    return buildString {
        append(if (cell.top) '1' else '0')
        append(if (cell.bottom) '1' else '0')
        append(if (cell.left) '1' else '0')
        append(if (cell.right) '1' else '0')
    }
}


fun decompressMaze(gameViewModel: GameViewModel,
                 mazeInfoViewModel: MazeInfoViewModel,
                 playerViewModel: PlayerViewModel,
                 mazeviewModel: MazeViewModel
): Boolean {




}



@Composable
fun winCheck(
    gameViewModel: GameViewModel,
    mazeInfoViewModel: MazeInfoViewModel
): Boolean {
    val aktualzacia = { findPlayer(gameViewModel = gameViewModel, mazeInfoViewModel = mazeInfoViewModel)
            && findFinish(gameViewModel = gameViewModel, mazeInfoViewModel = mazeInfoViewModel)}

    if (!aktualzacia()) return false

    val maze = gameViewModel.Maze.value
    val playerX = mazeInfoViewModel.MazeInfo.value.playerX
    val playerY = mazeInfoViewModel.MazeInfo.value.playerY
    val finishX = mazeInfoViewModel.MazeInfo.value.finishX
    val finishY = mazeInfoViewModel.MazeInfo.value.finishY


    if (playerX !in 0 until maze.width || playerY !in 0 until maze.height) return false
    if (finishX !in 0 until maze.width || finishY !in 0 until maze.height) return false


    return playerX == finishX && playerY == finishY
}




fun findFinish (
    gameViewModel: GameViewModel,
    mazeInfoViewModel: MazeInfoViewModel
): Boolean {
    val maze = gameViewModel.Maze.value

    var finishX = mazeInfoViewModel.MazeInfo.value.finishX
    var finishY = mazeInfoViewModel.MazeInfo.value.finishY

    val finishExistsAtStoredPos = finishX in 0 until maze.width &&
            finishY in 0 until maze.height &&
            maze.maze[finishY][finishX].finish

    if (finishExistsAtStoredPos) {
        return true
    } else {
        finishX = -1
        finishY = -1
        loop@ for (y in 0 until maze.height) {
            for (x in 0 until maze.width) {
                if (maze.maze[y][x].finish) {
                    finishX = x
                    finishY = y
                    break@loop
                }
            }
        }

        mazeInfoViewModel.updateMazeFinish(
            mazeInfoViewModel.MazeInfo.value.copy(
                finishX = finishX,
                finishY = finishY
            )
        )

        return finishX != -1 && finishY != -1
    }
}


fun setSkore(skore: Int,
             mazeInfoViewModel: MazeInfoViewModel
){
    mazeInfoViewModel.updateMazeSkore(
        mazeInfoViewModel.MazeInfo.value.copy(
            skorenow = skore,
        )
    )
}

fun findPlayer (
    gameViewModel: GameViewModel,
    mazeInfoViewModel: MazeInfoViewModel
): Boolean {
    val maze = gameViewModel.Maze.value

    var playerX = mazeInfoViewModel.MazeInfo.value.playerX
    var playerY = mazeInfoViewModel.MazeInfo.value.playerY

    val playerExistsAtStoredPos = playerX in 0 until maze.width &&
            playerY in 0 until maze.height &&
            maze.maze[playerY][playerX].player

    if (playerExistsAtStoredPos) {
        return true
    } else {
        playerX = -1
        playerY = -1
        loop@ for (y in 0 until maze.height) {
            for (x in 0 until maze.width) {
                if (maze.maze[y][x].player) {
                    playerX = x
                    playerY = y
                    break@loop
                }
            }
        }
        mazeInfoViewModel.updateMazePlayer(
            mazeInfoViewModel.MazeInfo.value.copy(
                playerX = playerX,
                playerY = playerY
            )
        )
        return playerX != -1 || playerY != -1
    }
}

fun movePlayer(
    gameViewModel: GameViewModel,
    mazeInfoViewModel: MazeInfoViewModel,
    smer: Smer
) {
    removeHint(gameViewModel = gameViewModel)
    val maze = gameViewModel.Maze.value


    if (!findPlayer(gameViewModel,mazeInfoViewModel)) return
    var playerX = mazeInfoViewModel.MazeInfo.value.playerX
    var playerY = mazeInfoViewModel.MazeInfo.value.playerY

    //println("$playerX $playerY")


    val canMove = when(smer) {
        Smer.UP -> playerY > 0 && !maze.maze[playerY][playerX].top
        Smer.DOWN -> playerY < maze.height - 1 && !maze.maze[playerY][playerX].bottom
        Smer.LEFT -> playerX > 0 && !maze.maze[playerY][playerX].left
        Smer.RIGHT -> playerX < maze.width - 1 && !maze.maze[playerY][playerX].right
    }
    if (!canMove) return

    maze.maze[playerY][playerX].player = false

    when (smer) {
        Smer.UP -> playerY -= 1
        Smer.DOWN -> playerY += 1
        Smer.LEFT -> playerX -= 1
        Smer.RIGHT -> playerX += 1
    }
    maze.maze[playerY][playerX].player = true

    mazeInfoViewModel.updateMazePlayer(
        mazeInfoViewModel.MazeInfo.value.copy(
            playerX = playerX,
            playerY = playerY
        )
    )
    mazeInfoViewModel.updateMazeSkore(
        mazeInfoViewModel.MazeInfo.value.copy(
            skorenow = mazeInfoViewModel.MazeInfo.value.skorenow-1
        )
    )

    //gameViewModel.updateMaze(maze)

    forceUpdateMaze(gameViewModel = gameViewModel,
        maze = maze)
    /*
    val newMazeGrid = maze.maze.map { row ->
        row.map { cell -> cell.copy() }.toTypedArray()
    }.toTypedArray()

    val newMaze = Maze(
        maze = newMazeGrid,
        width = maze.width,
        height = maze.height
    )
    gameViewModel.updateMaze(newMaze)*/
}

fun forceUpdateMaze(gameViewModel: GameViewModel, maze: Maze = gameViewModel.Maze.value) {
    val newMazeGrid = maze.maze.map { row ->
        row.map { cell -> cell.copy() }.toTypedArray()
    }.toTypedArray()

    val newMaze = Maze(
        maze = newMazeGrid,
        width = maze.width,
        height = maze.height
    )
    gameViewModel.updateMaze(newMaze)
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

fun removeHint(gameViewModel: GameViewModel) {
    val maze = gameViewModel.Maze.value
    for (y in 0 until maze.height) {
        for (x in 0 until maze.width) {
            maze.maze[y][x].hint = 0
        }
    }
    gameViewModel.updateMaze(maze.copy())
}


fun hint(gameViewModel: GameViewModel,mazeInfoViewModel: MazeInfoViewModel,x: Int = -1,y: Int = -1, counter: Int = 0) {
    var nextX = x
    var nextY = y
    if (x == -1 || y == -1) {
        if (!findPlayer(gameViewModel = gameViewModel, mazeInfoViewModel = mazeInfoViewModel)) return
        nextX = mazeInfoViewModel.MazeInfo.value.playerX
        nextY = mazeInfoViewModel.MazeInfo.value.playerY
    }
    val maze = gameViewModel.Maze.value
    maze.maze[nextY][nextX].hint = counter
    val directions = listOf(
        Triple(0, -1, "top"),
        Triple(0, 1, "bottom"),
        Triple(1, 0, "right"),
        Triple(-1, 0, "left")
    )

    var bestX = nextX
    var bestY = nextY
    var minFlood = maze.maze[nextY][nextX].flood
    for ((dx, dy, direction) in directions) {
        val nx = nextX + dx
        val ny = nextY + dy

        if (ny in 0 until maze.height && nx in 0 until maze.width) {
            val wallBlocked = when (direction) {
                "top" -> maze.maze[nextY][nextX].top
                "bottom" -> maze.maze[nextY][nextX].bottom
                "right" -> maze.maze[nextY][nextX].right
                "left" -> maze.maze[nextY][nextX].left
                else -> true
            }
            if (wallBlocked) continue

            val neighborFlood = maze.maze[ny][nx].flood
            if (neighborFlood in 0 until minFlood) {
                minFlood = neighborFlood
                bestX = nx
                bestY = ny
            }
        }
    }
    nextX = bestX
    nextY = bestY
    val counternext = counter - 1
    //gameViewModel.updateMaze(maze.copy())
    if (counternext > 0) {
        hint(gameViewModel, mazeInfoViewModel, nextX, nextY, counternext)
    } else {
        gameViewModel.updateMaze(maze.copy())
    }
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
    maze: Maze,
    randwall: Int = 0
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
            generateMaze(nx, ny, maze, randwall)
        } else if (ny in 0 until maze.height && nx in 0 until maze.width && maze.maze[ny][nx].visited) {
            val randomNumber = (0..100).random()
            if (randwall > randomNumber) {
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
            }
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