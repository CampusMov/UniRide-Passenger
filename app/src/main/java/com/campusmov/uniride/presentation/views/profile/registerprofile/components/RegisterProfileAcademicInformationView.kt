package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterProfileAcademicInformationView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val state     = viewModel.state.value
    val isValid   = viewModel.isAcademicInformationRegisterValid.value
    val isDialog  = viewModel.isScheduleDialogOpen.value

    if (isDialog) {
        AddOrEditScheduleDialogView(
            viewModel        = viewModel,
            onDismissRequest = { viewModel.onCancelScheduleDialog() }
        )
    }

    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    Scaffold(
        modifier       = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier           = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick  = { navHostController.popBackStack() },
                    modifier = Modifier.size(31.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint               = Color.White
                    )
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    "Información académica",
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Esta es la información que quieres que usemos para brindarte los mejores carpool que se adapten a tus horarios.",
                    fontSize = 16.sp,
                    color    = Color.White
                )
                Spacer(Modifier.height(24.dp))

                //–– Universidad / Facultad / Carrera ––
                Text("Universidad", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value         = state.university,
                    onValueChange = { viewModel.onUniversityInput(it) },
                    placeholder   = "UPC"
                )
                Spacer(Modifier.height(16.dp))

                Text("Facultad", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value         = state.faculty,
                    onValueChange = { viewModel.onFacultyInput(it) },
                    placeholder   = "Ingeniería"
                )
                Spacer(Modifier.height(16.dp))

                Text("Carrera", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value         = state.academicProgram,
                    onValueChange = { viewModel.onAcademicProgramInput(it) },
                    placeholder   = "Ingeniería de software"
                )
                Spacer(Modifier.height(24.dp))

                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Horarios", fontSize = 18.sp, color = Color.White)
                    IconButton(onClick = { viewModel.onAddScheduleDialog() }) {
                        Icon(Icons.Rounded.Add, contentDescription = "Agregar horario", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    itemsIndexed(state.classSchedules) { index, sched ->
                        Row(
                            modifier           = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onEditScheduleDialog(index) }
                                .padding(vertical = 12.dp),
                            verticalAlignment  = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.Schedule,
                                contentDescription = null,
                                tint               = Color.White,
                                modifier           = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text     = sched.courseName,
                                    fontSize = 16.sp,
                                    color    = Color.White
                                )
                                Spacer(Modifier.height(4.dp))
                                Row {
                                    Text(
                                        text     = "${sched.startedAt.format(timeFormatter)} – ${sched.endedAt.format(timeFormatter)}",
                                        fontSize = 14.sp,
                                        color    = Color.Gray
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text     = when (sched.selectedDay) {
                                            EDay.MONDAY    -> "Lunes"
                                            EDay.TUESDAY   -> "Martes"
                                            EDay.WEDNESDAY -> "Miércoles"
                                            EDay.THURSDAY  -> "Jueves"
                                            EDay.FRIDAY    -> "Viernes"
                                            EDay.SATURDAY  -> "Sábado"
                                            EDay.SUNDAY    -> "Domingo"
                                        },
                                        fontSize = 14.sp,
                                        color    = Color.Gray
                                    )
                                }
                            }
                        }
                        Divider(color = Color(0xFF3F4042), thickness = 1.dp)
                    }
                }
            }

            DefaultRoundedTextButton(
                modifier = Modifier.fillMaxWidth(),
                text     = "Guardar",
                enabled  = isValid,
                onClick  = {
                    navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                }
            )
        }
    }
}