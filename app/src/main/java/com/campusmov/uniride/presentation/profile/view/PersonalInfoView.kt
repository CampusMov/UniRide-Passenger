package com.campusmov.uniride.presentation.profile.view

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoView(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val ui = viewModel.personalInfo.value
    val valid = ui.firstName.isNotBlank() && ui.lastName.isNotBlank()

    val context = LocalContext.current
    val dateDialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                viewModel.updatePersonal {
                    copy(birthDate = "%02d/%02d/%04d".format(d, m + 1, y))
                }
            }, 2000, 0, 1
        )
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.updatePersonal { copy(profilePicUri = uri.toString()) }
    }

    // Estado para el menú desplegable de género
    var selectedGender by remember { mutableStateOf(ui.gender ?: "Masculino") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Información personal", color = Color.White, fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black),
                modifier = Modifier.padding(vertical = 25.dp).fillMaxWidth()
            )
        },
        containerColor = Color.Black,
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(8.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Corregido el error tipográfico
                    .size(100.dp)
                    .background(Color.DarkGray, CircleShape)
                    .clickable { imagePicker.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (ui.profilePicUri.isNotEmpty() || ui.profilePicUri.isNotBlank()) {
                    AsyncImage(
                        model = ui.profilePicUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Edit, null, tint = Color.LightGray) // placeholder icon
                }
            }

            Spacer(Modifier.height(24.dp))

            // Campo de nombre
            DefaultRoundedInputField(
                value = ui.firstName,
                onValueChange = { viewModel.updatePersonal { copy(firstName = it) } },
                placeholder = "Nombre",
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de apellido
            DefaultRoundedInputField(
                value = ui.lastName,
                onValueChange = { viewModel.updatePersonal { copy(lastName = it) } },
                placeholder = "Apellido",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3F4042), shape = RoundedCornerShape(12.dp))
                    .clickable { dateDialog.show() }
                    .padding(16.dp)
            ) {
                Text(text = ui.birthDate.ifEmpty { "Seleccionar fecha de nacimiento" }, color = Color.White)
            }

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF3F4042), shape = RoundedCornerShape(12.dp))
                    .clickable {
                        selectedGender = when (selectedGender) {
                            "Masculino" -> "Femenino"
                            "Femenino" -> "Otro"
                            else -> "Masculino"
                        }
                        viewModel.updatePersonal { copy(gender = selectedGender) }
                    }
                    .padding(16.dp)
            ) {
                Text(text = "Género: $selectedGender", color = Color.White)
            }

            Spacer(Modifier.weight(1f))

            DefaultRoundedTextButton(
                text = "Guardar",
                enabled = valid,
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().padding(vertical = 50.dp)
            )
        }
    }
}
