package com.campusmov.uniride.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRoundedDateInputField(
    modifier: Modifier = Modifier,
    selectedDateMillis: Long?,
    onDateSelected: (Long?) -> Unit,
    showModeToggle: Boolean = false
) {
    var showPicker = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)

    val formatted = selectedDateMillis
        ?.let { convertMillisToSpanishDate(it) }
        .orEmpty()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        onDateSelected(datePickerState.selectedDateMillis)
    }

    Box(
        modifier = modifier
            .background(color = Color(0xFF3F4042), shape = RoundedCornerShape(12.dp))
            .padding(3.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = formatted,
            onValueChange = {},
            readOnly = true,
            enabled = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = "Selecciona fecha",
                    color = Color(0xFFB3B3B3)
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPicker.value = !showPicker.value }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha",
                        tint = Color(0xFFB3B3B3)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFB3B3B3),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color(0xFF3F4042),
                disabledTextColor = Color.White
            ),
        )

        if (showPicker.value) {
            Popup(
                onDismissRequest = { showPicker.value = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = showModeToggle
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToSpanishDate(millis: Long): String {
    val date = Instant
        .ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val day = date.dayOfMonth
        .toString()
        .padStart(2, '0')
    val month = date.month
        .getDisplayName(TextStyle.FULL, Locale("es", "ES"))
        .replaceFirstChar { it.uppercase() }
    val year = date.year

    return "$day de $month del $year"
}