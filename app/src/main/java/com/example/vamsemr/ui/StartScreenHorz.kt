package com.example.vamsemr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inventory.data.Item
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.Navigation.ConfirmExitOnBackHandler
import com.example.vamsemr.Navigation.NavigationDestination
import com.example.vamsemr.Navigation.Screen
import com.example.vamsemr.R
import com.example.vamsemr.data.MazeInfo
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.Player
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.data.ScreenViewModel


object HomeDestinationHorz : NavigationDestination {
    override val route = "homehorz"
    override val titleRes: Int = 0
}

@Composable
fun StartScreenHorz (
    screenViewModel: ScreenViewModel,
    viewModel: ItemViewModel,
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    mazeInfoViewModel: MazeInfoViewModel
){

    ConfirmExitOnBackHandler {
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    var selectedItemId by remember { mutableStateOf<Int?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var isRemoveDialogOpen by remember { mutableStateOf(false) }

    val selectedItem by viewModel.getItemById(selectedItemId ?: -1).collectAsState(initial = null)
    val player by playerViewModel.player

    var selectedButton by remember { mutableStateOf<Int?>(null) }
    val mazeInfo by mazeInfoViewModel.MazeInfo

    Row(modifier = modifier.fillMaxSize()) {
        Box(    //PLAYERS
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            ScrollableBoxSelectionHorz(
                viewModel,
                modifier = Modifier.fillMaxSize(),
                selectedItemId = selectedItemId,
                onItemSelected = { selectedItemId = it }
            )
        }



        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .background(Color.LightGray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)  // horná časť
            ) {
                TopButtonHorz(
                    onAddClick = { isDialogOpen = true },
                    onRemoveClick = { isRemoveDialogOpen = true }
                )

                PlayerInfoSection(player = player, modifier = Modifier.padding(top = 35.dp))


                Text(text = stringResource(id = R.string.selectmazesizetext, mazeInfo.x, mazeInfo.y),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 15.dp))
                Text(text = "${mazeInfo.x} x ${mazeInfo.y}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally))


            }
            BottomButton(
                buttontext = stringResource(R.string.start),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                onNextClick = {
                    if (selectedItem != null && (selectedButton != null || mazeInfo.x != 0)) {
                        selectedItem?.let { item ->
                            playerViewModel.updatePlayer(
                                player.copy(
                                    id = item.id,
                                    name = item.name,
                                    skore = item.skore,
                                    games = item.games
                                )
                            )
                        }
                        screenViewModel.setStage(2)
                        navController.navigate(Screen.GAMEHORZ.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }

                    }
                }
            )

        }






        Box(    //games types
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp)
        ) {

            MazeSizeSection(
                selectedButton = selectedButton,
                onButtonClick = { selectedButton = it },
                buttonModifier = Modifier.fillMaxWidth().height(60.dp)
            )



        }
    }

    if (isDialogOpen) {
        AddUserDialog(
            onConfirm = { name ->
                viewModel.addItem(name = name, skore = 0, games = 0)

                //println("Pridaný používateľ: $name")
                isDialogOpen = false
            },
            onDismiss = { isDialogOpen = false }
        )
    }
    // Odstránenie používateľa
    if (isRemoveDialogOpen) {
        RemoveUserDialog(
            onConfirm = { id ->
                viewModel.deleteItemById(id)
                isRemoveDialogOpen = false
            },
            onDismiss = { isRemoveDialogOpen = false }
        )
    }

    if (selectedItem != null) {
        val item = selectedItem!!
        val isDifferent = player.id != item.id ||
                player.name != item.name ||
                player.skore != item.skore ||
                player.games != item.games

        if (isDifferent) {
            playerViewModel.updatePlayer(
                player.copy(
                    id = item.id,
                    name = item.name,
                    skore = item.skore,
                    games = item.games
                )
            )
        }
    } else {
        playerViewModel.updatePlayer(Player(id = -1, name = "", skore = 0, games = 0)
        )
    }

    if (selectedButton != null) {
        selectedButton?.let { selected ->
            val newMazeInfo = MazeInfo(
                x = selected,
                y = selected
            )
            mazeInfoViewModel.updateMazeInfo(newMazeInfo)
        }
    }
}







@Composable
fun TopButtonHorz(onAddClick: () -> Unit, onRemoveClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
        //horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.pridajhraca))
        }
        Button(
            onClick = onRemoveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.removeuser))
        }
    }
}






@Composable
fun ScrollableBoxSelectionHorz(
    viewModel: ItemViewModel,
    modifier: Modifier = Modifier,
    selectedItemId: Int?,
    onItemSelected: (Int) -> Unit
) {
    val items by viewModel.getAllItems().collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SelectableBoxHorz(
                item = item,
                isSelected = selectedItemId == item.id,
                onClick = { clickedItem ->
                    onItemSelected(clickedItem.id)
                }
            )
        }
    }
}



@Composable
fun SelectableBoxHorz(
    item: Item,
    isSelected: Boolean,
    onClick: (Item) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(80.dp)
            .background(if (isSelected) Color.Yellow else Color.LightGray)
            .padding(16.dp)
            .clickable {
                onClick(item)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(id = R.string.id_label, item.id), style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(id = R.string.name_label, item.name), style = MaterialTheme.typography.bodyLarge)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(id = R.string.skore_label, item.skore), style = MaterialTheme.typography.bodyLarge)
                Text(text = stringResource(id = R.string.games_label, item.games), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}