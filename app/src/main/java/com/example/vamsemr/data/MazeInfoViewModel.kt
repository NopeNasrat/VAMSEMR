package com.example.vamsemr.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.util.copy

class MazeInfoViewModel : ViewModel() {
    private val _MazeInfo = mutableStateOf(MazeInfo())
    val MazeInfo: State<MazeInfo> get() = _MazeInfo


    fun updateMazeInfo(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            x = newMazeInfo.x,
            y = newMazeInfo.y
        )
    }

    fun updateMazeFinish(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            finishX = newMazeInfo.finishX,
            finishY = newMazeInfo.finishY
        )
    }

    fun updateMazePlayer(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            playerX = newMazeInfo.playerX,
            playerY = newMazeInfo.playerY
        )
    }
}