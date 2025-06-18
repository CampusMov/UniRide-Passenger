package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RegisterProfileContactInformationView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val state = viewModel.profileState.value
    val isValid = viewModel.isContactInformationRegisterValid

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .background(Color.Transparent)
                        .size(31.dp),
                    onClick = { navHostController.popBackStack() },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Icon of go back",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    textAlign = TextAlign.Start,
                    text = "Informacion de contacto",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 25.dp),
                    textAlign = TextAlign.Start,
                    text = "Esta es la informacion que quieres por donde nos contactemos contigo.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Correo institucional",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedInputField(
                    placeholder = "Ingresa tu correo institucional",
                    enable = false,
                    value = state.institutionalEmailAddress,
                    onValueChange = {},
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Correo personal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedInputField(
                    placeholder = "example@gmail.com",
                    value = state.personalEmailAddress,
                    onValueChange = { viewModel.onPersonalEmailAddressInput(it) },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Numero de celular",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                DefaultRoundedInputField(
                    placeholder = "999999999",
                    keyboardType = KeyboardType.Number,
                    value = state.phoneNumber,
                    onValueChange = { viewModel.onPhoneNumberInput(it) },
                )
            }
            DefaultRoundedTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Guardar",
                enabled = isValid.value,
                onClick = {
                    viewModel.onNextStep(2)
                    navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                },
            )
        }
    }
}
