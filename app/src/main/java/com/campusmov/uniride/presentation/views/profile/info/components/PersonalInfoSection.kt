package com.campusmov.uniride.presentation.views.profile.info.components

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.profile.model.EGender
import com.campusmov.uniride.presentation.components.DefaultRoundedDateInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedDropdownField
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.util.toEpochMillis
import com.campusmov.uniride.presentation.util.toLocalDate
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileState // Import RegisterProfileState
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PersonalInfoSection(
    profile: RegisterProfileState,
    onFirstNameInput: (String) -> Unit,
    onLastNameInput: (String) -> Unit,
    onBirthDateInput: (LocalDate) -> Unit,
    onGenderInput: (EGender) -> Unit,
    uploadProfileImage: (Uri) -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { uploadProfileImage(it) }
    }
    var dateMillis = remember(profile.birthDate) { mutableStateOf(profile.birthDate?.toEpochMillis()) }

    LaunchedEffect(dateMillis.value) {
        dateMillis.value?.let { millis ->
            val newDate = millis.toLocalDate()
            if (newDate != profile.birthDate) {
                onBirthDateInput(newDate)
            }
        }
    }

    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (profile.profilePictureUrl.isNotBlank()) {
                AsyncImage(
                    model = profile.profilePictureUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.user_profile_icon),
                    contentDescription = "Icono de perfil por defecto",
                    modifier = Modifier.fillMaxSize()
                )
            }
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF292929))
                    .size(40.dp),
                onClick = { imagePicker.launch("image/*") }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_icon),
                    contentDescription = "Icono de editar",
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
            value = profile.firstName,
            onValueChange = onFirstNameInput,
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
            value = profile.lastName,
            onValueChange = onLastNameInput,
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
            text = "Género",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        DefaultRoundedDropdownField(
            selectedOption = profile.gender,
            options = EGender.entries.map { it.toString() },
            onOptionSelected = { selectedString ->
                onGenderInput(EGender.valueOf(selectedString.uppercase()))
            }
        )
    }
}