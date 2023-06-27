package com.example.flightsearchapplication.data


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceRepository(private val dataStore: DataStore<Preferences>) {
    private companion object{
        val AIRPORT = stringPreferencesKey("airport")
        const val TAG = "preference"
    }
    suspend fun saveAirport(airport: String) {
        dataStore.edit { preferences ->
            preferences[AIRPORT] = airport
        }
    }

    val airportSelected: Flow<String> = dataStore.data.map { preferences -> preferences[AIRPORT] ?: "" }

}