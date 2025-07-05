package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.profile.model.ClassSchedule
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
    val profileState = viewModel.profileState.value
    val isValid = viewModel.isAcademicInformationRegisterValid.value
    val isScheduleDialogOpened = viewModel.isScheduleDialogOpen.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 90.dp
                )
            ) {
                item {
                    IconButton(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .background(Color.Transparent)
                            .size(31.dp),
                        onClick = { navHostController.popBackStack() },
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.fillMaxSize(),
                            tint = Color.White
                        )
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        textAlign = TextAlign.Start,
                        text = "Información académica",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, bottom = 25.dp),
                        textAlign = TextAlign.Start,
                        text = "Esta es la información que quieres que usemos para brindarte los mejores carpool que se adapten a tus horarios.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = "Universidad",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    DefaultRoundedInputField(
                        placeholder = "Ingresa tu universidad (UPC)",
                        value = profileState.university,
                        onValueChange = { viewModel.onUniversityInput(it) },
                    )
                    Spacer(Modifier.height(15.dp))
                    Text(
                        text = "Facultad",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    DefaultRoundedInputField(
                        placeholder = "Ingresa tu facultad (Ingeniería)",
                        value = profileState.faculty,
                        onValueChange = { viewModel.onFacultyInput(it) },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Carrera",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    DefaultRoundedInputField(
                        placeholder = "Ingresa tu carrera (Negocios)",
                        value = profileState.academicProgram,
                        onValueChange = { viewModel.onAcademicProgramInput(it) },
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Ciclo académico",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    DefaultRoundedInputField(
                        placeholder = "Ingresa tu ciclo de ingreso (2023-02)",
                        value = profileState.semester,
                        onValueChange = { viewModel.onSemesterInput(it) },
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Horarios", fontSize = 18.sp, color = Color.White)
                        IconButton(onClick = { viewModel.onOpenDialogToAddNewSchedule() }) {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "Agregar horario",
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }

                itemsIndexed(profileState.classSchedules) { index, schedule ->
                    ClassScheduleItemView(
                        viewModel = viewModel,
                        schedule = schedule
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFF3F4042)
                    )
                }
            }

            if (isValid) {
                DefaultRoundedTextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    text = "Guardar",
                    onClick = {
                        navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                    }
                )
            }
        }

        if (isScheduleDialogOpened) {
            ClassScheduleDialogView(viewModel = viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClassScheduleItemView(
    viewModel: RegisterProfileViewModel,
    schedule: ClassSchedule
) {
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.onOpenDialogToEditSchedule(schedule.id) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .background(Color(0xFF292929), shape = CircleShape)
                .padding(12.dp)
                .size(20.dp),
            painter = painterResource(id = R.drawable.icon_calendar),
            contentDescription = "User Icon",
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = schedule.courseName,
                fontSize = 16.sp,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))
            Row {
                Text(
                    text = "${schedule.startedAt.format(timeFormatter)} – ${
                        schedule.endedAt.format(
                            timeFormatter
                        )
                    }",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = schedule.selectedDay.showDay(),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}