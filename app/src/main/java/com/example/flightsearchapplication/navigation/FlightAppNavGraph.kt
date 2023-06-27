package com.example.flightsearchapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flightsearchapplication.ui.screens.HomeViewModel
import com.example.flightsearchapplication.ui.screens.screen_modules.HomeBody
import com.example.flightsearchapplication.ui.screens.screen_modules.QueryResult


@Composable
fun FlightSearchNavHost(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
   NavHost(navController = navController, startDestination = "homeBody"){
       composable("homeBody") {
           HomeBody(
               viewModel = viewModel
           )
       }
       composable("query/{iataCode}") {
           QueryResult(navigateUp = { navController.popBackStack()})
       }
   }
}