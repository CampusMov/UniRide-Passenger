package com.campusmov.uniride.di

import com.campusmov.uniride.data.datasource.local.dao.ProfileDao
import com.campusmov.uniride.data.datasource.local.dao.UserDao
import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.data.datasource.remote.service.AnalyticsService
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.datasource.remote.service.CarpoolService
import com.campusmov.uniride.data.datasource.remote.service.InTripCommunicationService
import com.campusmov.uniride.data.datasource.remote.service.PassengerRequestService
import com.campusmov.uniride.data.datasource.remote.service.ProfileClassScheduleService
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.data.datasource.remote.service.ReputationIncentivesService
import com.campusmov.uniride.data.datasource.remote.service.RouteService
import com.campusmov.uniride.data.repository.analytics.AnalyticsRepositoryImpl
import com.campusmov.uniride.data.repository.auth.AuthRepositoryImpl
import com.campusmov.uniride.data.repository.auth.UserRepositoryImpl
import com.campusmov.uniride.data.repository.intripcommunication.InTripCommunicationRepositoryImpl
import com.campusmov.uniride.data.repository.location.LocationRepositoryImpl
import com.campusmov.uniride.data.repository.profile.ProfileClassScheduleRepositoryImpl
import com.campusmov.uniride.data.repository.profile.ProfileRepositoryImpl
import com.campusmov.uniride.data.repository.reputation.ReputationIncentivesRepositoryImpl
import com.campusmov.uniride.domain.analytics.repository.AnalyticsRepository
import com.campusmov.uniride.domain.reputation.repository.ReputationIncentivesRepository
import com.campusmov.uniride.data.repository.filemanagement.FileManagementRepositoryImpl
import com.campusmov.uniride.data.repository.intripcommunication.InTripCommunicationWebSocketRepositoryImpl
import com.campusmov.uniride.data.repository.route.RouteRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.CarpoolRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.CarpoolWebSocketRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.PassengerRequestRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.PassengerRequestWebSocketRepositoryImpl
import com.campusmov.uniride.data.repository.route.RouteCarpoolWebSocketRepositoryRepositoryImpl
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.filemanagement.repository.FileManagementRepository
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationWebSocketRepository
import com.campusmov.uniride.domain.route.repository.RouteRepository
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolWebSocketRepository
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestRepository
import com.campusmov.uniride.domain.routingmatching.repository.PassengerRequestWebSocketRepository
import com.campusmov.uniride.domain.route.repository.RouteCarpoolWebSocketRepository
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hildan.krossbow.stomp.StompClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepositoryImpl(userDao)

    @Provides
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)

    @Provides
    fun provideLocationRepository(locationDataSource: LocationDataSource, placesClient: PlacesClient): LocationRepository = LocationRepositoryImpl(locationDataSource, placesClient)

    @Provides
    fun provideProfileRepository(profileService: ProfileService, profileDao: ProfileDao): ProfileRepository = ProfileRepositoryImpl(profileService, profileDao)

    @Provides
    fun provideProfileClassScheduleRepository(profileClassScheduleService: ProfileClassScheduleService): ProfileClassScheduleRepository = ProfileClassScheduleRepositoryImpl(profileClassScheduleService)

    @Provides
    fun provideAnalyticsRepository(analyticsService: AnalyticsService): AnalyticsRepository = AnalyticsRepositoryImpl(analyticsService)

    @Provides
    fun provideReputationIncentivesRepository(reputationIncentivesService: ReputationIncentivesService): ReputationIncentivesRepository = ReputationIncentivesRepositoryImpl(reputationIncentivesService)

    @Provides   
    fun provideCarpoolRepository(carpoolService: CarpoolService): CarpoolRepository = CarpoolRepositoryImpl(carpoolService)

    @Provides
    fun provideCarpoolWebSocketRepository(stompClient: StompClient, gson: Gson): CarpoolWebSocketRepository = CarpoolWebSocketRepositoryImpl(stompClient, gson)

    @Provides
    fun provideFileManagementRepository(storage: FirebaseStorage): FileManagementRepository = FileManagementRepositoryImpl(storage)

    @Provides
    fun providePassengerRequestRepository(passengerRequestService: PassengerRequestService): PassengerRequestRepository = PassengerRequestRepositoryImpl(passengerRequestService)

    @Provides
    @Singleton
    fun providePassengerRequestWebSocketRepository(stompClient: StompClient, gson: Gson): PassengerRequestWebSocketRepository = PassengerRequestWebSocketRepositoryImpl(stompClient, gson)

    @Provides
    @Singleton
    fun provideRouteCarpoolWebSocketRepository(stompClient: StompClient, gson: Gson): RouteCarpoolWebSocketRepository = RouteCarpoolWebSocketRepositoryRepositoryImpl(stompClient, gson)

    @Provides
    @Singleton
    fun provideInTripCommunicationRepository(inTripCommunicationService: InTripCommunicationService): InTripCommunicationRepository = InTripCommunicationRepositoryImpl(inTripCommunicationService)

    @Provides
    @Singleton
    fun provideInTripCommunicationWebSocketRepository(stompClient: StompClient, gson: Gson): InTripCommunicationWebSocketRepository = InTripCommunicationWebSocketRepositoryImpl(stompClient, gson)

    @Provides
    @Singleton
    fun provideRouteRepository(routeService: RouteService): RouteRepository = RouteRepositoryImpl(routeService)
}