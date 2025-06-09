package com.campusmov.uniride.presentation.views.auth.verifyemail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Composable
fun EnterInstitutionalEmailView(
    viewModel: EnterInstitutionalEmailViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state = viewModel

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.errorMessage.value) {
        if (viewModel.errorMessage.value.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(viewModel.errorMessage.value)
                viewModel.errorMessage.value = ""
            }
        }
    }

    LaunchedEffect(viewModel.verifyEmailResponse.value) {
        when (viewModel.verifyEmailResponse.value) {
            is Resource.Success -> navHostController.navigate(AuthScreen.EnterVerificationCode.route)
            is Resource.Failure -> { Log.d("TAG", "Error:") }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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
                    value = state.state.value.email,
                    onValueChange = { state.onEmailInput(it) },
                    placeholder = "example@university.upc.edu",
                    keyboardType = KeyboardType.Email
                )

                DefaultRoundedTextButton(
                    text = "Enviar codigo",
                    onClick = { viewModel.sendVerificationEmail() },
                )
            }
        }
    }
}