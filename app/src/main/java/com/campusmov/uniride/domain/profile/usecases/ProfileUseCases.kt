package com.campusmov.uniride.domain.profile.usecases

data class ProfileUseCases (
    val saveProfile : SaveProfileUseCase,
    val getProfileById: GetProfileByIdUseCase,
    val updateProfile: UpdateProfileUsecase,
    val deleteLocalProfiles: DeleteLocalProfiles,
)