package com.example.flightsearchapplication.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Favorite::class)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete(entity = Favorite::class)
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * from favorite")
    fun getFavorites(): Flow<List<Favorite>>

    @Query("SELECT * from airport WHERE name LIKE :name || '%'  OR iata_code LIKE :name || '%' LIMIT 8 ")
    fun getAirport(name: String?): Flow<List<Airport>>

    @Query("SELECT * from airport ORDER BY passengers DESC LIMIT 3")
    fun getPopularFlights(): Flow<List<Airport>>

    @Query("SELECT * from airport WHERE iata_code = :name ORDER BY passengers DESC")
    fun getSearched(name: String): Flow<Airport>

    @Query("SELECT * from airport WHERE NOT iata_code = :name ORDER BY passengers DESC")
    fun getOthers(name: String): Flow<List<Airport>>
}