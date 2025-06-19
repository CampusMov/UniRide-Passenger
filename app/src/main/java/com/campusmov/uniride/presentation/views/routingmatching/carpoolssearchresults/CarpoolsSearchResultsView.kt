package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.components.CarpoolInfoCard
import com.campusmov.uniride.presentation.views.routingmatching.searchcarpool.SearchCarpoolViewModel
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceViewModel

@Composable
fun CarpoolsSearchResultsView(
    navHostController: NavHostController,
    viewModel: CarpoolsSearchResultsViewModel = hiltViewModel(),
    viewModelSearchCarpoolViewModel: SearchCarpoolViewModel = hiltViewModel(),
    viewModelSearchPlace: SearchPlaceViewModel = hiltViewModel(),
    onGoBack: () -> Unit,
) {
    val availableCarpools = viewModelSearchCarpoolViewModel.availableCarpools.collectAsState()
    val amountSeatsRequested = viewModelSearchCarpoolViewModel.amountSeatsRequested.collectAsState()
    val selectedPlace = viewModelSearchPlace.selectedPlace.collectAsState()

    Dialog(
        onDismissRequest = onGoBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.9f)
                .background(
                    Color.Black,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 21.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)
                )
                Text(
                    text = "Resultado de busqueda",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(start = 16.dp),
                    lineHeight = 40.sp
                )
                IconButton(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF3F4042)),
                    onClick = {
                        onGoBack()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            if (availableCarpools.value.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    text = "No se encontraron carpools disponibles",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, bottom = 5.dp, start = 16.dp, end = 16.dp),
                    text = "Carpool disponibles:",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
                LazyColumn {
                    itemsIndexed(availableCarpools.value) { _, carpool ->
                        CarpoolInfoCard(
                            navHostController = navHostController,
                            viewModel = viewModel,
                            carpool = carpool,
                            onCarpoolRequest = {
                                viewModel.savePassengerRequest(carpool, selectedPlace.value, amountSeatsRequested.value )
                            }
                        )
                    }
                }
            }
        }
    }
}