package com.campusmov.uniride.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun DefaultRoundedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enable: Boolean = true,
    enableLeadingIcon: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    Box(
        modifier = modifier
            .background(color = Color(0xFF3F4042), shape = RoundedCornerShape(12.dp))
            .padding(3.dp)
    ) {
        OutlinedTextField(
            value = value,
            enabled = enable,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color(0xFFB3B3B3)
                )
            },
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFB3B3B3),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color(0xFF3F4042),
                focusedContainerColor = Color(0xFF3F4042),
                unfocusedContainerColor = Color(0xFF3F4042),
                disabledContainerColor = Color(0xFF3F4042),
                disabledTextColor = Color.White
            ),
            leadingIcon = if (enableLeadingIcon) {
                {
                    Icon(
                        imageVector = leadingIcon ?: Icons.Default.Search,
                        contentDescription = "Icono de búsqueda",
                        tint = Color(0xFFB3B3B3)
                    )
                }
            } else null,
            trailingIcon = if (enable) {
                {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = Color(0xFFB3B3B3)
                            )
                        }
                    }
                }
            } else null
        )
    }
}
