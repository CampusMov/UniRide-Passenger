package com.campusmov.uniride.presentation.views.auth.verifiyCode

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.shared.util.Resource
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.components.OtpTextField
import com.campusmov.uniride.presentation.navigation.screen.auth.AuthScreen
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen

@Composable
fun EnterVerificationCodeView(
    viewModel: EnterVerificationCodeViewModel = hiltViewModel(),
    navHostController: NavHostController,
) {
    val codeState = viewModel.state.value.code
    val user = viewModel.user.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val isButtonAvailable = viewModel.isButtonAvailable.collectAsState()
    val secondsLeftToResendCode = viewModel.secondsLeftToResendCode.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    val isMessageErrorVisible = viewModel.isMessageErrorVisible.collectAsState()

    LaunchedEffect(viewModel.verifyCodeResponse.value) {
        when (viewModel.verifyCodeResponse.value) {
            is Resource.Success -> {
                navHostController.navigate(ProfileScreen.RegisterProfileFullName.route) {
                    popUpTo(AuthScreen.EnterVerificationCode.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            is Resource.Failure -> {
                Log.d("TAG" , "Error navigating to HomeView")
            }
            else -> {}
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues),
        ) {
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(Color.Transparent),
                onClick = {
                    navHostController.popBackStack()
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
                textAlign = TextAlign.Center,
                text = "Ingresa el código",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 40.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center,
                text = "Te enviaremos un codigo para verificacion al ${user.value?.email}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
            Column(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
               Column {
                   Box{
                       if(isLoading.value) {
                           CircularProgressIndicator(
                               modifier = Modifier
                                   .padding(top = 20.dp),
                               color = Color.White,
                               trackColor = Color.Transparent
                           )
                       } else {
                           OtpTextField(
                               modifier = Modifier
                                   .fillMaxWidth(),
                               otpText = codeState,
                               onOtpTextChange = { value, otpInputFilled ->
                                   viewModel.onCodeInput(value)
                                   if (otpInputFilled) {
                                       viewModel.sendVerificationCode()
                                   }
                               }
                           )
                       }
                   }
                   if (isMessageErrorVisible.value && !isLoading.value) {
                       Text(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(top = 20.dp),
                           text = errorMessage.value,
                           fontSize = 14.sp,
                           fontWeight = FontWeight.Normal,
                           textAlign = TextAlign.Center,
                           color = Color.Red
                       )
                   }
                   if (secondsLeftToResendCode.value > 0 && !isLoading.value) {
                       Text(
                          modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                          text = "Puedes reenviar el código en ${secondsLeftToResendCode.value} segundos",
                          fontSize = 14.sp,
                          fontWeight = FontWeight.Normal,
                          textAlign = TextAlign.Center,
                          color = Color.White
                       )
                   }
               }
                if (isButtonAvailable.value && !isLoading.value) {
                    DefaultRoundedTextButton(
                        text = "Reenviar código",
                        onClick = { viewModel.resendVerificationEmail() },
                    )
                }
            }
        }
    }
}