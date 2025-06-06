package com.example.vamsemr.Navigation

/**
 * Rozhranie reprezentujúce navigačný cieľ v aplikácii.
 * Každý cieľ (screen) má svoju unikátnu route a názov.
 *
 * @author Bc. Fabo Peter
 */
interface NavigationDestination {
    val route: String
    val titleRes: Int
}