import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.profile.model.ClassScheduleUiModel
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ScheduleDialogView(
    schedule: ClassScheduleUiModel? = null,
    onSave: () -> Unit, // no pasa el objeto, se usa el viewModel
    onCancel: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Si estamos editando, obtenemos el horario actual del viewModel; si no, usamos el param o uno nuevo vacío
    val editingSchedule = viewModel.editingSchedule.value ?: schedule

    // Estado local para campos de texto, inicializado desde editingSchedule o vacíos
    var name by remember { mutableStateOf(editingSchedule?.name ?: "") }
    var address by remember { mutableStateOf(editingSchedule?.address ?: "") }
    var locationName by remember { mutableStateOf(editingSchedule?.locationName ?: "") }

    // dayOfWeek es String completo ("Monday", "Tuesday", ...) — guardamos como String para evitar confusión
    var dayOfWeek by remember { mutableStateOf(editingSchedule?.dayOfWeek ?: "Monday") }

    // Convertimos fechas ISO a LocalDateTime para manejar DatePickers/TimePickers
    var startDateTime by remember {
        mutableStateOf(editingSchedule?.start?.let { parseISOToLocalDateTime(it) } ?: LocalDateTime.now())
    }
    var endDateTime by remember {
        mutableStateOf(editingSchedule?.end?.let { parseISOToLocalDateTime(it) } ?: LocalDateTime.now().plusHours(1))
    }

    val valid by derivedStateOf {
        name.isNotBlank() &&
                address.isNotBlank() &&
                locationName.isNotBlank() &&
                startDateTime.isBefore(endDateTime)
    }

    val days = listOf(
        "Monday" to 'L',
        "Tuesday" to 'M',
        "Wednesday" to 'X',
        "Thursday" to 'J',
        "Friday" to 'V',
        "Saturday" to 'S',
        "Sunday" to 'D'
    )

    // DatePickers y TimePickers configurados
    val startDatePicker = DatePickerDialog(
        context,
        { _, y, m, d -> startDateTime = startDateTime.withYear(y).withMonth(m + 1).withDayOfMonth(d) },
        startDateTime.year, startDateTime.monthValue - 1, startDateTime.dayOfMonth
    )
    val startTimePicker = TimePickerDialog(
        context,
        { _, h, min -> startDateTime = startDateTime.withHour(h).withMinute(min).withSecond(0).withNano(0) },
        startDateTime.hour, startDateTime.minute, true
    )
    val endDatePicker = DatePickerDialog(
        context,
        { _, y, m, d -> endDateTime = endDateTime.withYear(y).withMonth(m + 1).withDayOfMonth(d) },
        endDateTime.year, endDateTime.monthValue - 1, endDateTime.dayOfMonth
    )
    val endTimePicker = TimePickerDialog(
        context,
        { _, h, min -> endDateTime = endDateTime.withHour(h).withMinute(min).withSecond(0).withNano(0) },
        endDateTime.hour, endDateTime.minute, true
    )

    fun formatISO(dateTime: LocalDateTime): String =
        dateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)

    AlertDialog(
        onDismissRequest = onCancel,
        containerColor = Color.Black,
        title = {
            Text(
                text = if (editingSchedule == null) "Agregar horario" else "Editar horario",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column {
                DefaultRoundedInputField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Nombre del curso"
                )
                Spacer(Modifier.height(12.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { startDatePicker.show() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222))
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Fecha Inicio", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                            Text(startDateTime.toLocalDate().toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { startTimePicker.show() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222))
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Hora Inicio", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                            Text(startDateTime.toLocalTime().toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { endDatePicker.show() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222))
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Fecha Fin", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                            Text(endDateTime.toLocalDate().toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { endTimePicker.show() },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF222222))
                    ) {
                        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Hora Fin", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                            Text(endDateTime.toLocalTime().toString(), color = Color.White, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                DefaultRoundedInputField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = "Dirección"
                )
                Spacer(Modifier.height(8.dp))
                DefaultRoundedInputField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    placeholder = "Ubicación"
                )

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    days.forEach { (fullName, letter) ->
                        Text(
                            text = letter.toString(),
                            color = if (dayOfWeek == fullName) Color.Black else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { dayOfWeek = fullName }
                                .background(
                                    color = if (dayOfWeek == fullName) Color.White else Color.DarkGray,
                                    shape = CircleShape
                                )
                                .padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedSchedule = ClassScheduleUiModel(
                        name = name,
                        start = formatISO(startDateTime),
                        end = formatISO(endDateTime),
                        dayOfWeek = dayOfWeek,
                        address = address,
                        locationName = locationName
                    )
                    if (editingSchedule == null) {
                        viewModel.addSchedule(updatedSchedule)
                    } else {
                        viewModel.finishEditingSchedule(updatedSchedule)
                    }
                    onSave()
                },
                enabled = valid
            ) {
                Text("Guardar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

fun parseISOToLocalDateTime(isoString: String): LocalDateTime {
    return try {
        LocalDateTime.parse(isoString, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        LocalDateTime.now()
    }
}
