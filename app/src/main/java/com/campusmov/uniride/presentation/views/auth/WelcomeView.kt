package com.campusmov.uniride.presentation.views.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.auth.model.UserStatus
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.Graph
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen

@Composable
fun WelcomeView(
    viewModel: WelcomeViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val user = viewModel.user.collectAsState().value

    LaunchedEffect(user) {
        if (user?.status == UserStatus.ACTIVE) {
            navHostController.navigate(route = Graph.MATCHING) {
                popUpTo(route = Graph.AUTH) { inclusive = true }
            }
        }
    }

    Scaffold { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValue)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .background(Color.Black),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.uniride_logo),
                    contentDescription = null,
                )
                Text(
                    text = "UniRide",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .size(280.dp),
                painter = painterResource(id = R.drawable.welcome_banner),
                contentDescription = null,
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    textAlign = TextAlign.Center,
                    text = "Llega a clases en un clic",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp),
                    textAlign = TextAlign.Center,
                    text = "Elije los carpools que mejor te funcionen",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .width(30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircleWithBox(
                        color = Color(0xFFD9D9D9),
                        size = 10.dp
                    )
                    CircleWithBox(
                        color = Color(0xFF7B7B7B),
                        size = 10.dp
                    )
                }
                DefaultRoundedTextButton(
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 13.dp)
                        .fillMaxWidth(),
                    text = "Continuar",
                    onClick = {
                        navHostController.navigate(route = AuthScreen.EnterInstitutionalEmail.route)
                        //navHostController.navigate(route = Graph.MATCHING)



                    }
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 50.dp, start = 13.dp, end = 13.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Al unirte a nuestra aplicación, aceptas nuestro Terminos de uso y Política de privacidad.",
                    fontSize = 13.sp,
                    color = Color(0xFF98999B)
                )
            }
        }
    }
}

@Composable
fun CircleWithBox(
    color: Color,
    size: Dp = 100.dp
){
    Box(
        modifier = Modifier
            .size(size)
            .background(color = color, shape = CircleShape)
    )
}