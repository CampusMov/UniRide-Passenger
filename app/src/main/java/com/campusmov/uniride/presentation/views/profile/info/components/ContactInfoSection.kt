package com.campusmov.uniride.presentation.views.profile.info.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileState

@Composable
fun ContactInfoSection(
    profile: RegisterProfileState,
    onPersonalEmailAddressInput: (String) -> Unit,
    onPhoneNumberInput: (String) -> Unit
) {
    Column {
        SectionTitle(title = "Información de Contacto")
        Spacer(modifier = Modifier.height(10.dp))
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
            value = profile.institutionalEmailAddress,
            onValueChange = { /* Not directly editable */ },
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
            value = profile.personalEmailAddress,
            onValueChange = onPersonalEmailAddressInput,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Número de celular",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        DefaultRoundedInputField(
            placeholder = "999999999",
            keyboardType = KeyboardType.Number,
            value = profile.phoneNumber,
            onValueChange = onPhoneNumberInput,
        )
    }
}