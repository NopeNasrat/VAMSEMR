package com.example.vamsemr.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vamsemr.Navigation.ConfirmExitOnBackHandler
import com.example.vamsemr.Navigation.NavigationDestination
import com.example.vamsemr.R
import com.example.vamsemr.data.GameViewModel
import com.example.vamsemr.data.Maze
import com.example.vamsemr.data.MazeInfo
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.Player
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.data.ScreenViewModel
import com.example.vamsemr.ui.PlayerInfoSection
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.drawText
import kotlin.math.min
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.Navigation.Screen
import com.example.vamsemr.data.sql.MazeViewModel


object GameScreen : NavigationDestination {
    override val route = "GameScr"
    override val titleRes: Int = 0
}


@Composable
fun Game(
    viewModel: ItemViewModel,
    playerViewModel: PlayerViewModel,
    screenViewModel: ScreenViewModel,
    modifier: Modifier = Modifier,
    mazeInfoViewModel: MazeInfoViewModel,
    gameViewModel: GameViewModel,
    mazeviewModel: MazeViewModel,
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


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //PlayerInfoSection(player = player,mazeInfo = mazeInfo, modifier = Modifier.padding(top = 35.dp))
        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = stringResource(R.string.skore_label, mazeInfoViewModel.MazeInfo.value.skorenow),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        )


        MazeCanvas(
            maze = maze,
            modifier = Modifier
                .padding(top = 10.dp)
                //.background(Color.Magenta)
                .align(Alignment.CenterHorizontally),
            mazeInfoViewModel = mazeInfoViewModel
        )




        Spacer(modifier = Modifier.height(16.dp))

        ButtonsRowGame(
            onBackClick = { showConfirmDialogMenu = true},
            onNextClick = { showConfirmDialogExit = true }
        )

        Spacer(modifier = Modifier.height(30.dp))

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


                //gameViewModel.loadMaze(gameViewModel,mazeInfoViewModel,mazeviewModel,playerViewModel)
                //gameViewModel.saveMaze(gameViewModel,mazeInfoViewModel,playerViewModel,mazeviewModel)

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

                //gameViewModel.updateMaze(gameViewModel.Maze.value.copy())
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
/*
    if (showConfirmDialogExit) {
        ConfirmReturnToMenuDialog(
            onConfirm = {
                showConfirmDialogExit = false
                screenViewModel.setStage(1)
                navController.navigate(Screen.HOME.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            onDismiss = {
                showConfirmDialogExit = false
            }
        )
    }*/

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


@Composable
fun GameMenuDialog(
    onDismiss: () -> Unit,
    onSaveClick: () -> Unit,
    onLoadClick: () -> Unit,
    onHintClick: () -> Unit,
    onToggleSound: () -> Unit,
    isSoundMuted: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.realgamemenu), modifier = Modifier.weight(1f))
                IconButton(onClick = onToggleSound) {
                    Icon(
                        painter = if (isSoundMuted) painterResource(R.drawable.volume_off) else painterResource(R.drawable.volume_up),
                        contentDescription = if (isSoundMuted) stringResource(R.string.unmute) else stringResource(R.string.mute),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        },
        text = {
            Column {
                Button(
                    onClick = {
                        onSaveClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(stringResource(R.string.save))
                }

                Button(
                    onClick = {
                        onLoadClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(stringResource(R.string.load))
                }

                Button(
                    onClick = {
                        onHintClick()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(stringResource(R.string.hint))
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(stringResource(R.string.close))
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}


/*
@Composable
fun ConfirmHintDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    hintTile: Int,
    hintCost: Int
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.hintmenu)) },
        text = { Text(text = stringResource(R.string.hintmenutext,hintTile,hintCost)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.no))
            }
        }
    )
}*/

@Composable
fun ConfirmCustomDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    dialog: String,
    confirm: String,
    dismis: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = dialog) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = confirm)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = dismis)
            }
        }
    )
}


@Composable
fun GameResultDialog(
    playerViewModel: PlayerViewModel,
    resultTextRes: Int,
    scorenow: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(resultTextRes))
        },
        text = {
            Column {
                Text(text = stringResource(R.string.skore_label, scorenow))
                Text(text = stringResource(R.string.endwcelkoveskore, playerViewModel.player.value.skore - scorenow, scorenow))
                Text(text = stringResource(R.string.games_label, playerViewModel.player.value.games))
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.endnewgame))
            }
        }
    )
}

/*
@Composable
fun ConfirmReturnToMenuDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.exitToMenu)) },
        text = { Text(text = stringResource(R.string.realExitToMenu)) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.no))
            }
        }
    )
}*/


