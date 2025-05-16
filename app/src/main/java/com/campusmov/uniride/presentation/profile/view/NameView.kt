package com.campusmov.uniride.presentation.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.campusmov.uniride.presentation.components.DefaultRoundedInputField
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.profile.viewmodel.ProfileViewModel

@Composable
fun NameView(
    onBack: () -> Unit,
    onNext: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val ui = viewModel.personalInfo.value
    val valid = ui.firstName.isNotBlank() && ui.lastName.isNotBlank()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            color = Color.Black
        ) {
            Column {
                IconButton(
                    onClick = {
                        onBack()
                    },
                    modifier = Modifier.padding(vertical = 50.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(Color.Black)

                ) {
                    Text(
                        "¿Cuál es tu nombre?",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 24.sp
                    )
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Spacer(Modifier.height(50.dp))

                    DefaultRoundedInputField(
                        value = ui.firstName,
                        onValueChange = { viewModel.updatePersonal { copy(firstName = it) } },
                        placeholder = "Nombre",
                        Modifier.padding(vertical = 20.dp)
                    )

                    DefaultRoundedInputField(
                        value = ui.lastName,
                        onValueChange = { viewModel.updatePersonal { copy(lastName = it) } },
                        placeholder = "Apellido",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(Modifier.weight(1f))

                    DefaultRoundedTextButton(
                        text = "Siguiente →",
                        enabled = valid,
                        onClick = onNext,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp)
                    )
                }
            }
        }
    }
}
