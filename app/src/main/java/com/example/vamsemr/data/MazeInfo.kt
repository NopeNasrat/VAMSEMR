package com.example.vamsemr.data
/**
 * Dodatocne Informacie o bludisku
 *
 * @param x +y velkost bludiska
 * @param finishX +finishY suradnice finish
 * @param playerX +playerY suradnice player
 * @param skorenow skore aktualnej hry
 * @param zapisane ci hodnoty boli zapisana do databazy po hre
 * @param sounds povolit prehravat zvuky
 * @param soundPermision su povolene permisie zo systemu?
 *
 * @author Bc. Fabo Peter
 */
data class MazeInfo(
    val x: Int = 0,
    val y: Int = 0,
    val finishX: Int = -1,
    val finishY: Int = -1,
    val playerX: Int = -1,
    val playerY: Int = -1,
    val skorenow: Int = 0,
    val zapisane: Boolean = false,
    val sounds: Boolean = true,
    val soundPermision: Boolean = false
)
