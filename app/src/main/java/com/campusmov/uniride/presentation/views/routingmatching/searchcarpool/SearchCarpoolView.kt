package com.campusmov.uniride.presentation.views.routingmatching.searchcarpool

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton

@Composable
fun SearchCarpoolView(
    viewModel: SearchCarpoolViewModel = hiltViewModel(),
    onOriginPlaceSelected: () -> Unit,
    navHostController: NavHostController
) {

    var originSearchQuery = remember { mutableStateOf("") }
    var destinationSearchQuery = remember { mutableStateOf("") }
    var amountSeatsRequested = remember { mutableIntStateOf(1) }

    DefaultRoundedInputField(
        enable = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable {
                onOriginPlaceSelected()
            },
        value = originSearchQuery.value,
        onValueChange = {
            originSearchQuery.value = it
        },
        placeholder = "Seleccionar punto de recogida",
        enableLeadingIcon = true
    )

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
                    if (amountSeatsRequested.intValue > 1) amountSeatsRequested.intValue--
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
                text = amountSeatsRequested.intValue.toString(),
                color = Color.White,
                fontSize = 20.sp,
            )
            IconButton(
                onClick = {
                    amountSeatsRequested.intValue++
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

    DefaultRoundedTextButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        text = "Buscar carpools",
        onClick = {

        },
    )
}