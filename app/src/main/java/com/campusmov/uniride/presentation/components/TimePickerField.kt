package com.campusmov.uniride.presentation.components

import android.app.TimePickerDialog
import android.os.Build
import android.view.ContextThemeWrapper
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.campusmov.uniride.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerInputField(
    modifier: Modifier = Modifier,
    time: LocalTime?,
    onTimeChange: (LocalTime) -> Unit,
    placeholder: String = "Selecciona hora",
    enabled: Boolean = true
) {
    val context = LocalContext.current
    val formatted = time?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: ""
    var showPicker = remember { mutableStateOf(false) }
    val openTimePicker = {
        if (enabled) {
            showPicker.value = true
        }
    }

    if (showPicker.value) {
        val hour = time?.hour ?: LocalTime.now().hour
        val minute = time?.minute ?: LocalTime.now().minute

        val themedContext = ContextThemeWrapper(context, R.style.CustomTimePickerTheme)

        val timePickerDialog = TimePickerDialog(
            themedContext,
            { _, h, m ->
                onTimeChange(LocalTime.of(h, m))
                showPicker.value = false
            },
            hour,
            minute,
            true
        )

        timePickerDialog.setOnCancelListener {
            showPicker.value = false
        }

        timePickerDialog.setOnDismissListener {
            showPicker.value = false
        }

        timePickerDialog.show()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF3F4042),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(3.dp)
    ) {
        OutlinedTextField(
            value = formatted,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth(),
            enabled = false,
            readOnly = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFB3B3B3)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFB3B3B3),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color(0xFF3F4042),
                unfocusedContainerColor = Color(0xFF3F4042),
                disabledContainerColor = Color(0xFF3F4042),
                disabledTextColor = Color(0xFFB3B3B3)
            ),
            leadingIcon = null,
            trailingIcon = {
                IconButton(
                    onClick = openTimePicker,
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = "Seleccionar hora",
                        tint = Color.White
                    )
                }
            }
        )
    }
}
