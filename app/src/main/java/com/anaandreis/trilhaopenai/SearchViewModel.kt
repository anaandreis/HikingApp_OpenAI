package com.anaandreis.trilhaopenai

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anaandreis.minhastrilhas.data.SearchData
import com.anaandreis.trilhaopenai.data.models.ApiClient
import com.anaandreis.trilhaopenai.data.models.CompletionRequest
import com.anaandreis.trilhaopenai.data.models.CompletionResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import java.net.SocketTimeoutException

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private var message = ""

    private val _answer = MutableStateFlow("initial value")
    val answer: StateFlow<String> = _answer

    private val _markers = mutableStateListOf<LatLng>()
    val markers: MutableList<LatLng> = _markers

    private var _places = mutableStateListOf<String>()
    val places: MutableList<String> = _places

    private val _isMarkersLoaded = MutableStateFlow(false)
    val isMarkersLoaded: StateFlow<Boolean> = _isMarkersLoaded.asStateFlow()

    private var _isPlacesLoaded = MutableStateFlow(false)
    val isPlacesLoaded: StateFlow<Boolean> = _isPlacesLoaded.asStateFlow()


    fun createSearchMessage(where: String, difficulty: String, Goal: String) {
        resetSearch()
        message = "Me passe uma lista de cinco trilhas, separadas por vírgulas, (sem nenhuma outra informação, introdução, numeração ou conclusao em sua resposta) somente com o nome de trilhas em ${where} de nível de dificuldade ${difficulty} e que são boas para ${Goal}."
        Log.d("SEARCH", "$message")

        callApi(message)

    }

    fun addResponse(response: String){
        _answer.value = response
        getCoordenates(response)
    }

    fun callApi(question: String){

        val completionRequest = CompletionRequest(
            model = "text-davinci-003",
            prompt = question,
            max_tokens = 4000
        )


        viewModelScope.launch {
            try {
                val response =  withTimeout(10000) { ApiClient.apiService.getCompletions(completionRequest) }
                handleApiResponse(response)
                Log.d("TRY", "GOT HERE")
            }catch (e: SocketTimeoutException){
                addResponse("Timeout :  $e")
                Log.d("CATCH", "GOT HERE")
            }
        }
    }


    private suspend fun handleApiResponse(response: Response<CompletionResponse>) {
        withContext(Dispatchers.Main) {
            Log.d("HANDLE API", "GOT HERE")
            Log.d("ANSWER", answer.value)
            Log.d("API_RESPONSE", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                response.body()?.let { completionResponse ->
                    val result = completionResponse.choices.firstOrNull()?.text
                    if (result != null) {
                        addResponse(result.trim())
                        Log.d("HANDLE API", "$result")
                    } else {
                        addResponse("No choices found")
                        Log.d("HANDLE API", "NULL")
                    }
                }
            } else {
                addResponse("Failed to get response ${response.errorBody()}")
                Log.d("HANDLE API", "FAILED")
            }
        }
    }

    fun getCoordenates(answer: String) {

        val geocoder = Geocoder(getApplication())
        val placesList = answer.split(",")
        _places += placesList
        Log.d("COORDENATES1", "$placesList")


        viewModelScope.launch(Dispatchers.IO) {
            val updatedMarkers = mutableListOf<LatLng>()
            for (location in placesList) {
                val result = geocoder.getFromLocationName(location, 1)
                if (result != null && result.isNotEmpty()) {
                    val latitude = result[0].latitude
                    val longitude = result[0].longitude
                    updatedMarkers.add(LatLng(latitude, longitude))

                }

            }
            // Update the markers list
            _markers.clear()
            _markers += updatedMarkers

            // Set the isMarkersLoaded and isPlacesLoaded flags
            _isMarkersLoaded.value = true
            _isPlacesLoaded.value = true
        }
        }

    fun resetSearch() {
        _places.clear()
        _markers.clear()
        _isMarkersLoaded.value = false
        _isPlacesLoaded.value = false
    }
    }




