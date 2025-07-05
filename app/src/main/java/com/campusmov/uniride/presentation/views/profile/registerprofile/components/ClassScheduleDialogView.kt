package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.campusmov.uniride.presentation.views.profile.registerprofile.ProfileInfoViewModel
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClassScheduleDialogView(
    viewModel: Any?
) {
    val registerVM = viewModel as? RegisterProfileViewModel
    val profileVM = viewModel as? ProfileInfoViewModel

    val currentClassScheduleState = registerVM?.currentClassScheduleState?.value
        ?: profileVM?.currentClassScheduleState?.value
        ?: return

    val isCurrentClassScheduleValid = registerVM?.isCurrentClassScheduleValid?.value
        ?: profileVM?.isCurrentClassScheduleValid?.value
        ?: false

    val locationPredictions = registerVM?.locationPredictions?.collectAsState()?.value
        ?: profileVM?.locationPredictions?.collectAsState()?.value
        ?: emptyList()

    var locRaw = rememberSaveable { mutableStateOf(currentClassScheduleState.selectedLocation?.address.orEmpty()) }

    LaunchedEffect(currentClassScheduleState) {
        locRaw.value = currentClassScheduleState.selectedLocation?.address.orEmpty()
    }

    Dialog(
        onDismissRequest = {
            registerVM?.onCloseScheduleDialog() ?: profileVM?.onCloseScheduleDialog()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically { fullHeight -> fullHeight },
                exit = slideOutVertically { fullHeight -> fullHeight }
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 1f)
                        .background(
                            Color.Black,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
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
                                .size(40.dp)
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
                                .padding(start = 10.dp),
                            lineHeight = 40.sp
                        )
                        IconButton(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3F4042)),
                            onClick = {
                                registerVM?.onCloseScheduleDialog() ?: profileVM?.onCloseScheduleDialog()
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
                        onValueChange = {
                            registerVM?.onScheduleCourseNameInput(it)
                                ?: profileVM?.onScheduleCourseNameInput(it)
                        },
                        placeholder = "Matemática básica"
                    )

                    Text("Hora de inicio", fontSize = 16.sp, color = Color.White)
                    TimePickerInputField(
                        time = currentClassScheduleState.startedAt,
                        onTimeChange = { lt ->
                            registerVM?.onScheduleStartTimeInput(lt)
                                ?: profileVM?.onScheduleStartTimeInput(lt)
                        }
                    )

                    Text("Hora de salida", fontSize = 16.sp, color = Color.White)
                    TimePickerInputField(
                        time = currentClassScheduleState.endedAt,
                        onTimeChange = { lt ->
                            registerVM?.onScheduleEndTimeInput(lt)
                                ?: profileVM?.onScheduleEndTimeInput(lt)
                        }
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
                                    .background(
                                        if (sel) Color(0xFF3F4042) else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable {
                                        registerVM?.onScheduleDaySelected(day)
                                            ?: profileVM?.onScheduleDaySelected(day)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    day.showDay().first().toString(),
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Text("Universidad ubicación", fontSize = 16.sp, color = Color.White)
                    DefaultRoundedInputField(
                        value = locRaw.value,
                        onValueChange = { location ->
                            locRaw.value = location
                            if (location.isBlank()) {
                                registerVM?.onScheduleLocationCleared()
                                    ?: profileVM?.onScheduleLocationCleared()
                            } else {
                                registerVM?.onScheduleLocationQueryChange(location)
                                    ?: profileVM?.onScheduleLocationQueryChange(location)
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
                                            registerVM?.onScheduleLocationSelected(prediction)
                                                ?: profileVM?.onScheduleLocationSelected(prediction)
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
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                    if (isCurrentClassScheduleValid) {
                        if (currentClassScheduleState.isEditing) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                DefaultRoundedTextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Actualizar",
                                    onClick = {
                                        registerVM?.editExistingClassSchedule()
                                            ?: profileVM?.editExistingClassSchedule()
                                    }
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(top = 15.dp, bottom = 10.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            registerVM?.onDeleteSchedule()
                                                ?: profileVM?.onDeleteSchedule()
                                        },
                                    text = "Eliminar horario",
                                    style = TextStyle(
                                        textDecoration = TextDecoration.Underline,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        lineHeight = 10.sp
                                    ),
                                )
                            }
                        } else {
                            DefaultRoundedTextButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                text = "Agregar",
                                onClick = {
                                    registerVM?.addClasScheduleToProfile()
                                        ?: profileVM?.addClasScheduleToProfile()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}