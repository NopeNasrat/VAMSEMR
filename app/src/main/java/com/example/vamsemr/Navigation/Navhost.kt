package com.example.vamsemr.Navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.core.Game
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.ui.HomeDestination
import com.example.vamsemr.ui.HomeDestinationvert2
import com.example.vamsemr.ui.MainScreenV1
import com.example.vamsemr.ui.StartScreenVert2

@Composable
fun VamsemrNavHost(
    navController: NavHostController,
    viewModel: ItemViewModel,
    modifier: Modifier = Modifier
) {

    val playerViewModel: PlayerViewModel = viewModel()
    val mazeInfoViewModel: MazeInfoViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.HOME.route,
        modifier = modifier
    ) {
        composable(route = Screen.HOME.route) {
            MainScreenV1(
                viewModel = viewModel,
                modifier = modifier,
                playerViewModel = playerViewModel,
                navController = navController
            )
        }

        composable(route = Screen.SCREEN2VERT2.route) {
            StartScreenVert2(
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController
            )
        }


        composable(route = Screen.GAME.route) {
            Game(
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController
            )
        }
        // composable(OtherDestination.route) { ... }
    }
}