package com.example.vamsemr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vamsemr.R


@Composable
fun MainScreenV1(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
            .padding(top = 20.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopButton()

        ScrollableBoxSelection(modifier = Modifier.weight(1f))

        BottomButton(modifier = Modifier)
    }
}

@Composable
fun TopButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { /* akcia: pridať */ }) {
            Text(text = stringResource(R.string.adduser))
        }
        Button(onClick = { /* akcia: odstrániť */ }) {
            Text(text = stringResource(R.string.removeuser))
        }
    }
}


@Composable
fun ScrollableBoxSelection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) { index ->  // viac boxov, aby si videl scroll
            SelectableBox("Box #${index + 1}")
        }
    }
}

@Composable
fun SelectableBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(50.dp)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Composable
fun BottomButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* akcia dole */ },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(stringResource(R.string.next))
    }
}

