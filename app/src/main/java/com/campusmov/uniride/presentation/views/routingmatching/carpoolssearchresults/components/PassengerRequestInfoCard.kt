package com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.routingmatching.model.PassengerRequest
import com.campusmov.uniride.presentation.views.routingmatching.carpoolssearchresults.CarpoolsSearchResultsViewModel

@Composable
fun PassengerRequestInfoCard(
    navHostController: NavHostController,
    viewModel: CarpoolsSearchResultsViewModel,
    passengerRequest: PassengerRequest,
    onPassengerRequestCancel: () -> Unit,
) {
    val carpools = viewModel.requestedCarpools.collectAsState()
    val carpool = carpools.value[passengerRequest.carpoolId]

    LaunchedEffect(passengerRequest.carpoolId) {
        viewModel.getCarpoolById(passengerRequest.carpoolId)
    }

    LaunchedEffect(carpool?.driverId) {
        carpool?.driverId?.let { driverId ->
            viewModel.getProfileById(driverId)
            viewModel.getStudentAverageRating(driverId)
        }
    }


    val profiles = viewModel.profiles.collectAsState()
    val ratings = viewModel.ratings.collectAsState()
    val profile = profiles.value[carpool?.driverId]
    val rating = ratings.value[carpool?.driverId]

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.BottomEnd
            ){
                if (profile?.profilePictureUrl?.isNotBlank() == true) {
                    AsyncImage(
                        model = profile.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.user_profile_icon),
                        contentDescription = "Default profile icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
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
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))
                    Text(
                        text = "${rating}   ${profile?.firstName}  ${profile?.lastName}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "${carpool?.origin?.name} - aun no sale",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "S/ 5.10",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                )
            }
        }
        Spacer(
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        TextButton(
            modifier = Modifier
                .background(Color(0xFF292929), shape = RoundedCornerShape(12.dp))
                .height(40.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            onClick = {
                onPassengerRequestCancel()
            },
        ) {
            Image(
                modifier = Modifier
                    .size(19.dp),
                painter = painterResource(id = R.drawable.close_icon),
                contentDescription = "Send Icon",
            )
            Spacer(
                modifier = Modifier.padding(horizontal = 3.dp)
            )
            Text(
                text = "Cancelar",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}