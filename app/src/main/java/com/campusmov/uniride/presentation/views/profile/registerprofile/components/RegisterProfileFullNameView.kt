package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@Composable
fun RegisterProfileFullNameView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val formState = viewModel.state
    val isValid = viewModel.isFullNameRegisterValid

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp, horizontal = 16.dp),
                textAlign = TextAlign.Start,
                text = "Cual es tu nombre?",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 15.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Start,
                text = "Háganoslo saber para dirigirnos a usted correctamente.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White
            )
            Column(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Column {
                    DefaultRoundedInputField(
                        value = formState.value.firstName,
                        onValueChange = {
                            viewModel.onFirstNameInput(it)
                        },
                        placeholder = "Ingresa tus nombres",
                        keyboardType = KeyboardType.Text
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Transparent,
                        thickness = 1.dp
                    )
                    DefaultRoundedInputField(
                        value = formState.value.lastName,
                        onValueChange = {
                            viewModel.onLastNameInput(it)
                        },
                        placeholder = "Ingresa tus apellidos",
                        keyboardType = KeyboardType.Text
                    )
                }

                DefaultRoundedTextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.4f),
                    text = "Siguiente",
                    enabled = isValid.value,
                    enabledRightIcon = true,
                    onClick = {
                        navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                    }
                )
            }
        }
    }
}
