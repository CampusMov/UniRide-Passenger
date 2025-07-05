package com.campusmov.uniride.presentation.views.routingmatching.carpoolinprogress

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel
import com.campusmov.uniride.presentation.views.routingmatching.waitforcarpoolstart.WaitForCarpoolStartViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CarpoolInProgressView(
    navHostController: NavHostController,
    viewModel: CarpoolInProgressViewModel = hiltViewModel(),
    viewModelMapContent: MapContentViewModel = hiltViewModel(),
) {
    val currentCarpool = viewModel.currentCarpool.collectAsState()
    val route = viewModelMapContent.route.collectAsState()
    val driverProfile = viewModel.driverProfile.collectAsState().value
    val driverRating = viewModel.driverRating.collectAsState().value

    LaunchedEffect(currentCarpool.value) {
        Log.d( "CarpoolInProgressView", "Current Carpool1: ${currentCarpool.value}")
        viewModel.getCarpoolById(viewModelMapContent.carpoolAcceptedId.value!!)
        if (currentCarpool.value != null) {
            Log.d( "CarpoolInProgressView", "Current Carpool2: ${currentCarpool.value}")
            viewModel.getProfileById(currentCarpool.value?.driverId!!)
            viewModel.getStudentAverageRating(currentCarpool.value?.driverId!!)
            viewModelMapContent.loadRoute(
                startLatitude = currentCarpool.value?.origin?.latitude ?: 0.0,
                startLongitude = currentCarpool.value?.origin?.longitude ?: 0.0,
                endLatitude = currentCarpool.value?.destination?.latitude ?: 0.0,
                endLongitude = currentCarpool.value?.destination?.longitude ?: 0.0
            )
        }
    }

    val totalDistanceFormatted = remember { mutableStateOf("") }
    val totalDurationNew = remember { mutableStateOf("") }
    val arrivalTime = remember { mutableStateOf("") }

    LaunchedEffect(route.value?.totalDistance) {
        if (route.value?.totalDistance != null) {
            if (route.value?.totalDistance!! > 1000.0) {
                totalDistanceFormatted.value = "%.2f km".format(route.value?.totalDistance!! / 1000)
                totalDurationNew.value = "%.0f min".format(route.value?.totalDistance!!/ 500)
                arrivalTime.value = LocalDateTime.now().plusMinutes((route.value?.totalDistance!!/ 500).toLong()).format(DateTimeFormatter.ofPattern("HH:mm"))?: "00:00"

            } else {
                totalDistanceFormatted.value = "%.0f m".format(route.value?.totalDistance)
                totalDurationNew.value = "%.0f min".format(route.value?.totalDistance!! / 500)
                arrivalTime.value = LocalDateTime.now().plusMinutes((route.value?.totalDistance!!/ 500).toLong()).format(DateTimeFormatter.ofPattern("HH:mm"))?: "00:00"
            }
        } else {
            totalDistanceFormatted.value = "Cargando..."
            totalDurationNew.value = "Cargando..."
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = arrivalTime.value,
            color = Color.White,
            fontSize = 22.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "${totalDurationNew.value} - ${totalDistanceFormatted.value}",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

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
            ){
                Text(
                    text = currentCarpool.value?.destination?.address?: "Ubicación desconocida",
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

        Spacer(modifier = Modifier.height(16.dp))

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
                        text = "${driverRating}   ${driverProfile?.firstName}  ${driverProfile?.lastName}",
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


    }

}