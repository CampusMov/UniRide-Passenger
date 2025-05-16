package com.campusmov.uniride.presentation.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileListView(
    onNavigate: (String) -> Unit,
    onFinish: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val ui = viewModel.personalInfo.value
    val contactInfo = viewModel.contactInfo.value
    val academicInfo = viewModel.academicInfo.value
    val termsAccepted = viewModel.terms.value.accepted

    val allFieldsFilled =
        ui.firstName.isNotBlank() && ui.lastName.isNotBlank() && ui.gender.isNotBlank() && ui.birthDate.isNotBlank() &&
                contactInfo.institutionalEmail.isNotBlank() && contactInfo.personalEmail.isNotBlank() && contactInfo.countryCode.isNotBlank() && contactInfo.phoneNumber.isNotBlank() &&
                academicInfo.university.isNotBlank() && academicInfo.academicProgram.isNotBlank() && academicInfo.semester.isNotBlank() && academicInfo.faculty.isNotBlank() &&
                termsAccepted

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(vertical = 70.dp)
    ) {
        Text(
            "Bienvenido, ${ui.firstName}",
            color = Color.White,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(15.dp)
        )
        Spacer(Modifier.height(8.dp))

        Column(Modifier.padding(40.dp)) {
            Text("Configura tu perfil paso a paso.", color = Color.White.copy(0.7f))
            Spacer(Modifier.height(24.dp))

            listOf(
                ProfileScreen.RegisterProfilePersonalInfo to "Información personal",
                ProfileScreen.RegisterProfileContactInfo to "Información de contacto",
                ProfileScreen.RegisterProfileAcademicInfo to "Información académica",
                ProfileScreen.RegisterProfileTerms to "Términos y condiciones"
            ).forEach { (screen, label) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onNavigate(screen.route) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, color = Color.White)
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
                }
                Divider(color = Color.White.copy(0.12f))
            }
        }

            Spacer(Modifier.weight(1f))

        DefaultRoundedTextButton(
            text = if (allFieldsFilled) "Finalizar" else "¡Comenzar!",
            enabled = true,
            onClick = {
                if (allFieldsFilled) {
                    viewModel.onSaveProfile()
                    onFinish()
                } else {
                    onNavigate(ProfileScreen.RegisterProfilePersonalInfo.route)
                }
            },
            modifier = Modifier.padding(15.dp)
        )
    }
}

