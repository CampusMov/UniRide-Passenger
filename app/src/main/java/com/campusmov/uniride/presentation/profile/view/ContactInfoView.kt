package com.campusmov.uniride.presentation.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel
import java.util.regex.Pattern

@Composable
fun ContactInfoView(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // Accede directo al valor porque es mutableStateOf
    val ui = viewModel.contactInfo.value

    val okEmail = Pattern.compile(".+@.+\\..+").matcher(ui.personalEmail).matches()
    val okPhone = ui.phoneNumber.length >= 7
    val valid = okEmail && okPhone

    Column(
        Modifier
            .background(Color.Black)
            .padding(vertical = 30.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 25.dp)) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Text(
                "Información de contacto",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(Modifier.height(24.dp))

        DefaultRoundedInputField(ui.institutionalEmail, {}, "Correo institucional", Modifier.padding(10.dp))
        DefaultRoundedInputField(
            ui.personalEmail,
            { viewModel.updateContact { copy(personalEmail = it) } },
            "Correo personal",
            Modifier.padding(horizontal = 15.dp).padding(top = 8.dp)
        )
        DefaultRoundedInputField(
            ui.phoneNumber,
            { viewModel.updateContact { copy(phoneNumber = it) } },
            "Número de celular",
            Modifier.padding(15.dp)
        )

        Spacer(Modifier.weight(1f))
        DefaultRoundedTextButton(
            text = "Guardar",
            enabled = valid,
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp)
        )
    }
}
