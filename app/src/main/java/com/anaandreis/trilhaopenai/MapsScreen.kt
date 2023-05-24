package com.anaandreis.trilhaopenai

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun MapsScreen(viewModel: SearchViewModel) {
    val context = LocalContext.current
    val mapMarkers = viewModel.markers
    val places = viewModel.places
    val cameraPositionState = CameraPositionState(
        CameraPosition(
            LatLng(-14.235, -51.9253), 5f, 0f, 0f
        )
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        uiSettings = MapUiSettings(zoomControlsEnabled = true),
        properties = MapProperties(mapType = MapType.NORMAL),
        cameraPositionState = cameraPositionState

    )
    {
        mapMarkers.forEachIndexed { index, latLng ->
            Marker(
                state = rememberMarkerState(position = LatLng(latLng.latitude, latLng.longitude)),
                title = places[index],
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
        }

        MapEffect(cameraPositionState) { map ->
                if (mapMarkers.isNotEmpty()) {
                    val newLatLng = LatLng(mapMarkers.first().latitude, mapMarkers.first().longitude)
                    val zoomLevel = 10f
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLatLng, zoomLevel)
                    map.animateCamera(cameraUpdate)
                }
            }
        }





    if (mapMarkers.isEmpty() || places.isEmpty()) {
        // Placeholder or loading state
        // You can show a loading indicator or a placeholder image until the map data is available
        // Replace this with your desired UI
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFF058475).copy(alpha = 0.5f))
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color= Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Mapeando suas trilhas...",
                        color = Color.White)

                }
            }
        )
        mapMarkers.forEachIndexed { index, latLng ->
            Log.d("FAILED MARKER", "Marker $index: $latLng")
        }
        places.forEachIndexed { index, String ->
            Log.d("FAILED PLACE", "Mplace $index: $places")
        }
    }
}




@Composable

fun MapsScreenPreview() {
    val viewModel: SearchViewModel = viewModel()
    MapsScreen(viewModel = viewModel)
}