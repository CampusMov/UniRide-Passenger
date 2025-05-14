package com.campusmov.uniride.presentation.views.routingmatching.searchplace

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.google.android.libraries.places.api.model.Place

@Composable
fun SearchPlaceView(
    viewModel: SearchPlaceViewModel = hiltViewModel(),
    navHostController: NavHostController,
    onDismissRequest: () -> Unit,
    onPlaceSelected: (Place) -> Unit,
){
    val placePredictions = viewModel.placePredictions.collectAsState()

    var searchQuery = remember {
        mutableStateOf("")
    }
    val searchQueryState = remember {
        mutableStateOf("")
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.9f)
                .background(Color.Black)
        ) {
            DefaultRoundedInputField(
                modifier = Modifier.
                fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = searchQuery.value,
                onValueChange = {
                    searchQuery.value = it
                    searchQueryState.value = it
                },
                placeholder = "Seleccionar punto de recogida"
            )
            LazyColumn {
                items(placePredictions.value) { prediction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = prediction.fullText,
                            fontSize = 17.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.getPlaceDetails(prediction.id) { place ->
                                        onPlaceSelected(place)
                                    }
                                }
                        )
                    }

                }
            }
        }
    }
}