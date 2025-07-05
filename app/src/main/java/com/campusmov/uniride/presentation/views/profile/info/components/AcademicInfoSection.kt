package com.campusmov.uniride.presentation.views.profile.info.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campusmov.uniride.R
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AcademicInfoSection(
    profile: RegisterProfileState,
    onUniversityInput: (String) -> Unit,
    onFacultyInput: (String) -> Unit,
    onAcademicProgramInput: (String) -> Unit,
    onSemesterInput: (String) -> Unit,
    onOpenDialogToAddNewSchedule: () -> Unit,
    onOpenDialogToEditSchedule: (String) -> Unit
) {
    Column {
        SectionTitle(title = "Información Académica")
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Universidad",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        DefaultRoundedInputField(
            placeholder = "Ingresa tu universidad (UPC)",
            value = profile.university,
            onValueChange = onUniversityInput,
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Facultad",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        DefaultRoundedInputField(
            placeholder = "Ingresa tu facultad (Ingeniería)",
            value = profile.faculty,
            onValueChange = onFacultyInput,
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
            placeholder = "Ingresa tu carrera (Ingeniería de Sistemas)",
            value = profile.academicProgram,
            onValueChange = onAcademicProgramInput,
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
            placeholder = "Ingresa tu ciclo de ingreso (2023-2)",
            value = profile.semester,
            onValueChange = onSemesterInput,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Horarios de Clase", fontSize = 18.sp, color = Color.White)
            IconButton(onClick = onOpenDialogToAddNewSchedule) {
                Icon(Icons.Rounded.Add, contentDescription = "Agregar horario", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        if (profile.classSchedules.isEmpty()) {
            Text("No hay horarios registrados.", color = Color.Gray)
        } else {
            profile.classSchedules.forEach { schedule ->
                EditableClassScheduleItem(
                    schedule = schedule,
                    onEditSchedule = onOpenDialogToEditSchedule
                )
                HorizontalDivider(color = Color(0xFF3F4042), modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun EditableClassScheduleItem(schedule: ClassSchedule, onEditSchedule: (String) -> Unit) {
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { schedule.id.let(onEditSchedule) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .background(Color(0xFF292929), shape = CircleShape)
                .padding(12.dp)
                .size(20.dp),
            painter = painterResource(id = R.drawable.icon_calendar),
            contentDescription = "Ícono de Calendario",
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = schedule.courseName,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = schedule.locationName,
                fontSize = 14.sp,
                color = Color.LightGray
            )
            Spacer(Modifier.height(4.dp))
            Row {
                Text(
                    text = "${schedule.startedAt.format(timeFormatter)} – ${schedule.endedAt.format(timeFormatter)}",
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