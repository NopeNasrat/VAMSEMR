package com.example.vamsemr.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.vamsemr.Navigation.NavigationDestination
import com.example.vamsemr.R
import com.example.vamsemr.data.MazeInfo
import com.example.vamsemr.data.MazeInfoViewModel
import com.example.vamsemr.data.Player
import com.example.vamsemr.data.PlayerViewModel
import com.example.vamsemr.ui.PlayerInfoSection


object GameScreen : NavigationDestination {
    override val route = "GameScr"
    override val titleRes: Int = 0
}


@Composable
fun Game(playerViewModel: PlayerViewModel,
         modifier: Modifier = Modifier,
         mazeInfoViewModel: MazeInfoViewModel,
         navController: NavHostController
) {
    val player by playerViewModel.player
    val mazeInfo by mazeInfoViewModel.MazeInfo


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PlayerInfoSection(player = player,mazeInfo = mazeInfo, modifier = Modifier.padding(top = 35.dp))





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
