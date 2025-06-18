package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule.SearchClassScheduleViewModel
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchCarpoolView(
    viewModel: SearchCarpoolViewModel = hiltViewModel(),
    viewModelSearchPlace: SearchPlaceViewModel = hiltViewModel(),
    viewModelSearchClassSchedule: SearchClassScheduleViewModel = hiltViewModel(),
    onPickUpPointSelected: () -> Unit,
    onClassScheduleSelected: () -> Unit,
    onOpenCarpoolsSearchResultsView: () -> Unit,
    navHostController: NavHostController
) {
    val selectedPickUpPoint = viewModelSearchPlace.selectedPlace.collectAsState()
    val selectedClassSchedule = viewModelSearchClassSchedule.selectedClassSchedule.collectAsState()
    val amountSeatsRequested = viewModel.amountSeatsRequested.collectAsState()
    val isLoadingSearchAvailableCarpool = viewModel.isLoadingSearchAvailableCarpool.collectAsState()

    val isSearchEnabled = remember {
        derivedStateOf {
            selectedPickUpPoint.value != null &&
            selectedClassSchedule.value != null &&
            amountSeatsRequested.value > 0
        }
    }

    if (selectedPickUpPoint.value == null) {
        DefaultRoundedInputField(
            enable = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onPickUpPointSelected() },
            value = "",
            onValueChange = {},
            placeholder = "Seleccionar punto de recogida",
            enableLeadingIcon = true
        )
    }
    else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onPickUpPointSelected()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 12.dp),
                painter = painterResource(id = R.drawable.ellipse_icon),
                contentDescription = "Icon of pickUpLocation",
            )
            Text(
                text = selectedPickUpPoint.value?.displayName ?: "",
                softWrap = true,
                fontSize = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(Color(0xFF3F4042), RoundedCornerShape(5.dp))
            ){
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Recogida",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )

    if (selectedClassSchedule.value == null) {
        DefaultRoundedInputField(
            enable = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onClassScheduleSelected() },
            value = "",
            onValueChange = {},
            placeholder = "Seleccionar horario de clases",
            enableLeadingIcon = true
        )
    }
    else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onClassScheduleSelected()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 12.dp),
                painter = painterResource(id = R.drawable.ellipse_icon),
                contentDescription = "Icon of pickUpLocation",
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = selectedClassSchedule.value?.courseName ?: "",
                    softWrap = true,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                )
                Text(
                    text = selectedClassSchedule.value?.timeRange() ?: "",
                    softWrap = true,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .background(Color(0xFF3F4042), RoundedCornerShape(5.dp))
            ){
                Text(
                    modifier = Modifier
                        .padding(4.dp),
                    text = "Llegada",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }
    }

    SetAmountOfSeats(viewModel = viewModel)

    DefaultRoundedTextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        text = "Buscar carpools",
        loadingText = "Buscando carpools",
        isLoading = isLoadingSearchAvailableCarpool.value,
        enabled = isSearchEnabled.value,
        onClick = {
            viewModel.searchAvailableCarpools(
                place = selectedPickUpPoint.value,
                classSchedule = selectedClassSchedule.value,
                requestedSeats = amountSeatsRequested.value,
                openCarpoolSearchResultsView = {onOpenCarpoolsSearchResultsView()}
            )
        },
    )
}

@Composable
fun SetAmountOfSeats(viewModel: SearchCarpoolViewModel){
    val amountSeatsRequested = viewModel.amountSeatsRequested.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Cantidad de asientos",
            color = Color.White,
            fontSize = 16.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.decreaseAmountSeatsRequested()
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.RemoveCircleOutline,
                    contentDescription = "Remove seats",
                    tint = Color.White
                )
            }
            Text(
                text = amountSeatsRequested.value.toString(),
                color = Color.White,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {
                    viewModel.increaseAmountSeatsRequested()
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.AddCircleOutline,
                    contentDescription = "Add seats",
                    tint = Color.White
                )
            }
        }
    }
}