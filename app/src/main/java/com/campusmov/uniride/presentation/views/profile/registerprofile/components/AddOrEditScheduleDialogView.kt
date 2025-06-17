package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.campusmov.uniride.domain.profile.model.EDay
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.components.TimePickerInputField
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddOrEditScheduleDialogView(
    onDismissRequest: () -> Unit,
    viewModel: RegisterProfileViewModel
) {
    val dialog = viewModel.state.value.scheduleDialogState
    val preds  = viewModel.locationPredictions.collectAsState().value

    // buffer local para el texto del input
    var locRaw by rememberSaveable { mutableStateOf(dialog.selectedLocation?.address.orEmpty()) }
    LaunchedEffect(dialog) {
        locRaw = dialog.selectedLocation?.address.orEmpty()
    }

    val isDialogValid =
        dialog.courseName.isNotBlank()
                && dialog.startedAt    != null
                && dialog.endedAt      != null
                && dialog.selectedDay  != null
                && dialog.selectedLocation != null

    Dialog(
        onDismissRequest = {
            viewModel.onCancelScheduleDialog()
            onDismissRequest()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside   = true
        )
    ) {
        AnimatedVisibility(
            visible = true,
            enter   = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
            exit    = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Black, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // — Header —
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text     = if (dialog.isEditing) "Actualizar horario" else "Agregar horario",
                        fontSize = 20.sp,
                        color    = Color.White
                    )
                    IconButton(onClick = {
                        viewModel.onCancelScheduleDialog()
                        onDismissRequest()
                    }) {
                        Icon(Icons.Rounded.Close, contentDescription = null, tint = Color.White)
                    }
                }

                // — Curso —
                Text("Nombre del curso", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value         = dialog.courseName,
                    onValueChange = { viewModel.onScheduleCourseNameInput(it) },
                    placeholder   = "Matemática básica"
                )

                // — Inicio —
                TimePickerInputField(
                    label           = "Horario de inicio",
                    time            = dialog.startedAt?.toLocalTime(),
                    onTimeSelected  = { lt -> viewModel.onScheduleStartTimeInput(LocalDate.now().atTime(lt)) }
                )

                // — Salida —
                TimePickerInputField(
                    label           = "Hora de salida",
                    time            = dialog.endedAt?.toLocalTime(),
                    onTimeSelected  = { lt -> viewModel.onScheduleEndTimeInput(LocalDate.now().atTime(lt)) }
                )

                // — Día de la semana —
                Text("Día de la semana", fontSize = 16.sp, color = Color.White)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        EDay.MONDAY, EDay.TUESDAY, EDay.WEDNESDAY,
                        EDay.THURSDAY, EDay.FRIDAY, EDay.SATURDAY, EDay.SUNDAY
                    ).forEach { day ->
                        val sel = dialog.selectedDay == day
                        Box(
                            Modifier
                                .size(36.dp)
                                .background(if (sel) Color(0xFF3F4042) else Color.Transparent, CircleShape)
                                .clickable { viewModel.onScheduleDaySelected(day) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day.name.first().toString(), fontSize = 14.sp, color = Color.White)
                        }
                    }
                }

// Universidad ubicación
                Text("Universidad ubicación", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value             = locRaw,
                    onValueChange     = { txt ->
                        locRaw = txt
                        if (txt.isBlank()) {
                            viewModel.onScheduleLocationCleared()
                        } else {
                            viewModel.onScheduleLocationQueryChange(txt)
                        }
                    },
                    placeholder       = "Surco, Primavera 2653",
                    enableLeadingIcon = true,
                )

                AnimatedVisibility(visible = dialog.selectedLocation == null) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(top = 16.dp)
                    ) {
                        itemsIndexed(preds) { index, pred ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onScheduleLocationSelected(pred)
                                        locRaw = pred.fullText
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(pred.fullText, fontSize = 14.sp, color = Color.White)
                            }
                            if (index < preds.lastIndex) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color     = Color.White,
                                    modifier  = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }

                DefaultRoundedTextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text     = if (dialog.isEditing) "Actualizar" else "Agregar",
                    enabled  = isDialogValid,
                    onClick  = {
                        viewModel.addOrUpdateSchedule()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}