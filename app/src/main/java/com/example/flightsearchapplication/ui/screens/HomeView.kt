package com.example.flightsearchapplication.ui.screens



import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flightsearchapplication.R
import com.example.flightsearchapplication.navigation.FlightSearchNavHost
import com.example.flightsearchapplication.ui.screens.screen_modules.SuggestionList

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val homeUiState = viewModel.homeUiState.collectAsState()
    val navController = rememberNavController()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        SearchView(
            value = homeUiState.value.query,
            onSuggestionChanged = { viewModel.updateQuery(it) },
            viewModel = viewModel,
            navController = navController
        )
        FlightSearchNavHost(navController = navController, viewModel = viewModel)
    }
}

@Composable
fun SearchView(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onSuggestionChanged: (String) -> Unit,
    viewModel: HomeViewModel,
    navController: NavController
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier
            .padding(10.dp)
            .animateContentSize()
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val isFocussed by interactionSource.collectIsFocusedAsState()
        OutlinedTextField(
            value = value,
            onValueChange = {
                onSuggestionChanged(it)
            },
            singleLine = true,
            modifier = modifier
                .fillMaxWidth(),
            label = { Text("search") },
            leadingIcon = {
                Icon(
                    painter = painterResource(
                    id = R.drawable.search_fill0_wght400_grad0_opsz48),
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                )
            },
            interactionSource = interactionSource,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        if (isFocussed) {
            SuggestionList(viewModel = viewModel, navController = navController)
        }
    }
}

