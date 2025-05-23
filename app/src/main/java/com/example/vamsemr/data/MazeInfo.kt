package com.example.vamsemr.data

data class MazeInfo(
    val x: Int = 0,
    val y: Int = 0,
    val finishX: Int = -1,
    val finishY: Int = -1,
    val playerX: Int = -1,
    val playerY: Int = -1,
)
