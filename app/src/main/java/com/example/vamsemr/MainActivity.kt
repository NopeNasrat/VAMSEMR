package com.example.vamsemr

import android.app.Application
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
import androidx.navigation.compose.rememberNavController
import com.example.inventory.data.AppContainer
import com.example.inventory.data.AppDataContainer
import com.example.inventory.ui.ItemViewModel
import com.example.inventory.ui.ItemViewModelFactory
import com.example.vamsemr.Navigation.VamsemrNavHost
import com.example.vamsemr.data.sql.MazeViewModel
import com.example.vamsemr.data.sql.MazeViewModelFactory
import com.example.vamsemr.ui.theme.VAMSEMRTheme
import com.example.vamsemr.ui.*
/**
 * Hra Bludisko,
 * hrac musi najst cestu k cielu, aby hru vyhral
 *
 * @author Bc. Fabo Peter
 */
class MainActivity : ComponentActivity() {
/*
    private val viewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(AppDataContainer(applicationContext).itemsRepository)
    }*/

    private val itemviewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application as MyApplication).container.itemsRepository)
    }

    private val mazeViewModel: MazeViewModel by viewModels {
        MazeViewModelFactory((application as MyApplication).container.mazesRepository)
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            VAMSEMRTheme {

                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    /*
                    Greeting(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )*/

                    VamsemrNavHost(
                        navController = navController,
                        viewModel = itemviewModel,
                        mazeviewModel = mazeViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


/*

@Composable
fun Greeting(viewModel: ItemViewModel, modifier: Modifier = Modifier) {
    MainScreenV1(viewModel = viewModel ,modifier);
}*/

class MyApplication : Application() {
    // Lazy inicializácia kontajnera (iba raz pri spustení aplikácie)
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}