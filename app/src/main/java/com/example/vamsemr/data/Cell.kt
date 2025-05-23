package com.example.vamsemr.data

data class Cell(
    var visited: Boolean = false,
    var top: Boolean = true,
    var bottom: Boolean = true,
    var left: Boolean = true,
    var right: Boolean = true,
    var finish: Boolean = false,
    var flood: Int = -1,
    var player: Boolean = false
){

}
