package com.campusmov.uniride.presentation.views.auth.verifyemail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.shared.util.Resource
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen

@Composable
fun EnterInstitutionalEmailView(
    viewModel: EnterInstitutionalEmailViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val emailState = viewModel.state.value

    val isLoading = viewModel.isLoading.collectAsState()
    val isButtonAvailable = viewModel.isButtonAvailable.collectAsState()

    LaunchedEffect(viewModel.verifyEmailResponse.value) {
        when (viewModel.verifyEmailResponse.value) {
            is Resource.Success -> navHostController.navigate(AuthScreen.EnterVerificationCode.route)
            is Resource.Failure -> { Log.d("TAG", "Error navigating to EnterVerificationCode") }
            else -> {}
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(Color.Transparent),
                onClick = {
                    navHostController.navigate(AuthScreen.Welcome.route)
                },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Icon of go back",
                    tint = Color.White)
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 16.dp),
                textAlign = TextAlign.Start,
                text = "Introduce tu correo institucional",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Start,
                text = "Te enviaremos un codigo para verificar tu correo.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
            Column(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultRoundedInputField(
                    value = emailState.email,
                    onValueChange = { viewModel.onEmailInput(it) },
                    placeholder = "example@university.upc.edu",
                    keyboardType = KeyboardType.Email
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 20.dp),
                            color = Color.White,
                            trackColor = Color.Transparent
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    )
                    if (isButtonAvailable.value && !isLoading.value) {
                        DefaultRoundedTextButton(
                            text = "Enviar codigo",
                            onClick = { viewModel.sendVerificationEmail() },
                        )
                    }
                }
            }
        }
    }
}