@Composable
fun ArrowPad(
    onUp: () -> Unit = {},
    onDown: () -> Unit = {},
    onLeft: () -> Unit = {},
    onRight: () -> Unit = {}
) {
    val arrowButtonSize = 64.dp
    val iconSize = 32.dp

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Spacer(modifier = Modifier.size(arrowButtonSize))
            IconButton(
                onClick = onUp,
                modifier = Modifier
                    .size(arrowButtonSize)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", modifier = Modifier.size(iconSize))
            }
            Spacer(modifier = Modifier.size(arrowButtonSize))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = onLeft,
                modifier = Modifier
                    .size(arrowButtonSize)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Left", modifier = Modifier.size(iconSize))
            }

            Spacer(modifier = Modifier.size(arrowButtonSize))

            IconButton(
                onClick = onRight,
                modifier = Modifier
                    .size(arrowButtonSize)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Right", modifier = Modifier.size(iconSize))
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Spacer(modifier = Modifier.size(arrowButtonSize))
            IconButton(
                onClick = onDown,
                modifier = Modifier
                    .size(arrowButtonSize)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", modifier = Modifier.size(iconSize))
            }
            Spacer(modifier = Modifier.size(arrowButtonSize))
        }
    }
}


@Composable
fun ButtonsRowGame(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onBackClick,modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.gamemenu))
        }
        Button(onClick = onNextClick,modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.menu))
        }
    }
}


@Composable
fun MazeCanvas(
    maze: Maze,
    mazeInfoViewModel: MazeInfoViewModel,
    modifier: Modifier = Modifier
) {
    val cellCountX = maze.height
    val cellCountY = maze.width
    val maxCells = maxOf(cellCountX, cellCountY)

    val finishText = stringResource(R.string.finishPic)
    val playerText = stringResource(R.string.playerPic)

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f) // štvorcový tvar
            .padding(16.dp)
    ) {
        val canvasSize = maxWidth.coerceAtMost(maxHeight)
        val cellSize = canvasSize / maxCells

        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSizePx = cellSize.toPx()
            val nativeCanvas = drawContext.canvas.nativeCanvas
            val wallWidth = cellSizePx * 0.1f

            fun drawCenteredText(text: String, centerX: Float, centerY: Float, textSize: Float) {
                val paint = android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    this.textSize = textSize
                    textAlign = android.graphics.Paint.Align.CENTER
                    isFakeBoldText = true
                }
                val textY = centerY - (paint.descent() + paint.ascent()) / 2
                nativeCanvas.drawText(text, centerX, textY, paint)
            }

            fun drawFinishCell(x: Int, y: Int) {
                val centerX = x * cellSizePx + cellSizePx / 2
                val centerY = y * cellSizePx + cellSizePx / 2
                val radius = cellSizePx * 0.4f

                drawCircle(Color.Yellow, radius, Offset(centerX, centerY))
                drawCenteredText(finishText, centerX, centerY, radius * 1.2f)
            }

            fun drawPlayerCell(x: Int, y: Int) {
                val centerX = x * cellSizePx + cellSizePx / 2
                val centerY = y * cellSizePx + cellSizePx / 2
                val radius = cellSizePx * 0.4f

                drawCircle(Color.Blue, radius, Offset(centerX, centerY))
                drawCenteredText(playerText, centerX, centerY, radius * 1.2f)
            }

            for (y in 0 until cellCountY) {
                for (x in 0 until cellCountX) {
                    val cell = maze.maze[y][x]

                    drawRect(
                        color = if (cell.hint > 0) Color.LightGray else Color.Gray,
                        topLeft = Offset(x * cellSizePx, y * cellSizePx),
                        size = Size(cellSizePx, cellSizePx)
                    )

                    if (cell.right && x != cellCountX - 1) {
                        drawRect(
                            Color.Blue,
                            topLeft = Offset((x + 1) * cellSizePx - wallWidth / 2f, y * cellSizePx),
                            size = Size(wallWidth, cellSizePx)
                        )
                    }

                    if (cell.bottom && y != cellCountY - 1) {
                        drawRect(
                            Color.Blue,
                            topLeft = Offset(x * cellSizePx, (y + 1) * cellSizePx - wallWidth / 2f),
                            size = Size(cellSizePx, wallWidth)
                        )
                    }

                    when {
                        cell.finish -> drawFinishCell(x, y)
                        cell.player -> drawPlayerCell(x, y)
                    }
                }
            }
        }
    }
}


@Composable
fun PlayerInfoSection(player: Player, mazeInfo: MazeInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = stringResource(R.string.playerinfotext), style = MaterialTheme.typography.bodyLarge)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 35.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.id_label, player.id),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.name_label, player.name),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.skore_label, player.skore),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.games_label, player.games),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("x : %d", mazeInfo.x),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = String.format("y : %d", mazeInfo.y),
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }
    }
}
