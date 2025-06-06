package com.example.vamsemr.data

import android.graphics.Color
/**
 * Data class na jedno policko bludiska
 *
 * @param visited podpora pri generovani ci uz bolo navstivene a teda aj vygenerovane
 * @param top,bottom,left,right su steny
 * @param player ci je tam player
 * @param finish ci je tam finish
 * @param flood vzdialenost od finish
 * @param hint zobrazit hint
 * @author Bc. Fabo Peter
 */
data class Cell(
    var visited: Boolean = false,
    var top: Boolean = true,
    var bottom: Boolean = true,
    var left: Boolean = true,
    var right: Boolean = true,
    var finish: Boolean = false,
    var flood: Int = -1,
    var player: Boolean = false,
    var hint: Int = 0
){

}
