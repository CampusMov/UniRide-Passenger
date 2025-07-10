package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterProfileAcceptTermsView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val isValid = viewModel.isTermsAcceptedValid

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .background(Color.Transparent)
                        .size(31.dp),
                    onClick = { navHostController.popBackStack() },
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Icon of go back",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier
                            .size(120.dp),
                        painter = painterResource(id = R.drawable.terms_icon),
                        contentDescription = "Icon of terms",
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Acepta los términos de UniRide y revisa el aviso de privacidad",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 38.sp,
                    )
                }
                Text(
                    text = "Al seleccionar \"Acepto\" a continuación, he revisado y acepto las Condiciones de uso de y acepto el Aviso de privacidad. Soy mayor de 18 años.",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.2f),
                    thickness = 2.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onTermsAccepted(!viewModel.isTermsAccepted.value) }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Estoy de acuerdo.",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Checkbox(
                        checked = viewModel.isTermsAccepted.value,
                        onCheckedChange = { viewModel.onTermsAccepted(it) },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = Color.Black,
                            checkedColor = Color.White,
                            uncheckedColor = Color.White
                        )
                    )
                }
                DefaultRoundedTextButton(
                    modifier = Modifier
                        .fillMaxWidth(0.6f),
                    text = "Siguiente",
                    enabled = isValid.value,
                    enabledRightIcon = true,
                    onClick = {
                        viewModel.onNextStep(-1)
                        navHostController.navigate(ProfileScreen.RegisterProfileListItems.route)
                    }
                )
            }
        }
    }
}
