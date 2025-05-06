package com.example.vamsemr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.inventory.data.Item
import com.example.inventory.ui.ItemViewModel
import com.example.vamsemr.R


@Composable
fun MainScreenV1(viewModel: ItemViewModel, modifier: Modifier = Modifier) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var isRemoveDialogOpen by remember { mutableStateOf(false) }
    var selectedItemId by remember { mutableStateOf<Int?>(null) }

    val selectedItem by viewModel.getItemById(selectedItemId ?: -1).collectAsState(initial = null)


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(top = 20.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopButton(
            onAddClick = { isDialogOpen = true },
            onRemoveClick = { isRemoveDialogOpen = true }
        )

        ScrollableBoxSelection(
            viewModel,
            modifier = Modifier.weight(1f),
            selectedItemId = selectedItemId,
            onItemSelected = { selectedItemId = it }
        )

        BottomButton(modifier = Modifier,
            onNextClick = {
                if (selectedItem != null) {
                    println("Selected item: ${selectedItem?.id}")
                } else {
                    println("No item selected or item removed")
                }
            }
        )
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

}

@Composable
fun TopButton(onAddClick: () -> Unit, onRemoveClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = onAddClick) {
            Text(text = stringResource(R.string.pridajhraca))
        }
        Button(onClick = onRemoveClick) {
            Text(text = stringResource(R.string.removeuser))
        }
    }
}


@Composable
fun AddUserDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.pridajhraca)) },
        text = {
            TextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(stringResource(R.string.menohraca)) }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (userName.isNotBlank()) {
                        onConfirm(userName)
                    }
                }
            ) {
                Text(stringResource(R.string.pridat))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.zrusit))
            }
        }
    )
}

@Composable
fun RemoveUserDialog(
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var userId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.removeuser)) },
        text = {
            TextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text(stringResource(R.string.enter_id)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions.Default
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (userId.isNotBlank()) {
                        try {
                            onConfirm(userId.toInt())
                        } catch (e: NumberFormatException) {
                            println((R.string.error_id))
                        }

                    }
                }
            ) {
                Text(stringResource(R.string.remove))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.zrusit))
            }
        }
    )
}

@Composable
fun ScrollableBoxSelection(
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
            SelectableBox(
                item = item,
                isSelected = selectedItemId == item.id, // Určujeme, či je vybraný
                onClick = { clickedItem ->
                    onItemSelected(clickedItem.id) // Vyberieme aktuálny item
                }
            )
        }
    }
}


@Composable
fun SelectableBox(
    item: Item,
    isSelected: Boolean,
    onClick: (Item) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(80.dp)
            .background(if (isSelected) Color.Yellow else Color.LightGray) // Ak je vybraný, nastaví sa modrá farba
            .padding(16.dp)
            .clickable {
                onClick(item) // Volanie callbacku pre rodiča, ktorý zmení stav
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






@Composable
fun BottomButton(modifier: Modifier = Modifier, onNextClick: () -> Unit) {
    Button(
        onClick = onNextClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(stringResource(R.string.next))
    }
}