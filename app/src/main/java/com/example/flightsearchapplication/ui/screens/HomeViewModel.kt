package com.example.flightsearchapplication.ui.screens




import android.view.inputmethod.InlineSuggestion
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapplication.FlightSearchApplication
import com.example.flightsearchapplication.data.Airport
import com.example.flightsearchapplication.data.Favorite
import com.example.flightsearchapplication.data.FlightSearchRepository
import com.example.flightsearchapplication.data.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val listOfAirports: List<Airport> = listOf(),
    val suggestedAirports: List<Airport> = mutableListOf(),
    val listOfFavorites: List<Favorite> = mutableListOf(),
    val query: String = "",
    val isHomeScreen: Boolean = true
)

class HomeViewModel(private val flightSearchRepository: FlightSearchRepository, val userPreferenceRepository: UserPreferenceRepository): ViewModel() {


    val homeUiState = MutableStateFlow(HomeUiState())

    val listOfAirports: StateFlow<HomeUiState> = flightSearchRepository.getPopularItems()
        .map {
            HomeUiState(listOfAirports = it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    val listOfFavorites: StateFlow<HomeUiState> = flightSearchRepository.getFavorites()
        .map { HomeUiState(listOfFavorites = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

    fun suggestedAirports(): StateFlow<HomeUiState> = flightSearchRepository.getItem(homeUiState.value.query)
        .filterNotNull()
        .map { HomeUiState(suggestedAirports = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )


    fun updateQuery(query: String) {
        homeUiState.update {
            homeUiState ->  homeUiState.copy(
            query = query,
            )
        }
    }

    init {
        updateQuery("")
    }

    fun saveSearchedQuery(query: String) {
        viewModelScope.launch {
            userPreferenceRepository.saveAirport(query)
        }
        homeUiState.update {
            homeUiState -> homeUiState.copy(
            isHomeScreen = false
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApplication)
                val flightSearchRepository = application.container.flightSearchRepository
                val dataStore = application.userPreferenceRepository
                HomeViewModel(flightSearchRepository, dataStore)
            }
        }
    }
}
