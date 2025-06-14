package com.campusmov.uniride.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DefaultRoundedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    enabledRightIcon : Boolean = false,
    rightIcon: ImageVector? = null,
){
    TextButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC4C4C4),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(12.dp),
            text = text,
            style = TextStyle(
                lineHeight = (40.sp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        if (enabledRightIcon) {
            if (rightIcon != null) {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    tint = Color.Black
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}