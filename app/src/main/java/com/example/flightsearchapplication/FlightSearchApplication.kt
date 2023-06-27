package com.example.flightsearchapplication

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapplication.data.AppContainer
import com.example.flightsearchapplication.data.DefaultAppContainer
import com.example.flightsearchapplication.data.UserPreferenceRepository


private const val AIRPORT_REFERENCE_NAME = "airport"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(AIRPORT_REFERENCE_NAME)
class FlightSearchApplication: Application() {
    lateinit var container: AppContainer
    lateinit var userPreferenceRepository: UserPreferenceRepository
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        userPreferenceRepository = UserPreferenceRepository(dataStore = dataStore)
    }
}