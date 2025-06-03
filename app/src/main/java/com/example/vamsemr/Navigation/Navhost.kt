package com.example.vamsemr.Navigation

import android.content.res.Configuration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.core.Game
import com.example.vamsemr.core.GameHorz
import com.example.vamsemr.core.SoundManager
import com.example.vamsemr.data.GameViewModel
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.data.ScreenViewModel
import com.example.vamsemr.data.sql.MazeViewModel
import com.example.vamsemr.ui.MainScreenV1
import com.example.vamsemr.ui.StartScreenHorz
import com.example.vamsemr.ui.StartScreenVert2




@Composable
fun VamsemrNavHost(
    navController: NavHostController,
    viewModel: ItemViewModel,
    mazeviewModel: MazeViewModel,
    modifier: Modifier = Modifier
) {

    val playerViewModel: PlayerViewModel = viewModel()
    val mazeInfoViewModel: MazeInfoViewModel = viewModel()
    val gameViewModel: GameViewModel = viewModel()

    val configuration = LocalConfiguration.current
    val screenViewModel: ScreenViewModel = viewModel()
    val appStage by screenViewModel.stage.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SoundManager.init(context)
    }

    LaunchedEffect(configuration.orientation, appStage) {
        val currentRoute = navController.currentDestination?.route

        when (appStage) {
            1 -> {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    currentRoute != Screen.HOMEHORZ.route
                ) {
                    navController.navigate(Screen.HOMEHORZ.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT &&
                    currentRoute != Screen.HOME.route
                ) {
                    navController.navigate(Screen.HOME.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            2 -> {
                if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT &&
                    currentRoute != Screen.GAME.route) {
                    navController.navigate(Screen.GAME.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                    currentRoute != Screen.GAMEHORZ.route) {
                    navController.navigate(Screen.GAMEHORZ.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }





        }
    }

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
                navController = navController,
                screenViewModel = screenViewModel
            )
        }

        composable(route = Screen.SCREEN2VERT2.route) {
            StartScreenVert2(
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController,
                screenViewModel = screenViewModel
            )
        }

        composable(route = Screen.HOMEHORZ.route) {
            StartScreenHorz (
                viewModel = viewModel,
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController,
                screenViewModel = screenViewModel
            )
        }


        composable(route = Screen.GAME.route) {
            Game(
                viewModel = viewModel,
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController,
                gameViewModel = gameViewModel,
                mazeviewModel = mazeviewModel,
                screenViewModel = screenViewModel
            )
        }

        composable(route = Screen.GAMEHORZ.route) {
            GameHorz(
                viewModel = viewModel,
                modifier = modifier,
                playerViewModel = playerViewModel,
                mazeInfoViewModel = mazeInfoViewModel,
                navController = navController,
                gameViewModel = gameViewModel,
                mazeviewModel = mazeviewModel,
                screenViewModel = screenViewModel
            )
        }
    }
}