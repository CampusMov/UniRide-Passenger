package com.campusmov.uniride.presentation.profile.view

import ScheduleDialogView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel

@Composable
fun AcademicInfoView(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val ui = viewModel.academicInfo.value
    val valid = ui.university.isNotBlank() && ui.academicProgram.isNotBlank()

    var showScheduleDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val profileText by viewModel.profileTextToShow

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(vertical = 30.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Volver", tint = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Text(
                "Información académica",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(Modifier.height(50.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DefaultRoundedInputField(
                    ui.university,
                    { viewModel.updateAcademic { copy(university = it) } },
                    "Universidad"
                )
                DefaultRoundedInputField(
                    ui.faculty,
                    { viewModel.updateAcademic { copy(faculty = it) } },
                    "Facultad"
                )
                DefaultRoundedInputField(
                    ui.academicProgram,
                    { viewModel.updateAcademic { copy(academicProgram = it) } },
                    "Carrera"
                )
            }

            Spacer(Modifier.height(24.dp))

            Text("Horarios", color = Color.White, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            ui.schedules.forEach { sched ->
                ScheduleItemView(
                    schedule = sched,
                    onEdit = { viewModel.startEditingSchedule(sched); showScheduleDialog = true },
                    onDelete = { viewModel.deleteSchedule(sched) }
                )
                Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            }

            Spacer(Modifier.height(8.dp))

            Box(modifier = Modifier.padding(horizontal = 40.dp)) {
                DefaultRoundedTextButton(
                    text = "Agregar horario",
                    enabled = true,
                    onClick = { showScheduleDialog = true },
                )
            }
        }

        Spacer(Modifier.weight(1f))
        DefaultRoundedTextButton(
            text = "Guardar",
            enabled = valid,
            onClick = {
                viewModel.prepareProfileText() // Prepara el texto del perfil para mostrar
                showProfileDialog = true       // Muestra el diálogo con el texto
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 50.dp)
        )
    }

    if (showScheduleDialog) {
        ScheduleDialogView(
            schedule = viewModel.editingSchedule.value,
            onSave = {
                showScheduleDialog = false
                viewModel.editingSchedule.value = null
            },
            onCancel = {
                showScheduleDialog = false
                viewModel.editingSchedule.value = null
            }
        )
    }

    if (showProfileDialog && !profileText.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = {
                showProfileDialog = false
                viewModel.profileTextToShow.value = null
            },
            title = { Text("Perfil generado", color = Color.White) },
            text = { Text(profileText ?: "", color = Color.White) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showProfileDialog = false
                        viewModel.profileTextToShow.value = null
                    }
                ) {
                    Text("Cerrar")
                }
            },
            containerColor = Color.Black
        )
    }
}

