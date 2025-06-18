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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.campusmov.uniride.domain.shared.model.EDay
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.components.TimePickerInputField
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClassScheduleDialogView(
    viewModel: RegisterProfileViewModel
) {
    val currentClassScheduleState = viewModel.currentClassScheduleState.value
    val isCurrentClassScheduleValid = viewModel.isCurrentClassScheduleValid.value
    val locationPredictions = viewModel.locationPredictions.collectAsState().value

    var locRaw = rememberSaveable { mutableStateOf(currentClassScheduleState.selectedLocation?.address.orEmpty()) }

    LaunchedEffect(currentClassScheduleState) {
        locRaw.value = currentClassScheduleState.selectedLocation?.address.orEmpty()
    }

    Dialog(
        onDismissRequest = {
            viewModel.onCloseScheduleDialog()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true
        )
    ) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(300)),
            exit = slideOutVertically(targetOffsetY = { it }, animationSpec = tween(300))
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.9f)
                    .background(Color.Black, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, bottom = 21.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(
                        modifier = Modifier
                            .size(50.dp)
                            .background(Color.Transparent)

                    )
                    Text(
                        text = if (currentClassScheduleState.isEditing) "Actualizar horario" else "Agregar horario",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(start = 16.dp),
                        lineHeight = 40.sp
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3F4042)),
                        onClick = {
                            viewModel.onCloseScheduleDialog()
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(40.dp),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Text("Nombre del curso", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value = currentClassScheduleState.courseName,
                    onValueChange = { viewModel.onScheduleCourseNameInput(it) },
                    placeholder = "Matemática básica"
                )

                Text("Hora de inicio", fontSize = 16.sp, color = Color.White)

                TimePickerInputField(
                    time = currentClassScheduleState.startedAt,
                    onTimeChange = { lt -> viewModel.onScheduleStartTimeInput(lt) }
                )

                Text("Hora de salida", fontSize = 16.sp, color = Color.White)

                TimePickerInputField(
                    time = currentClassScheduleState.endedAt,
                    onTimeChange = { lt -> viewModel.onScheduleEndTimeInput(lt) }
                )
                Text("Día de la semana", fontSize = 16.sp, color = Color.White)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        EDay.MONDAY, EDay.TUESDAY, EDay.WEDNESDAY,
                        EDay.THURSDAY, EDay.FRIDAY, EDay.SATURDAY, EDay.SUNDAY
                    ).forEach { day ->
                        val sel = currentClassScheduleState.selectedDay == day
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

                Text("Universidad ubicación", fontSize = 16.sp, color = Color.White)
                DefaultRoundedInputField(
                    value = locRaw.value,
                    onValueChange = { location ->
                        locRaw.value = location
                        if (location.isBlank()) {
                            viewModel.onScheduleLocationCleared()
                        } else {
                            viewModel.onScheduleLocationQueryChange(location)
                        }
                    },
                    placeholder = "Surco, Primavera 2653",
                    enableLeadingIcon = true,
                )

                AnimatedVisibility(visible = currentClassScheduleState.selectedLocation == null) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(top = 16.dp)
                    ) {
                        itemsIndexed(locationPredictions) { index, prediction ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onScheduleLocationSelected(prediction)
                                        locRaw.value = prediction.fullText
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(prediction.fullText, fontSize = 14.sp, color = Color.White)
                            }
                            if (index < locationPredictions.lastIndex) {
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = Color.White,
                                    modifier  = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
                if (isCurrentClassScheduleValid) {
                    if(currentClassScheduleState.isEditing){
                        DefaultRoundedTextButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Actualizar",
                            onClick  = { viewModel.editExistingClassSchedule() }
                        )
                        Text(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onDeleteSchedule()
                                },
                            text = "Eliminar horario",
                            fontSize = 12.sp,
                            style = TextStyle(
                                textDecoration = TextDecoration.Underline
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        DefaultRoundedTextButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Agregar",
                            onClick  = { viewModel.addClasScheduleToProfile() }
                        )
                    }
                }
            }
        }
    }
}