package com.campusmov.uniride.di

import com.campusmov.uniride.data.datasource.remote.service.InTripSocketService
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
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.intripcommunication.usecases.CloseChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.ConnectToChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.CreateChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.DisconnectChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.GetMessagesUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.GetPassengerChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.InTripCommunicationUseCases
import com.campusmov.uniride.domain.intripcommunication.usecases.MarkMessageAsReadUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.ObserveLiveMessagesUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.SendMessageUseCase
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
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
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
    fun provideInTripCommunicationUseCases(
        inTripCommunicationRepository: InTripCommunicationRepository,
        socketService: InTripSocketService
    ): InTripCommunicationUseCases = InTripCommunicationUseCases(
        createChat = CreateChatUseCase(inTripCommunicationRepository),
        closeChat = CloseChatUseCase(inTripCommunicationRepository),
        getPassengerChat = GetPassengerChatUseCase(inTripCommunicationRepository),
        getMessages = GetMessagesUseCase(inTripCommunicationRepository),
        sendMessage = SendMessageUseCase(inTripCommunicationRepository),
        markMessageAsRead = MarkMessageAsReadUseCase(inTripCommunicationRepository),
        connectToChat        = ConnectToChatUseCase(socketService),
        disconnectChat       = DisconnectChatUseCase(socketService),
        observeLiveMessages  = ObserveLiveMessagesUseCase(socketService),

    )
}