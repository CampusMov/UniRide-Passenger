package com.campusmov.uniride.presentation.views.profile.info

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.presentation.components.DefaultRoundedTextButton
import com.campusmov.uniride.presentation.views.profile.info.components.AcademicInfoSection
import com.campusmov.uniride.presentation.views.profile.info.components.ContactInfoSection
import com.campusmov.uniride.presentation.views.profile.info.components.PersonalInfoSection
import com.campusmov.uniride.presentation.views.profile.registerprofile.RegisterProfileState
import com.campusmov.uniride.presentation.views.profile.registerprofile.components.ClassScheduleDialogView

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileInfoView(
    navHostController: NavHostController,
    viewModel: ProfileInfoViewModel = hiltViewModel()
) {
    val editableProfileState = viewModel.profileState.value
    val isLoading = viewModel.isLoading.collectAsState().value
    val hasChanges = viewModel.hasChanges.value
    val isScheduleDialogOpened = viewModel.isScheduleDialogOpen.value
    val currentUser = viewModel.user.collectAsState().value

    LaunchedEffect(currentUser) {
        currentUser?.id?.let { userId ->
            viewModel.fetchProfileForEditing(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading && editableProfileState.userId.isBlank() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                !isLoading && editableProfileState.userId.isBlank() -> {
                    Text(
                        "No se encontraron datos del perfil.",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                editableProfileState.userId.isNotBlank() -> {
                    ProfileDetails(
                        profile = editableProfileState,
                        viewModel = viewModel
                    )

                    if (hasChanges) {
                        DefaultRoundedTextButton(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            text = if (isLoading) "Guardando..." else "Guardar cambios",
                            onClick = { viewModel.updateProfile() },
                            enabled = !isLoading
                        )
                    }
                }
            }
        }
        if (isScheduleDialogOpened) {
            ClassScheduleDialogView(viewModel = viewModel)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ProfileDetails(profile: RegisterProfileState, viewModel: ProfileInfoViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            PersonalInfoSection(
                profile = profile,
                onFirstNameInput = viewModel::onFirstNameInput,
                onLastNameInput = viewModel::onLastNameInput,
                onBirthDateInput = viewModel::onBirthDateInput,
                onGenderInput = viewModel::onGenderInput,
                uploadProfileImage = viewModel::uploadProfileImage
            )
            HorizontalDivider(
                color = Color(0xFF3F4042),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
        item {
            ContactInfoSection(
                profile = profile,
                onPersonalEmailAddressInput = viewModel::onPersonalEmailAddressInput,
                onPhoneNumberInput = viewModel::onPhoneNumberInput
            )
            HorizontalDivider(
                color = Color(0xFF3F4042),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 20.dp)
            )
        }
        item {
            AcademicInfoSection(
                profile = profile,
                onUniversityInput = viewModel::onUniversityInput,
                onFacultyInput = viewModel::onFacultyInput,
                onAcademicProgramInput = viewModel::onAcademicProgramInput,
                onSemesterInput = viewModel::onSemesterInput,
                onOpenDialogToAddNewSchedule = viewModel::onOpenDialogToAddNewSchedule,
                onOpenDialogToEditSchedule = viewModel::onOpenDialogToEditSchedule
            )
        }
        item {
            Column(modifier = Modifier.height(80.dp)) {}
        }
    }
}