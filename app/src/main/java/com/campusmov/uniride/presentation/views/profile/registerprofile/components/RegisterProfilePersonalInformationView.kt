package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.presentation.components.DefaultRoundedDateInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedDropdownField
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.util.toEpochMillis
import com.campusmov.uniride.presentation.util.toLocalDate
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterProfilePersonalInformationView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val isValid = viewModel.isPersonalInformationRegisterValid

    var dateMillis = remember { mutableStateOf<Long?>(state.birthDate?.toEpochMillis()) }

    LaunchedEffect(dateMillis.value) {
        dateMillis.value?.let {
            viewModel.onBirthDateInput(
                it.toLocalDate()
            )
        }
    }

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
                    text = "Informacion personal",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 25.dp),
                    textAlign = TextAlign.Start,
                    text = "Esta es la informacion que quieres que las  personas usen cuando se refieran a ti.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                            .padding(8.dp),
                        painter = painterResource(id = R.drawable.user_profile_icon),
                        contentDescription = "Icon of profile",
                    )
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF292929))
                            .size(40.dp),
                        onClick = {
                            //TODO: edit image
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_icon),
                            contentDescription = "Icon of go back",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "Nombres",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedInputField(
                    placeholder = "Ingresa tus nombres",
                    value = state.firstName,
                    onValueChange = { viewModel.onFirstNameInput(it) },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Apellidos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedInputField(
                    placeholder = "Ingresa tus apellidos",
                    value = state.lastName,
                    onValueChange = { viewModel.onLastNameInput(it) },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Fecha de nacimiento",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedDateInputField(
                    selectedDateMillis = dateMillis.value,
                    onDateSelected = { dateMillis.value = it }
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Genero",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                DefaultRoundedDropdownField(
                    selectedOption = state.gender,
                    options = listOf(EGender.MALE.toString(), EGender.FEMALE.toString()),
                    onOptionSelected = {
                        viewModel.onGenderInput(
                            EGender.valueOf(it.uppercase())
                        )
                    }
                )
            }
            DefaultRoundedTextButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Guardar",
                enabled = isValid.value,
                onClick = {
                    viewModel.onNextStep(1)
                    navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                },
            )
        }
    }
}