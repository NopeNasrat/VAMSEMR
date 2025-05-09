package com.example.vamsemr.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.ui.HomeDestination
import com.example.vamsemr.ui.MainScreenV1
import com.example.vamsemr.ui.StartScreenVert2

@Composable
fun VamsemrNavHost(
    navController: NavHostController,
    viewModel: ItemViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            MainScreenV1(
                viewModel = viewModel,
                modifier = modifier
                // Tu môžeš pridať lambdy pre navigáciu na ďalšie obrazovky
            )
        }

        // composable(OtherDestination.route) { ... }
    }
}