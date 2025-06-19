package com.campusmov.uniride.presentation.components

import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DefaultRoundedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    enabledRightIcon : Boolean = false,
    rightIcon: ImageVector? = null,
    isLoading: Boolean = false,
    loadingText: String? = null
){
    var dotCount = remember { mutableIntStateOf(0) }

    LaunchedEffect(isLoading) {
        while (isLoading) {
            dotCount.intValue = (dotCount.intValue % 3) + 1
            delay(500)
        }
        dotCount.intValue = 0
    }

    TextButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFC4C4C4),
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFF8C8C8C),
            disabledContentColor = Color.Black,
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isLoading) {
                Text(
                    text = "$loadingText${".".repeat(dotCount.intValue)}",
                    style = TextStyle(
                        lineHeight = 40.sp,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.weight(1f).padding(12.dp)
                )
            } else {
                Text(
                    text = text,
                    style = TextStyle(
                        lineHeight = 40.sp,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.weight(1f).padding(12.dp)
                )
                if (enabledRightIcon) {
                    Icon(
                        imageVector = rightIcon ?: Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}