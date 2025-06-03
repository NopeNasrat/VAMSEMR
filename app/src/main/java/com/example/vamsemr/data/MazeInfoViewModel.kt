package com.example.vamsemr.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.util.copy

class MazeInfoViewModel : ViewModel() {
    private val _MazeInfo = mutableStateOf(MazeInfo())
    val MazeInfo: State<MazeInfo> get() = _MazeInfo


    fun updateALLMazeInfo(newMazeInfo: MazeInfo) {
        _MazeInfo.value = newMazeInfo
        /*
            _MazeInfo.value.copy(
            x = newMazeInfo.x,
            y = newMazeInfo.y,
            playerX = newMazeInfo.playerX,
            playerY = newMazeInfo.playerY,
            finishX = newMazeInfo.finishX,
            finishY = newMazeInfo.finishY,
            skorenow = newMazeInfo.skorenow
        )*/
    }

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

    fun updateMazeSkore(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            skorenow = newMazeInfo.skorenow,
        )
    }

    fun updateMazeWrite(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            zapisane = newMazeInfo.zapisane,
        )
    }

    fun setZapisane(zapisane: Boolean) {
        _MazeInfo.value = _MazeInfo.value.copy(
            zapisane = zapisane
        )
    }

    fun setSounds(sounds: Boolean) {
        _MazeInfo.value = _MazeInfo.value.copy(
            sounds = sounds
        )
    }

    fun allowSounds(soundPermision: Boolean = true) {
        _MazeInfo.value = _MazeInfo.value.copy(
            soundPermision = soundPermision
        )
    }
}