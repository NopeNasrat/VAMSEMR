package com.example.vamsemr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.data.AppDataContainer
import com.example.inventory.ui.ItemViewModel
import com.example.inventory.ui.ItemViewModelFactory
import com.example.vamsemr.ui.theme.VAMSEMRTheme
import com.example.vamsemr.ui.*

class MainActivity : ComponentActivity() {

    private val viewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(AppDataContainer(applicationContext).itemsRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            VAMSEMRTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}




@Composable
fun Greeting(viewModel: ItemViewModel, modifier: Modifier = Modifier) {
    MainScreenV1(viewModel = viewModel ,modifier);
}
