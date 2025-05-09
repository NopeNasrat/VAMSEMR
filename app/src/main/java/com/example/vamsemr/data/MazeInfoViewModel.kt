package com.example.vamsemr.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MazeInfoViewModel : ViewModel() {
    private val _MazeInfo = mutableStateOf(MazeInfo())
    val MazeInfo: State<MazeInfo> get() = _MazeInfo


    fun updateMazeInfo(newMazeInfo: MazeInfo) {
        _MazeInfo.value = newMazeInfo
    }
}