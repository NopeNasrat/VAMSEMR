package com.example.vamsemr.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.util.copy

/**
 * ViewModel na dodatocne Informacie o bludisku
 *
 *
 * @author Bc. Fabo Peter
 */
class MazeInfoViewModel : ViewModel() {
    private val _MazeInfo = mutableStateOf(MazeInfo())
    val MazeInfo: State<MazeInfo> get() = _MazeInfo

    /**
     * Updatni vsetky
     */
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

    /**
     * update na velkost bludiska
     */
    fun updateMazeInfo(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            x = newMazeInfo.x,
            y = newMazeInfo.y
        )
    }
    /**
     * update na suradnice finishu
     */
    fun updateMazeFinish(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            finishX = newMazeInfo.finishX,
            finishY = newMazeInfo.finishY
        )
    }
    /**
     * update na suradnice hraca
     */
    fun updateMazePlayer(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            playerX = newMazeInfo.playerX,
            playerY = newMazeInfo.playerY
        )
    }
    /**
     * update na aktualne skore
     */
    fun updateMazeSkore(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            skorenow = newMazeInfo.skorenow,
        )
    }
    /**
     * update na zapisanu hodnotu do databazy
     */
    fun updateMazeWrite(newMazeInfo: MazeInfo) {
        _MazeInfo.value = _MazeInfo.value.copy(
            zapisane = newMazeInfo.zapisane,
        )
    }
    /**
     * update na zapisanu hodnotu do databazy
     */
    fun setZapisane(zapisane: Boolean) {
        _MazeInfo.value = _MazeInfo.value.copy(
            zapisane = zapisane
        )
    }
    /**
     * povolenie zvuku
     */
    fun setSounds(sounds: Boolean) {
        _MazeInfo.value = _MazeInfo.value.copy(
            sounds = sounds
        )
    }
    /**
     * povolenie zvuku cez permisie
     */
    fun allowSounds(soundPermision: Boolean = true) {
        _MazeInfo.value = _MazeInfo.value.copy(
            soundPermision = soundPermision
        )
    }
}