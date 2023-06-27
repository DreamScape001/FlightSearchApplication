package com.example.flightsearchapplication.data

import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    suspend fun insertItem(favorite: Favorite)
    suspend fun deleteItem(favorite: Favorite)
    fun getItem(name: String?): Flow<List<Airport>>
    fun getPopularItems(): Flow<List<Airport>>
    fun getOthers(name: String): Flow<List<Airport>>
    fun getSearched(name: String): Flow<Airport>
    fun getFavorites(): Flow<List<Favorite>>
}

class OfflineSearchRepository(private val flightItemDao: FlightItemDao): FlightSearchRepository {
    override suspend fun insertItem(favorite: Favorite) = flightItemDao.insertFavorite(favorite)

    override suspend fun deleteItem(favorite: Favorite) = flightItemDao.deleteFavorite(favorite)

    override fun getItem(name: String?): Flow<List<Airport>> = flightItemDao.getAirport(name)

    override fun getPopularItems(): Flow<List<Airport>> = flightItemDao.getPopularFlights()

    override fun getOthers(name: String): Flow<List<Airport>> = flightItemDao.getOthers(name)

    override fun getSearched(name: String): Flow<Airport> = flightItemDao.getSearched(name)

    override fun getFavorites(): Flow<List<Favorite>> = flightItemDao.getFavorites()
}