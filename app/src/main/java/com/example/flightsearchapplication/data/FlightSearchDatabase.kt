package com.example.flightsearchapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favorite::class], version = 2, exportSchema = false)
abstract class FlightSearchDatabase: RoomDatabase() {
    abstract fun flightItemDao(): FlightItemDao
    companion object {
        fun getDatabase(context: Context): FlightSearchDatabase {
            return Room.databaseBuilder(
                context,
                FlightSearchDatabase::class.java,
                "flight_app"
            )
                .createFromAsset("database/flight_search.db")
                .build()
        }
    }
}