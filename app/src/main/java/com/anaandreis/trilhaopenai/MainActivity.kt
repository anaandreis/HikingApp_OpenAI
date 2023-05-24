package com.anaandreis.trilhaopenai

import android.annotation.SuppressLint
import android.inputmethodservice.Keyboard
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anaandreis.trilhaopenai.ui.theme.TrilhaOpenAITheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}



@Composable
fun ResultsScreen() {
    val viewModel: SearchViewModel = viewModel()
    val answer: String by viewModel.answer.collectAsState()
    // Use the data in your UI
    Text(text = answer)
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun MapTest() {
    val context = LocalContext.current
    val geocoder = Geocoder(context)
    var addressList by remember { mutableStateOf<List<Address>?>(null) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val editTextValue = remember { mutableStateOf("") }
        val textViewValue = remember { mutableStateOf("") }

        OutlinedTextField(
            value = editTextValue.value,
            onValueChange = { editTextValue.value = it },
            label = { Text("Enter location") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Launch a coroutine in the IO context
                CoroutineScope(Dispatchers.IO).launch {
                    val result = geocoder.getFromLocationName(editTextValue.value, 1)
                    withContext(Dispatchers.Main) {
                        if (result != null && result.isNotEmpty()) {
                            val latitude = result[0].latitude
                            val longitude = result[0].longitude
                            textViewValue.value = "Latitude: $latitude | Longitude: $longitude"
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Get Coordinates")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(textViewValue.value)
    }
}

