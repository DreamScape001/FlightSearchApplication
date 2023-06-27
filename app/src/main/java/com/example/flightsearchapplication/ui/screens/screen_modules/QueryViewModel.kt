package com.example.flightsearchapplication.ui.screens.screen_modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapplication.FlightSearchApplication
import com.example.flightsearchapplication.data.Airport
import com.example.flightsearchapplication.data.Favorite
import com.example.flightsearchapplication.data.FlightSearchRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class QueryUiState(
    val id: Int = 0,
    val iataCode: String = "",
    val name: String = "",
    val passengers: Int = 0,
    val otherResults: List<Airport> = mutableListOf(),
    val listOfFavorites: List<Favorite> = mutableListOf()
)

fun Airport.toQueryUiState(): QueryUiState = QueryUiState(
        id = this.id,
        iataCode = this.iataCode,
        name = this.name,
        passengers = this.passengers
    )



class QueryViewModel(
    savedStateHandle: SavedStateHandle,
    private val flightSearchRepository: FlightSearchRepository
    ): ViewModel() {

    var isNull by mutableStateOf(false)
        private set

    fun changeIsNull() {
        isNull = false
    }

    private val iataCode: String = checkNotNull(savedStateHandle["iataCode"])

    val getOthers: StateFlow<QueryUiState> = flightSearchRepository.getOthers(iataCode)
        .filterNotNull().catch { isNull = true }
        .map { QueryUiState(otherResults = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QueryUiState()
        )

    val getSelectedAirport: StateFlow<QueryUiState> = flightSearchRepository.getSearched(iataCode)
        .filterNotNull().catch {  isNull = true }
        .map { it.toQueryUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QueryUiState()
        )

    val listOfFavorites: StateFlow<QueryUiState> = flightSearchRepository.getFavorites()
        .map { QueryUiState(listOfFavorites = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QueryUiState()
        )

    suspend fun insertFavorites(favorite: Favorite) {
        flightSearchRepository.insertItem(favorite)
    }

    suspend fun deleteFavorites(favorite: Favorite) {
        flightSearchRepository.deleteItem(favorite)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)
                val flightSearchRepository = application.container.flightSearchRepository
                QueryViewModel(this.createSavedStateHandle(), flightSearchRepository)
            }
        }
    }
}