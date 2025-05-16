package com.campusmov.uniride.presentation.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsView(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val ui = viewModel.terms.value

    var isExpanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color.Black)
            .padding(7.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 150.dp)
        ) {
            Spacer(Modifier.height(15.dp))
            TopAppBar(
                title = { Text("Términos y privacidad", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
            Spacer(Modifier.height(40.dp))
            Column(modifier = Modifier.padding(horizontal = 35.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, null, tint = Color(0xFF4AC8EA), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Acepta términos y privacidad",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(Modifier.height(16.dp))

                Text(
                    "Al aceptar has leído y estás de acuerdo con los Términos de uso y el Aviso de privacidad.",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isExpanded.value) "Aquí van los Términos y Condiciones completos, que pueden incluir varias líneas de texto explicando el acuerdo, la privacidad, etc."
                        else "Aquí van los Términos y Condiciones... (más)",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpanded.value) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { isExpanded.value = !isExpanded.value }) {
                        Text(
                            text = if (isExpanded.value) "Ver menos" else "Ver más",
                            color = Color(0xFF4AC8EA) // Cambia el color según preferencia
                        )
                    }
                }

                Spacer(Modifier.height(24.dp)) // Añadido espaciado antes del checkbox

                Divider(color = Color.White.copy(.2f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateTerms { copy(accepted = !accepted) } }
                        .padding(16.dp), // Espaciado adicional para evitar que el contenido se quede pegado a los bordes
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = ui.accepted,
                        onCheckedChange = null,
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.Black,
                            checkedColor = Color.White,
                            uncheckedColor = Color.White
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Estoy de acuerdo.", color = Color.White)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            DefaultRoundedTextButton(
                text = "Siguiente →",
                enabled = ui.accepted,
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().padding(50.dp)
            )
        }
    }
}
