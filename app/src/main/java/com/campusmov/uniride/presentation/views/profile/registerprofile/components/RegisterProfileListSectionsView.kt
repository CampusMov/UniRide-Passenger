package com.campusmov.uniride.presentation.views.profile.registerprofile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.navigation.screen.profile.ProfileScreen
import com.campusmov.uniride.presentation.util.NavigationItem
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileViewModel

@Composable
fun RegisterProfileListSectionsView(
    navHostController: NavHostController,
    viewModel: RegisterProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val nextRecommendedStep = viewModel.nextRecommendedStep.intValue

    val items = listOf(
        NavigationItem(
            title = "Información personal",
            route = ProfileScreen.RegisterProfilePersonalInfo.route,
            selectedIcon = Icons.Default.ChevronRight,
            unselectedIcon = Icons.Default.ChevronRight
        ),
        NavigationItem(
            title = "Información de contacto",
            route = ProfileScreen.RegisterProfileContactInfo.route,
            selectedIcon = Icons.Default.ChevronRight,
            unselectedIcon = Icons.Default.ChevronRight
        ),
        NavigationItem(
            title = "Información académica",
            route = ProfileScreen.RegisterProfileAcademicInfo.route,
            selectedIcon = Icons.Default.ChevronRight,
            unselectedIcon = Icons.Default.ChevronRight
        ),
        NavigationItem(
            title = "Términos y condiciones",
            route = ProfileScreen.RegisterProfileAcceptTerms.route,
            selectedIcon = Icons.Default.ChevronRight,
            unselectedIcon = Icons.Default.ChevronRight
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 30.dp, horizontal = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    textAlign = TextAlign.Start,
                    text = "Bienvenido, ${state.firstName}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 20.dp),
                    textAlign = TextAlign.Start,
                    text = "Esto es lo que debe hacer para configurar su cuenta.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                items.forEachIndexed { index, item ->
                    RegisterProfileItemSection(
                        item = item,
                        navHostController = navHostController,
                        currentPosition = index,
                        nextRecommendedPosition = nextRecommendedStep
                    )
                    if (index != items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(),
                            color = Color(0xFF2E2E2E),
                            thickness = 1.dp
                        )
                    }
                }
            }

            DefaultRoundedTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                text = "¡Comenzar!",
                onClick = {
                    viewModel.saveProfile()
                },
            )
        }
    }
}

@Composable
fun RegisterProfileItemSection(item: NavigationItem, navHostController: NavHostController, currentPosition: Int, nextRecommendedPosition: Int){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clickable() {
                navHostController.navigate(item.route)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            if (currentPosition == nextRecommendedPosition) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Siguiente paso recomendado",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA2B9E4)
                )
            }
        }
        Icon(
            imageVector = item.selectedIcon,
            contentDescription = "go to ${item.title}",
            tint = Color.White
        )
    }
}