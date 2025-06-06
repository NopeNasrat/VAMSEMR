package com.example.vamsemr.Navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.example.vamsemr.R

/**
 * Dialog na ukoncenie appky
 *
 * @param title title na sprave o ukonceny appky
 * @param message message o ukonceny appky
 * @param onConfirmExit lambda funkia pri ukonceny appky
 *
 * @author Bc. Fabo Peter
 */
@Composable
fun ConfirmExitOnBackHandler(
    title: String = stringResource(R.string.exitapp),
    message: String = stringResource(R.string.realexitapp),
    onConfirmExit: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    BackHandler {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(title) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onConfirmExit()
                }) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}
