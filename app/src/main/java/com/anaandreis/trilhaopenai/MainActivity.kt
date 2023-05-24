package com.anaandreis.trilhaopenai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.anaandreis.trilhaopenai.ui.theme.TrilhaOpenAITheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrilhaOpenAITheme {

                App()

            }
        }

    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel: SearchViewModel = viewModel()

    SetupNavGraph(navController = navController, viewModel = viewModel)
}


