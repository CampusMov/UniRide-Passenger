package com.campusmov.uniride.di

import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.DeleteAllUsersLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.GetUserByEmailLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.GetUserByIdLocallyUserCase
import com.campusmov.uniride.domain.auth.usecases.GetUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.SaveUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.UpdateUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationCodeUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationEmailUseCase
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.campusmov.uniride.domain.location.usecases.GetLocationsUpdatesUseCase
import com.campusmov.uniride.domain.location.usecases.GetPlaceDetailsUseCase
import com.campusmov.uniride.domain.location.usecases.GetPlacePredictionsUseCase
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.profile.usecases.GetClassSchedulesByProfileIdUseCase
import com.campusmov.uniride.domain.profile.usecases.ProfileClassScheduleUseCases
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.profile.usecases.SaveProfileUseCase
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.SavePassengerRequestUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.SearchCarpoolsAvailableUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideAuthUseCase(authRepository: AuthRepository) = AuthUseCase(
        verifyEmail = VerificationEmailUseCase(authRepository),
        verifyCode = VerificationCodeUseCase(authRepository),
    )

    @Provides
    fun provideLocationUseCases(locationRepository: LocationRepository) = LocationUsesCases(
        getLocationUpdates = GetLocationsUpdatesUseCase(locationRepository),
        getPlacePredictions = GetPlacePredictionsUseCase(locationRepository),
        getPlaceDetails = GetPlaceDetailsUseCase(locationRepository)
    )

    @Provides
    fun provideUserUseCases(userRepository: UserRepository) = UserUseCase(
        saveUserLocallyUseCase = SaveUserLocallyUseCase(userRepository),
        getUserByIdLocallyUserCase = GetUserByIdLocallyUserCase(userRepository),
        getUserByEmailLocallyUseCase = GetUserByEmailLocallyUseCase(userRepository),
        updateUserLocallyUseCase = UpdateUserLocallyUseCase(userRepository),
        getUserLocallyUseCase = GetUserLocallyUseCase(userRepository),
        deleteAllUsersLocallyUseCase = DeleteAllUsersLocallyUseCase(userRepository),
    )

    @Provides
    fun provideProfileUseCases(profileRepository: ProfileRepository) = ProfileUseCases(
        saveProfile = SaveProfileUseCase(profileRepository)
    )

    @Provides
    fun provideProfileClassScheduleUseCases(profileClassScheduleRepository: ProfileClassScheduleRepository) = ProfileClassScheduleUseCases(
        getClassSchedulesByProfileId = GetClassSchedulesByProfileIdUseCase(profileClassScheduleRepository)
    )
    
    @Provides
    fun provideCarpoolUseCases(carpoolRepository: CarpoolRepository) = CarpoolUseCases(
        searchCarpoolsAvailable = SearchCarpoolsAvailableUseCase(carpoolRepository)
    )

    @Provides
    fun providePassengerRequestUseCases(passengerRequestRepository: PassengerRequestRepository) = PassengerRequestUseCases(
        savePassengerRequest = SavePassengerRequestUseCase(passengerRequestRepository),
    )
}