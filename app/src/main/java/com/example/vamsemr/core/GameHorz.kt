package com.example.vamsemr.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inventory.data.Item
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.Navigation.ConfirmExitOnBackHandler
import com.example.vamsemr.Navigation.NavigationDestination
import com.example.vamsemr.Navigation.Screen
import com.example.vamsemr.R
import com.example.vamsemr.data.GameViewModel
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.data.ScreenViewModel
import com.example.vamsemr.data.sql.MazeViewModel

object GameScreenHorz : NavigationDestination {
    override val route = "GameScrHorz"
    override val titleRes: Int = 0
}

/**
 * Screen s hrou na horizontalne rozpolozenie.
 *
 * @param viewModel Player databaza
 * @param playerViewModel Info o aktualnom hracovi
 * @param screenViewModel Nastavenie stage na screeny
 * @param mazeInfoViewModel Zdruzene informacie o aktualnej hre
 * @param gameViewModel 2D maze pole hry
 * @param navController Navigacia cez screeny
 * @param mazeviewModel Databaza z maze
 *
 * @author Bc. Fabo Peter
 */
@Composable
fun GameHorz(
    viewModel: ItemViewModel,
    playerViewModel: PlayerViewModel,
    screenViewModel: ScreenViewModel,
    modifier: Modifier = Modifier,
    mazeviewModel: MazeViewModel,
    mazeInfoViewModel: MazeInfoViewModel,
    gameViewModel: GameViewModel,
    navController: NavHostController
) {
    val player by playerViewModel.player
    val mazeInfo by mazeInfoViewModel.MazeInfo
    val maze by gameViewModel.Maze

    var showConfirmDialogExit by remember { mutableStateOf(false) }
    var showConfirmDialogMenu by remember { mutableStateOf(false) }
    var showConfirmDialogHint by remember { mutableStateOf(false) }
    var showConfirmDialogSave by remember { mutableStateOf(false) }
    var showConfirmDialogLoad by remember { mutableStateOf(false) }
    var isSoundMuted by remember { mutableStateOf(false) }

    ConfirmExitOnBackHandler {
        SoundManager.release()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    LaunchedEffect(mazeInfo.x, mazeInfo.y) {
        if (mazeInfo.x != maze.width || mazeInfo.y != maze.height) {
            gameViewModel.updateSize(mazeInfo.x, mazeInfo.y)
            gameViewModel.resetMaze(mazeInfoViewModel)
        }
    }

    var win = winCheck(
        gameViewModel = gameViewModel,
        mazeInfoViewModel = mazeInfoViewModel
    )
    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        MazeCanvas(
            maze = maze,
            modifier = Modifier
                .padding(top = 10.dp)
                //.background(Color.Magenta)
                .align(Alignment.Top),
            mazeInfoViewModel = mazeInfoViewModel
        )

        ButtonsCollumGame(
            //modifier = modifier,
            onBackClick = { showConfirmDialogMenu = true },
            onNextClick = { showConfirmDialogExit = true }
        )



        Column (
            modifier = modifier.fillMaxWidth()
               // .background(Color.Green)
            ,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.skore_label, mazeInfoViewModel.MazeInfo.value.skorenow),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )


                //Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(35.dp))

            ArrowPad(
                onUp = {
                    movePlayer(
                        gameViewModel = gameViewModel,
                        mazeInfoViewModel = mazeInfoViewModel,
                        smer = Smer.UP
                    )
                    //println("testup")
                },
                onDown = {
                    movePlayer(
                        gameViewModel = gameViewModel,
                        mazeInfoViewModel = mazeInfoViewModel,
                        smer = Smer.DOWN
                    )
                },
                onLeft = {
                    movePlayer(
                        gameViewModel = gameViewModel,
                        mazeInfoViewModel = mazeInfoViewModel,
                        smer = Smer.LEFT
                    )
                },
                onRight = {
                    movePlayer(
                        gameViewModel = gameViewModel,
                        mazeInfoViewModel = mazeInfoViewModel,
                        smer = Smer.RIGHT
                    )
                }
            )


        }
    }




    if (showConfirmDialogMenu) {
        GameMenuDialog(
            onDismiss = { showConfirmDialogMenu = false },
            onSaveClick = { showConfirmDialogSave = true },
            onLoadClick = { showConfirmDialogLoad = true },
            onHintClick = { showConfirmDialogHint = true },
            onToggleSound = {
                mazeInfoViewModel.setSounds(!mazeInfoViewModel.MazeInfo.value.sounds)
            },
            isSoundMuted = !mazeInfoViewModel.MazeInfo.value.sounds
        )
    }

    if (showConfirmDialogSave) {
        ConfirmCustomDialog(
            onConfirm = {showConfirmDialogSave = false
                gameViewModel.saveMaze(gameViewModel,mazeInfoViewModel,playerViewModel,mazeviewModel)},
            onDismiss = {showConfirmDialogSave = false},
            title = stringResource(R.string.savemenu),
            dialog = stringResource(R.string.savemenutext),
            confirm = stringResource(R.string.yes),
            dismis = stringResource(R.string.no)
        )
    }

    if (showConfirmDialogLoad) {
        ConfirmCustomDialog(
            onConfirm = {showConfirmDialogLoad = false
                gameViewModel.loadMaze(gameViewModel,mazeInfoViewModel,mazeviewModel,playerViewModel)},
            onDismiss = {showConfirmDialogLoad = false},
            title = stringResource(R.string.loadmenu),
            dialog = stringResource(R.string.loadmenutext),
            confirm = stringResource(R.string.yes),
            dismis = stringResource(R.string.no)
        )
    }

    if (showConfirmDialogHint) {
        val hintTile = 5
        val hintcost = (gameViewModel.Maze.value.width * gameViewModel.Maze.value.height) * 5 / 100
        ConfirmCustomDialog(
            onConfirm = {
                showConfirmDialogHint = false
                val currentScore = mazeInfoViewModel.MazeInfo.value.skorenow
                val newScore = currentScore - hintcost
                mazeInfoViewModel.updateMazeSkore(
                    mazeInfoViewModel.MazeInfo.value.copy(
                        skorenow = newScore
                    )
                )
                hint(gameViewModel = gameViewModel,
                    mazeInfoViewModel = mazeInfoViewModel,
                    counter = hintTile + 1)
                forceUpdateMaze(gameViewModel = gameViewModel)
            },
            onDismiss = {
                showConfirmDialogHint = false
            },
            title = stringResource(R.string.hintmenu),
            dialog = stringResource(R.string.hintmenutext,hintTile,hintcost),
            confirm = stringResource(R.string.yes),
            dismis = stringResource(R.string.no)
        )
    }

    if (showConfirmDialogExit) {
        ConfirmCustomDialog(
            onConfirm = {
                showConfirmDialogExit = false
                screenViewModel.setStage(1)
                navController.navigate(Screen.HOME.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = {
                showConfirmDialogExit = false
            },
            title = stringResource(R.string.exitToMenu),
            dialog = stringResource(R.string.realExitToMenu),
            confirm = stringResource(R.string.yes),
            dismis = stringResource(R.string.no)
        )
    }

    if (win || mazeInfoViewModel.MazeInfo.value.skorenow < 0) {
        val (stage, scoreToDisplay) = if (win) {
            R.string.endwin to mazeInfoViewModel.MazeInfo.value.skorenow
        } else {
            R.string.endlose to -500
        }


        if (!mazeInfo.zapisane) {
            val updatedPlayer = player.copy(
                games = player.games + 1,
                skore = player.skore + scoreToDisplay
            )
            playerViewModel.updatePlayer(updatedPlayer)
            val updatedItem = Item(
                id = updatedPlayer.id,
                name = updatedPlayer.name,
                skore = updatedPlayer.skore,
                games = updatedPlayer.games
            )
            viewModel.updateItem(updatedItem)
            mazeInfoViewModel.setZapisane(true)
        }

        GameResultDialog(
            playerViewModel = playerViewModel,
            resultTextRes = stage,
            scorenow = scoreToDisplay,
            onDismiss = { gameViewModel.resetMaze(mazeInfoViewModel)
                mazeInfoViewModel.setZapisane(false)
            }
        )
    }


}

/**
 * Custom tlacitka na UI(pouzite v horizontalnom screene) ktore su pouzite na rozsirene menu
 */
@Composable
fun ButtonsCollumGame(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Column (
        modifier = modifier
            //.fillMaxWidth()
          //  .background(Color.Yellow)
            .padding(horizontal = 16.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        val buttonModifier = Modifier
            .width(60.dp)
            .height(130.dp)

        Button(onClick = onBackClick,
            modifier = buttonModifier
        ) {
            Text(stringResource(R.string.gamemenuhorz))
        }
        Button(onClick = onNextClick,
            modifier = Modifier.width(60.dp).height(150.dp)
        ) {
            Text(stringResource(R.string.menuhorz))
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}