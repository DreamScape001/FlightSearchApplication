@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.flightsearchapplication.ui.screens.screen_modules


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapplication.R
import com.example.flightsearchapplication.data.Airport
import com.example.flightsearchapplication.data.Favorite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun QueryResult(
    viewModel: QueryViewModel = viewModel(factory = QueryViewModel.Factory),
    navigateUp: () -> Unit
) {
    val queryUiState = viewModel.getSelectedAirport.collectAsState()
    val getOthers = viewModel.getOthers.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row {
                        Text(text = "Flights from ${queryUiState.value.iataCode}")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { it ->
        if (viewModel.isNull) {
            ErrorScreen(modifier = Modifier.padding(it), name = queryUiState.value.iataCode)
        } else {
            viewModel.changeIsNull()
            AnimatedVisibility(
                visible = true,
                enter = expandVertically {
                    40
                }
            ) {

            }
            FlightColumn(modifier =  Modifier.padding(it), airports = getOthers.value.otherResults, name = queryUiState.value, viewModel = viewModel)
        }
    }

}

@Composable
fun FlightColumn(
    modifier: Modifier = Modifier,
    airports: List<Airport>,
    name: QueryUiState,
    viewModel: QueryViewModel
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
    ) {
        items(items = airports, key = {it -> it.id}) {
                airport -> AirportListCard(airport = airport, name = name, viewModel = viewModel)
        }
    }
}

@Composable
fun AirportListCard(modifier: Modifier = Modifier, airport: Airport, name: QueryUiState, viewModel: QueryViewModel) {
    val listOfFavorites by viewModel.listOfFavorites.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var liked by rememberSaveable() {
        mutableStateOf(false)
    }
    var favorite = Favorite(departureCode = name.iataCode, destinationCode = airport.iataCode)

    for (i in listOfFavorites.listOfFavorites) {
        if (favorite.departureCode == i.departureCode && favorite.destinationCode == i.destinationCode) {
            liked = true
            favorite = favorite.copy(
                id = i.id
            )
            break
        }
    }

    var tum by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {

        Row {
            Box(
                modifier = modifier
                    .height(165.dp)
                    .weight(1f)
            ) {

                Column(modifier = modifier
                    .padding(15.dp)
                    .fillMaxSize()
                ) {
                    Spacer(modifier = modifier.height(5.dp))
                    Text(text = "DEPART", fontSize = 14.sp)
                    Spacer(modifier = modifier.height(5.dp))
                    Row {
                        Text(text = name.iataCode, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = modifier.width(5.dp))
                        Text(text = name.name, fontWeight = FontWeight.Light, fontSize = 14.sp)
                    }
                    Spacer(modifier = modifier.height(5.dp))
                    Text(text = "ARRIVE", fontSize = 14.sp)
                    Spacer(modifier = modifier.height(5.dp))
                    Row {
                        Text(text = airport.iataCode, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = modifier.width(5.dp))
                        Text(text = airport.name, fontWeight = FontWeight.Light, fontSize = 14.sp)
                    }
                }
            }
            val transition = updateTransition(targetState = tum, label = "pressed state")
            val size by transition.animateDp(
                label = "size",
                transitionSpec = {
                    tween(300)
                }
            ) { state ->
                when (state) {
                    true -> 20.dp
                    false -> 28.dp
                }
            }
            Column {
                Spacer(modifier = modifier.height(10.dp))
                Icon(
                    modifier = modifier
                        .size(size)
                        .padding(
                            start = 2.dp,
                            end = 2.dp
                        )
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            if (!liked) {
                                coroutineScope.launch {
                                    tum = true
                                    viewModel.insertFavorites(favorite)
                                    delay(300)
                                    tum = false
                                }
                            } else {
                                coroutineScope.launch {
                                    tum = true
                                    viewModel.deleteFavorites(favorite)
                                    delay(300)
                                    tum = false
                                }
                            }

                            liked = !liked
                        },
                    painter = painterResource(id = when (liked) {
                        true -> R.drawable.baseline_favorite_24
                        false -> R.drawable.baseline_favorite_border_24
                    }),
                    contentDescription = null
                )
                Spacer(modifier = modifier.width(30.dp))
            }
        }
            
    }
}

@Composable
fun ErrorScreen(name: String, modifier: Modifier = Modifier) {
    Text(modifier = modifier, text = "$name doesn't exist")
}