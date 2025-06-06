package com.example.vamsemr.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel


/**
 * Viewmodel na aktualneho vybrateho hraca
 *
 * @author Bc. Fabo Peter
 */
class PlayerViewModel : ViewModel() {
    private val _player = mutableStateOf(Player())
    val player: State<Player> get() = _player

    /**
     * Update aktualneho hraca
     */
    fun updatePlayer(newPlayer: Player) {
        _player.value = newPlayer
    }
}
