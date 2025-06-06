package com.example.vamsemr.Navigation
/**
 * Enum trieda reprezentujúca jednotlivé obrazovky aplikácie.
 * Každá položka obsahuje trasu (route), ktorá sa používa pri navigácii.
 *
 * @author Bc. Fabo Peter
 */
enum class Screen(val route: String) {
    HOME("home"),
    SCREEN2VERT2("homevert2"),
    GAME("GameScr"),
    HOMEHORZ("homehorz"),
    GAMEHORZ("GameScrHorz")

    // Pridaj ďalšie obrazovky podľa potreby
}

