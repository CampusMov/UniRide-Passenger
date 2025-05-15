package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceViewModel

@Composable
fun SearchCarpoolView(
    viewModel: SearchCarpoolViewModel = hiltViewModel(),
    viewModelSearchPlace: SearchPlaceViewModel = hiltViewModel(),
    onOriginPlaceSelected: () -> Unit,
    navHostController: NavHostController
) {
    val originPlace = viewModelSearchPlace.selectedPlace.collectAsState()
    var destinationSearchQuery = remember { mutableStateOf("") }

    if (originPlace.value == null){
        DefaultRoundedInputField(
            enable = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onOriginPlaceSelected()
                },
            value = originPlace.value?.address ?: "",
            onValueChange = {},
            placeholder = "Seleccionar punto de recogida",
            enableLeadingIcon = true
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onOriginPlaceSelected()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                modifier = Modifier.padding(end = 12.dp),
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = "PickUpLocation",
                tint = Color.White
            )
            Text(
                text = originPlace.value?.address ?: "",
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

    DefaultRoundedInputField(
        enable = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = destinationSearchQuery.value,
        onValueChange = {
            destinationSearchQuery.value = it
        },
        placeholder = "Seleccionar horario de clases",
        enableLeadingIcon = true
    )

    SetAmountOfSeats(viewModel = viewModel)

    DefaultRoundedTextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        text = "Buscar carpools",
        onClick = {

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