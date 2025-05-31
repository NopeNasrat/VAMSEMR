package com.example.vamsemr.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vamsemr.core.compressMaze
import com.example.vamsemr.core.decompressMaze
import com.example.vamsemr.core.findFinish
import com.example.vamsemr.core.findPlayer
import com.example.vamsemr.core.floodMaze
import com.example.vamsemr.core.generateMaze
import com.example.vamsemr.core.generatePlayer
import com.example.vamsemr.core.randomFinish
import com.example.vamsemr.core.setSkore
import com.example.vamsemr.core.verifyMaze
import com.example.vamsemr.data.sql.MazeViewModel
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private var x: Int = 2
    private var y: Int = 2
    private val _Maze = mutableStateOf(Maze(x,y))
    val Maze: State<Maze> get() = _Maze

    fun setAndResetMaze(newX: Int, newY: Int,mazeInfoViewModel: MazeInfoViewModel) {
        updateSize(newX, newY)
        resetMaze(mazeInfoViewModel)
    }

    fun updateSize(newX: Int, newY: Int) {
        if (newX >= 2) x = newX
        if (newY >= 2) y = newY
    }


    fun updateMaze(newMaze: Maze) {
        _Maze.value = newMaze
    }

    fun resetMaze(mazeInfoViewModel: MazeInfoViewModel) {
        var newMaze: Maze
        do {
            newMaze = Maze(x, y)
            generateMaze(0, 0, newMaze, randwall = 5)
            randomFinish(newMaze)
            floodMaze(newMaze)
            generatePlayer(newMaze)
            setSkore(x*y,mazeInfoViewModel)
        } while (!verifyMaze(newMaze))

        _Maze.value = newMaze
        findFinish(this, mazeInfoViewModel)
        findPlayer(this, mazeInfoViewModel)
    }

    fun saveMaze(gameViewModel: GameViewModel,
                 mazeInfoViewModel: MazeInfoViewModel,
                 playerViewModel: PlayerViewModel,
                 mazeviewModel: MazeViewModel
    ){
        compressMaze(gameViewModel = gameViewModel,
            mazeInfoViewModel = mazeInfoViewModel,
            mazeviewModel = mazeviewModel,
            playerViewModel = playerViewModel)
    }


    fun loadMaze(
        gameViewModel: GameViewModel,
        mazeInfoViewModel: MazeInfoViewModel,
        mazeViewModel: MazeViewModel,
        playerViewModel: PlayerViewModel
    ) {
        viewModelScope.launch {
            decompressMaze(
                gameViewModel = gameViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                mazeviewModel = mazeViewModel,
                playerViewModel = playerViewModel
            )
        }
    }
}

