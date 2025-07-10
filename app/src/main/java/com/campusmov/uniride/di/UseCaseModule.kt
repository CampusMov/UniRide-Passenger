package com.campusmov.uniride.di

import com.campusmov.uniride.domain.analytics.repository.AnalyticsRepository
import com.campusmov.uniride.domain.analytics.usecases.AnalyticsUseCase
import com.campusmov.uniride.domain.analytics.usecases.StudentRatingUseCase
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.auth.usecases.AuthUseCase
import com.campusmov.uniride.domain.auth.usecases.DeleteAllUsersLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.GetUserByEmailLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.GetUserByIdLocallyUserCase
import com.campusmov.uniride.domain.auth.usecases.GetUserByIdUseCase
import com.campusmov.uniride.domain.auth.usecases.GetUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.SaveUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.UpdateUserLocallyUseCase
import com.campusmov.uniride.domain.auth.usecases.UserUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationCodeUseCase
import com.campusmov.uniride.domain.auth.usecases.VerificationEmailUseCase
import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository
import com.campusmov.uniride.domain.filemanagement.usecases.DeleteFileUseCase
import com.campusmov.uniride.domain.filemanagement.usecases.DownloadFileUseCase
import com.campusmov.uniride.domain.filemanagement.usecases.FileManagementUseCases
import com.campusmov.uniride.domain.filemanagement.usecases.GetFileUrlUseCase
import com.campusmov.uniride.domain.filemanagement.usecases.UploadFileUseCase
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import com.campusmov.uniride.domain.intripcommunication.usecases.CloseChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.ConnectToChatSessionUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.CreateChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.DisconnectChatSessionUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.GetMessagesUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.GetPassengerChatUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.InTripCommunicationUseCases
import com.campusmov.uniride.domain.intripcommunication.usecases.MarkMessageAsReadUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.SendMessageUseCase
import com.campusmov.uniride.domain.intripcommunication.usecases.SubscribeToChatUseCase
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.campusmov.uniride.domain.location.usecases.GetLocationsUpdatesUseCase
import com.campusmov.uniride.domain.location.usecases.GetPlaceDetailsUseCase
import com.campusmov.uniride.domain.location.usecases.GetPlacePredictionsUseCase
import com.campusmov.uniride.domain.location.usecases.LocationUsesCases
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.profile.usecases.DeleteClassScheduleUseCase
import com.campusmov.uniride.domain.profile.usecases.DeleteLocalProfiles
import com.campusmov.uniride.domain.profile.usecases.GetClassSchedulesByProfileIdUseCase
import com.campusmov.uniride.domain.profile.usecases.GetProfileByIdUseCase
import com.campusmov.uniride.domain.profile.usecases.ProfileClassScheduleUseCases
import com.campusmov.uniride.domain.profile.usecases.ProfileUseCases
import com.campusmov.uniride.domain.profile.usecases.SaveProfileUseCase
import com.campusmov.uniride.domain.profile.usecases.UpdateProfileUsecase
import com.campusmov.uniride.domain.reputation.repository.ReputationIncentivesRepository
import com.campusmov.uniride.domain.reputation.usecases.InfractionUseCase
import com.campusmov.uniride.domain.reputation.usecases.ReputationIncentivesUseCase
import com.campusmov.uniride.domain.reputation.usecases.ValorationUseCase
import com.campusmov.uniride.domain.route.repository.RouteCarpoolWebSocketRepository
import com.campusmov.uniride.domain.route.repository.RouteRepository
import com.campusmov.uniride.domain.route.usecases.ConnectRouteCarpoolUseCase
import com.campusmov.uniride.domain.route.usecases.DisconnectRouteCarpoolSessionUseCase
import com.campusmov.uniride.domain.route.usecases.GetRouteUseCase
import com.campusmov.uniride.domain.route.usecases.RouteCarpoolWsUseCases
import com.campusmov.uniride.domain.route.usecases.RouteUseCases
import com.campusmov.uniride.domain.route.usecases.SubscribeRouteCarpoolUpdateCurrentLocationUseCase
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestWebSocketRepository
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.CarpoolWsUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.ConnectCarpoolUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.ConnectRequestsUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.DisconnectCarpoolUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.DisconnectRequestsUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.GetAllPassengerRequestsByPassengerIdUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.GetCarpoolByIdUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.PassengerRequestWsUseCases
import com.campusmov.uniride.domain.routingmatching.usecases.SavePassengerRequestUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.SearchCarpoolsAvailableUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.SubscribeCarpoolStatusUpdatesUseCase
import com.campusmov.uniride.domain.routingmatching.usecases.SubscribeRequestStatusUpdatesUseCase
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
        getUserById = GetUserByIdUseCase(authRepository)
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
        saveProfile = SaveProfileUseCase(profileRepository),
        getProfileById = GetProfileByIdUseCase(profileRepository),
        updateProfile = UpdateProfileUsecase(profileRepository),
        deleteLocalProfiles = DeleteLocalProfiles(profileRepository)
    )

    @Provides
    fun provideProfileClassScheduleUseCases(profileClassScheduleRepository: ProfileClassScheduleRepository) = ProfileClassScheduleUseCases(
        getClassSchedulesByProfileId = GetClassSchedulesByProfileIdUseCase(profileClassScheduleRepository),
        deleteClassSchedule = DeleteClassScheduleUseCase(profileClassScheduleRepository)
    )

    @Provides
    fun provideAnalyticsUseCase(analyticsRepository: AnalyticsRepository) =  AnalyticsUseCase(
        getStudentRatingMetrics = StudentRatingUseCase(analyticsRepository)
    )

    @Provides
    fun provideReputationIncentivesUseCase(reputationIncentivesRepository: ReputationIncentivesRepository) = ReputationIncentivesUseCase(
        getValorationsOfUser = ValorationUseCase(reputationIncentivesRepository),
        getInfractionsOfUser = InfractionUseCase(reputationIncentivesRepository),
        createValoration = ValorationUseCase(reputationIncentivesRepository)
    )

    @Provides
    fun provideCarpoolUseCases(carpoolRepository: CarpoolRepository) = CarpoolUseCases(
        searchCarpoolsAvailable = SearchCarpoolsAvailableUseCase(carpoolRepository),
        getCarpoolById = GetCarpoolByIdUseCase(carpoolRepository)
    )

    @Provides
    fun provideCarpoolWsUseCases(carpoolWebSocketRepository: CarpoolWebSocketRepository) = CarpoolWsUseCases(
        connectCarpoolUseCase = ConnectCarpoolUseCase(carpoolWebSocketRepository),
        subscribeCarpoolStatusUpdatesUseCase = SubscribeCarpoolStatusUpdatesUseCase(carpoolWebSocketRepository),
        disconnectCarpoolUseCase = DisconnectCarpoolUseCase(carpoolWebSocketRepository)
    )

    @Provides
    fun provideFileManagementUseCases(fileManagementRepository: FileManagementRepository) = FileManagementUseCases (
        uploadFileUseCase = UploadFileUseCase(fileManagementRepository),
        downloadFileUseCase = DownloadFileUseCase(fileManagementRepository),
        deleteFileUseCase = DeleteFileUseCase(fileManagementRepository),
        getFileUrlUseCase = GetFileUrlUseCase(fileManagementRepository)
    )

    @Provides
    fun providePassengerRequestUseCases(passengerRequestRepository: PassengerRequestRepository) = PassengerRequestUseCases(
        savePassengerRequest = SavePassengerRequestUseCase(passengerRequestRepository),
        getAllPassengerRequestsByPassengerId = GetAllPassengerRequestsByPassengerIdUseCase(passengerRequestRepository),
    )

    @Provides
    fun providePassengerRequestWsUseCases(passengerRequestWebSocketRepository: PassengerRequestWebSocketRepository) = PassengerRequestWsUseCases(
        connectRequestsUseCase = ConnectRequestsUseCase(passengerRequestWebSocketRepository),
        subscribeRequestStatusUpdatesUseCase = SubscribeRequestStatusUpdatesUseCase(passengerRequestWebSocketRepository),
        disconnectRequestsUseCase = DisconnectRequestsUseCase(passengerRequestWebSocketRepository)
    )

    @Provides
    fun provideInTripCommunicationUseCases(inTripCommunicationRepository: InTripCommunicationRepository, inTripCommunicationWebSocketRepository: InTripCommunicationWebSocketRepository): InTripCommunicationUseCases = InTripCommunicationUseCases(
        createChat = CreateChatUseCase(inTripCommunicationRepository),
        closeChat = CloseChatUseCase(inTripCommunicationRepository),
        getPassengerChat = GetPassengerChatUseCase(inTripCommunicationRepository),
        getMessages = GetMessagesUseCase(inTripCommunicationRepository),
        sendMessage = SendMessageUseCase(inTripCommunicationWebSocketRepository),
        markMessageAsRead = MarkMessageAsReadUseCase(inTripCommunicationRepository),
        connectToChat = ConnectToChatSessionUseCase(inTripCommunicationWebSocketRepository),
        disconnectChat = DisconnectChatSessionUseCase(inTripCommunicationWebSocketRepository),
        observeLiveMessages = SubscribeToChatUseCase(inTripCommunicationWebSocketRepository)
    )

    @Provides
    fun provideRoutesUseCases(routeRepository: RouteRepository) = RouteUseCases(
        getRoute = GetRouteUseCase(routeRepository)
    )

    @Provides
    fun provideRoutesCarpoolUseCases(routeCarpoolWebSocketRepository: RouteCarpoolWebSocketRepository) = RouteCarpoolWsUseCases(
        connectRouteCarpoolUseCase = ConnectRouteCarpoolUseCase(routeCarpoolWebSocketRepository),
        subscribeRouteCarpoolUpdatesUseCase = SubscribeRouteCarpoolUpdateCurrentLocationUseCase(routeCarpoolWebSocketRepository),
        disconnectRouteCarpoolUseCase = DisconnectRouteCarpoolSessionUseCase(routeCarpoolWebSocketRepository)
    )
}