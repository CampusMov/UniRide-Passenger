package com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.routingmatching.model.ECarpoolStatus
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.CarpoolsSearchResultsViewModel
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
import com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule.SearchClassScheduleViewModel
import com.campusmov.uniride.presentation.views.routingmatching.searchplace.SearchPlaceViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaitForCarpoolStartView(
    navHostController: NavHostController,
    viewModel: WaitForCarpoolStartViewModel = hiltViewModel(),
    viewModelMapContent: MapContentViewModel = hiltViewModel(),
    viewModelSearchPlace: SearchPlaceViewModel = hiltViewModel(),
    viewModelSearchClassSchedule: SearchClassScheduleViewModel = hiltViewModel(),
    viewModelSearchResult: CarpoolsSearchResultsViewModel = hiltViewModel()
    ) {
    val isLoading = viewModel.isLoading.collectAsState()
    val currentCarpool = viewModel.currentCarpool.collectAsState()
    val selectedPickUpPoint = viewModelSearchPlace.selectedPlace.collectAsState()
    val selectedClassSchedule = viewModelSearchClassSchedule.selectedClassSchedule.collectAsState()

    val profile = viewModelSearchResult.profiles.collectAsState().value[currentCarpool.value?.driverId ?: ""]
    val rating = viewModelSearchResult.ratings.collectAsState().value[currentCarpool.value?.driverId ?: ""] ?: 0.0

    LaunchedEffect(viewModelMapContent.carpoolAcceptedId.value) {
        if (viewModelMapContent.carpoolAcceptedId.value?.isNotEmpty() == true) {
            Log.d("TAG", "Carpool ID: ${viewModelMapContent.carpoolAcceptedId.value}")
            viewModel.getCarpoolById(viewModelMapContent.carpoolAcceptedId.value!!)
        } else {
            Log.d("TAG", "No carpool ID found")
        }
    }

    LaunchedEffect(currentCarpool.value) {
        if (currentCarpool.value != null) {
            Log.d("TAG", "Current carpool: ${currentCarpool.value}")
            viewModelMapContent.loadRoute(
                startLatitude = currentCarpool.value?.origin?.latitude ?: 0.0,
                startLongitude = currentCarpool.value?.origin?.longitude ?: 0.0,
                endLatitude = currentCarpool.value?.destination?.latitude ?: 0.0,
                endLongitude = currentCarpool.value?.destination?.longitude ?: 0.0
            )
            when(currentCarpool.value!!.status) {
                ECarpoolStatus.IN_PROGRESS -> {
                    Log.d("TAG", "WaitForCarpoolStartView: Carpool is in progress, status: ${currentCarpool.value!!.status}")
                    viewModelMapContent.inCarpool()
                }
                ECarpoolStatus.CANCELLED -> {
                    Log.d("TAG", "WaitForCarpoolStartView: Carpool is cancelled, status: ${currentCarpool.value!!.status}")
                    viewModelMapContent.cancelCarpool()
                }
                else -> {
                    Log.d("TAG", "WaitForCarpoolStartView: Carpool is not in progress or cancelled, status: ${currentCarpool.value!!.status}")
                }
            }
        } else {
            Log.d("TAG", "No current carpool found")
        }
    }

    Log.d("TAG", "Carpool ID : ${viewModelMapContent.carpoolAcceptedId.value}")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            text = currentCarpool.value?.origin?.address ?: "Origen desconocido",
            softWrap = true,
            fontSize = 16.sp,
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
                text = "Partida",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
            text = selectedPickUpPoint.value?.displayName ?: "Ubicación no seleccionada",
            softWrap = true,
            fontSize = 16.sp,
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

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
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
                text = currentCarpool.value?.destination?.address?: "Ubicación desconocida",
                softWrap = true,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
            )

            Text(
                text = ((selectedClassSchedule.value?.courseName ?: "Sin curso") + "  " + (selectedClassSchedule.value?.timeRange() ?: "Sin horario")),
                softWrap = true,
                fontSize = 16.sp,
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
                text = "Destino",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier
                .background(Color(0xFF292929), shape = CircleShape)
                .padding(10.dp)
                .size(15.dp),
            painter = painterResource(id = R.drawable.user_white_profile_icon),
            contentDescription = "User Icon",
        )
        Spacer(
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating star",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                Text(
                    text = "${rating}   ${profile?.firstName}  ${profile?.lastName}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "S/ 5.10",
                color = Color.Gray,
                fontSize = 12.sp
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(Color.Black)
    ){
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "El carpool aun no ha iniciado",
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 22.sp,
        )
    }

    Button (
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(0.dp),
        colors = ButtonDefaults.buttonColors(Color.Black),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(
            text = "Cancelar carpool",
            color = Color.White,
            fontSize = 12.sp,
        )
    }


}