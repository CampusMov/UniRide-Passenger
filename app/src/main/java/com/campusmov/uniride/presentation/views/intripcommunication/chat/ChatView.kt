package com.campusmov.uniride.presentation.views.intripcommunication.chat


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.domain.intripcommunication.model.Message
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDialog(
    passengerId: String,
    carpoolId: String,
    senderId: String,
    recipientName: String,
    recipientVehicle: String,
    show: Boolean,
    onDismiss: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    if (!show) return

    LaunchedEffect(show, passengerId, carpoolId) {
        if (show) {
            viewModel.openChat(passengerId, carpoolId)
        }
    }

    val chat by viewModel.chat.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val newText by viewModel.newMessageText.collectAsState()
    val isSending by viewModel.isSending.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.error.collectLatest { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.closeChatSession()
        }
    }

    val dateHeader = remember(messages) {
        messages.firstOrNull()?.sentAt?.takeIf { it.length >= 19 }
            ?.let {
                try {
                    LocalDateTime.parse(it.substring(0, 19))
                        .format(DateTimeFormatter.ofPattern("d 'de' MMMM, h:mm a", Locale("es")))
                        .replace("AM", "a.m.").replace("PM", "p.m.")
                } catch (e: Exception) {
                    null
                }
            } ?: ""
    }

    if (chat == null && show) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }


    if (show && chat != null) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                Modifier
                    .fillMaxSize()
                    .imePadding()
                    .background(Color(0x88000000)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF121212)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(recipientName, color = Color.White, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(4.dp))
                                Text(recipientVehicle, color = Color(0xFFBBBBBB), style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        if (dateHeader.isNotEmpty()) {
                            Text(
                                dateHeader,
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                            reverseLayout = false
                        ) {
                            items(messages, key = { it.messageId }) { msg ->
                                MessageBubble(msg, msg.senderId == senderId)
                            }
                        }

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1E1E1E))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = newText,
                                onValueChange = { viewModel.newMessageText.value = it },
                                placeholder = { Text("Mensaje...", color = Color.Gray) },
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 56.dp, max = 120.dp)
                                    .verticalScroll(rememberScrollState()),
                                maxLines = 3,
                                enabled = !isSending,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor   = Color(0xFF2C2C2E),
                                    unfocusedContainerColor = Color(0xFF2C2C2E),
                                    disabledContainerColor  = Color(0xFF2C2C2E),
                                    focusedIndicatorColor   = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor             = Color.White,
                                    focusedTextColor        = Color.White,
                                    unfocusedTextColor      = Color.White,
                                    errorContainerColor = Color(0xFF2C2C2E),
                                    focusedLabelColor = Color.White,
                                    unfocusedLabelColor = Color.Gray,
                                    errorLabelColor = Color.Red,
                                    disabledTextColor = Color.DarkGray,
                                    disabledLabelColor = Color.DarkGray,
                                    disabledLeadingIconColor = Color.DarkGray,
                                    disabledPlaceholderColor = Color.DarkGray,
                                    disabledSupportingTextColor = Color.DarkGray,
                                    disabledTrailingIconColor = Color.DarkGray,
                                ),
                                shape = MaterialTheme.shapes.small
                            )
                            Spacer(Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (chat != null) {
                                        viewModel.sendMessage()
                                    }
                                },
                                enabled = !isSending && newText.isNotBlank() && chat != null
                            ) {
                                Icon(Icons.Filled.Send, contentDescription = "Send", tint = if (!isSending && newText.isNotBlank() && chat != null) Color.White else Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MessageBubble(message: Message, isOwn: Boolean) {
    val time = try {
        LocalDateTime.parse(message.sentAt.substring(0, 19))
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        ""
    }
    val bgColor = if (isOwn) Color(0xFF616161) else Color(0xFF2C2C2E)
    val arrangement = if (isOwn) Arrangement.End else Arrangement.Start

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = arrangement
    ) {
        Column(
            Modifier
                .wrapContentWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(bgColor)
                .padding(12.dp)
        ) {
            Text(message.content, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                time,
                color = Color(0xFFAAAAAA),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}