package com.example.vamsemr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.Navigation.NavigationDestination
import com.example.vamsemr.Navigation.Screen
import com.example.vamsemr.R
import com.example.vamsemr.data.MazeInfo
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.Player
import com.example.vamsemr.data.PlayerViewModel
import kotlinx.coroutines.selects.select


object HomeDestinationvert2 : NavigationDestination {
    override val route = "homevert2"
    override val titleRes: Int = 0
}


@Composable
fun StartScreenVert2(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
    mazeInfoViewModel: MazeInfoViewModel,
    navController: NavHostController
) {
    val player by playerViewModel.player
    var selectedButton by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlayerInfoSection(player = player,modifier = Modifier.padding(top = 35.dp))



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            MazeSizeSection(selectedButton = selectedButton, onButtonClick = { selectedButton = it })
        }

        BottomButtonsRow(
            modifier = Modifier.padding(bottom = 30.dp),
            onBackClick = {
                navController.navigate(Screen.HOME.route) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNextClick = {
                if (selectedButton != null) {
                    selectedButton?.let { selected ->
                        val newMazeInfo = MazeInfo(
                            x = selected,
                            y = selected
                        )
                        mazeInfoViewModel.updateMazeInfo(newMazeInfo)
                    }
                    navController.navigate(Screen.GAME.route)
                }
            }
            //onNextClick = { navController.navigate(Screen.GAME.route) }
        )


    }
}


@Composable
fun PlayerInfoSection(player: Player, modifier: Modifier = Modifier) {
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
        }
    }
}



@Composable
fun MazeSizeSection(modifier: Modifier = Modifier,
                    selectedButton: Int?,
                    onButtonClick: (Int) -> Unit) {

    //var selectedButton by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.mazesizetext),
            style = MaterialTheme.typography.titleMedium
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val buttonModifier = Modifier
                .fillMaxWidth(0.6f)
                .height(60.dp)

            val small = 5
            val medium = 25
            val large = 50
            val huge = 100

            //  5x5
            Button(
                onClick = { onButtonClick(small) }, // Nastavenie hodnoty pri kliknutí
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == small) Color.Yellow else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string._5x5),
                    fontSize = 20.sp,
                    color = if (selectedButton == small) Color.Blue else Color.White // Modrý text, ak je tlačidlo vybrané
                )
            }

            //  25x25
            Button(
                onClick = { onButtonClick(medium) },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == medium) Color.Yellow else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string._25x25),
                    fontSize = 20.sp,
                    color = if (selectedButton == medium) Color.Blue else Color.White
                )
            }

            //  50x50
            Button(
                onClick = { onButtonClick(large) },
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == large) Color.Yellow else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string._50x50),
                    fontSize = 20.sp,
                    color = if (selectedButton == large) Color.Blue else Color.White
                )
            }

            //  100x100
            Button(
                onClick = { onButtonClick(huge)},
                modifier = buttonModifier,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == huge) Color.Yellow else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string._100x100),
                    fontSize = 20.sp,
                    color = if (selectedButton == huge) Color.Blue else Color.White
                )
            }
        }
    }
}




@Composable
fun BottomButtonsRow(
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
            Text(stringResource(R.string.back))
        }
        Button(onClick = onNextClick,modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.start))
        }
    }
}
