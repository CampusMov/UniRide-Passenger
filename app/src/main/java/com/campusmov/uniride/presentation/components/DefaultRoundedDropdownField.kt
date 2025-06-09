package com.campusmov.uniride.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.campusmov.uniride.domain.profile.model.EGender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultRoundedDropdownField(
    options: List<String>,
    selectedOption: EGender?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded = remember { mutableStateOf(false) }
    var selectedOptionText = remember { mutableStateOf(selectedOption?.toString()) }
    var textFieldSize = remember { mutableStateOf(Size.Zero) }

    val icon = if (!expanded.value) {
        Icons.Filled.ArrowDropDown
    } else {
        Icons.Filled.ArrowDropUp
    }

    Column(
        modifier = modifier
            .background(color = Color(0xFF3F4042), shape = RoundedCornerShape(12.dp))
            .padding(3.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize.value = coordinates.size.toSize()
                },
            value = selectedOptionText.value.toString(),
            onValueChange = {},
            readOnly = true,
            enabled = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = "Selecciona tu genero",
                    color = Color(0xFFB3B3B3)
                )
            },
            trailingIcon = {
                IconButton(onClick = {expanded.value = !expanded.value}) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Seleccionar genero",
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
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .fillMaxWidth()
                .width(with(LocalDensity.current) { textFieldSize.value.width.toDp() })
        ) {
            options.forEach { label ->
                DropdownMenuItem(text = { Text(text = label) }, onClick = {
                    selectedOptionText.value = label
                    onOptionSelected(label)
                    expanded.value = false
                })
            }
        }
    }
}