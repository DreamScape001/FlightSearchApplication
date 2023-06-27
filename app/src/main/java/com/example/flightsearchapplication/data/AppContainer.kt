package com.example.flightsearchapplication.data

import android.content.Context

interface AppContainer {
    val flightSearchRepository: FlightSearchRepository
}

class DefaultAppContainer(context: Context): AppContainer {
    private val database = FlightSearchDatabase.getDatabase(context).flightItemDao()
    override val flightSearchRepository: FlightSearchRepository by lazy {
        OfflineSearchRepository(database)
    }
}
