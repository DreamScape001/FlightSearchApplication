package com.example.flightsearchapplication.ui.screens.screen_modules


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flightsearchapplication.data.Airport
import com.example.flightsearchapplication.data.Favorite
import com.example.flightsearchapplication.ui.screens.HomeViewModel

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val listOfAirports = viewModel.listOfAirports.collectAsState()
    val listOfFavorites = viewModel.listOfFavorites.collectAsState()
    Column(modifier = modifier.fillMaxSize() ) {
        Spacer(modifier = modifier.height(10.dp))
        Text(
            text = "Popular",
            modifier = modifier.padding(start = 20.dp, top = 20.dp, bottom = 5.dp),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )

        PopularAirportList(airports = listOfAirports.value.listOfAirports)

        Text(
            text = "Favorites",
            modifier = modifier.padding(start = 20.dp, bottom = 5.dp),
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Bold
        )

        if (listOfFavorites.value.listOfFavorites.isNotEmpty()) {
            FavoriteAirportList(airports = listOfFavorites.value.listOfFavorites)
        } else {
            Text(text = "No Favorites", fontSize = 16.sp, modifier = modifier.padding(start = 20.dp))
        }

    }
}

@Composable
fun AirportCard(modifier: Modifier = Modifier, airport: Airport) {

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(
            modifier = modifier
                .height(165.dp)
                .width(300.dp)
        ) {
            Column(
                modifier = modifier
                    .padding(15.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = modifier.weight(1f))
                Text(text = airport.iataCode, fontSize = 17.5.sp, fontWeight = FontWeight.Bold)
                Text(text = airport.name, fontSize = 14.sp)
                Spacer(modifier = modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun SuggestedText(modifier: Modifier = Modifier, airport: Airport, viewModel: HomeViewModel, navController: NavController) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                viewModel.saveSearchedQuery(airport.iataCode)
                viewModel.updateQuery(airport.iataCode)
                navController.navigate("query/${airport.iataCode}") {
                    popUpTo("homeBody")
                }
                focusManager.clearFocus()
            }
    ) {
        Text(text = airport.iataCode, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = modifier.width(3.dp))
        Text(text = airport.name, fontSize = 13.sp)
    }
}

@Composable
fun PopularAirportList(airports: List<Airport>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        items(items = airports, key = { it -> it.id} ) {
                airport -> AirportCard(airport = airport)
        }
    }
}

@Composable
fun SuggestionList(viewModel: HomeViewModel, navController: NavController) {
    val airports = viewModel.suggestedAirports().collectAsState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        items(items = airports.value.suggestedAirports, key = { it -> it.id} ) {
            airport -> SuggestedText(airport = airport, viewModel = viewModel, navController = navController)
        }
    }
}

@Composable
fun FavoriteCard(modifier: Modifier = Modifier, airport: Favorite) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Box(
            modifier = modifier
                .height(165.dp)
                .width(300.dp)
        ) {

            Column(
                modifier = modifier
                    .padding(15.dp)
                    .fillMaxSize()
            ) {
                Spacer(modifier = modifier.weight(1f))
                Text(text = "Depart", fontSize = 17.5.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = airport.departureCode, fontSize = 14.sp)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = "Arrive", fontSize = 17.5.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = airport.destinationCode, fontSize = 14.sp)
            }
        }

    }
}


@Composable
fun FavoriteAirportList(airports: List<Favorite>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp)
    ) {
        items(items = airports, key = { it -> it.id} ) {
                airport -> FavoriteCard(airport = airport)
        }
    }
}
