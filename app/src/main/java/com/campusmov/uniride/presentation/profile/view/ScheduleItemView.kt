package com.campusmov.uniride.presentation.profile.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.campusmov.uniride.presentation.profile.model.ClassScheduleUiModel

@Composable
fun ScheduleItemView(
    schedule: ClassScheduleUiModel,
    onEdit:    () -> Unit,
    onDelete:  () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(schedule.name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text("${schedule.start} – ${schedule.end}", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            Text(
                text = when (schedule.dayOfWeek) {
                    'L'.toString() -> "Lunes"; 'M'.toString() -> "Martes"; 'X'.toString() -> "Miércoles"
                    'J'.toString() -> "Jueves"; 'V'.toString() -> "Viernes"; 'S'.toString() -> "Sábado"
                    'D'.toString() -> "Domingo"; else -> schedule.dayOfWeek.toString()
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}