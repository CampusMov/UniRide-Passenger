package com.campusmov.uniride.presentation.views.routingmatching.searchclassschedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.profile.model.ClassSchedule
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SearchClassScheduleView(
    navHostController: NavHostController,
    viewModel: SearchClassScheduleViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    onClassScheduleSelected: () -> Unit
) {
    val searchQuery = viewModel.searchQuery.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val filteredClassSchedules = viewModel.filteredClassSchedules.collectAsState()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.9f)
                .background(Color.Black, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 21.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Transparent)

                )
                Text(
                    text = "Tus horario de clases",
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
                        onDismissRequest()
                        viewModel.resetClassSchedules()
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

            DefaultRoundedInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = searchQuery.value,
                onValueChange = {
                    viewModel.onSearchQueryChanged(it)
                },
                placeholder = "Seleccionar tu horario de clases",
                enableLeadingIcon = true
            )
            if (isLoading.value) {
                Text(
                    text = "Cargando horarios...",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    itemsIndexed(filteredClassSchedules.value) { index, prediction ->
                        ClassScheduleItem(
                            classSchedule = prediction,
                            onClassScheduleSelected = {
                                viewModel.selectClassSchedule(it)
                                onClassScheduleSelected()
                            }
                        )
                        if (index < filteredClassSchedules.value.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 23.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClassScheduleItem(
    classSchedule: ClassSchedule,
    onClassScheduleSelected: (classSchedule: ClassSchedule) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 23.dp)
            .height(50.dp)
            .clickable { onClassScheduleSelected(classSchedule) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF3F4042))
                .padding(8.dp),
            imageVector = Icons.Rounded.Schedule,
            contentDescription = "Class Schedule Icon",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = classSchedule.courseName,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = classSchedule.scheduleTime(),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}