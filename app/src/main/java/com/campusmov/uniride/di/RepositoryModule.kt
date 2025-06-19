package com.campusmov.uniride.di

import com.campusmov.uniride.data.datasource.local.dao.UserDao
import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.datasource.remote.service.CarpoolService
import com.campusmov.uniride.data.datasource.remote.service.InTripCommunicationService
import com.campusmov.uniride.data.datasource.remote.service.ProfileClassScheduleService
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.data.repository.auth.AuthRepositoryImpl
import com.campusmov.uniride.data.repository.auth.UserRepositoryImpl
import com.campusmov.uniride.data.repository.intripcommunication.InTripCommunicationRepositoryImpl
import com.campusmov.uniride.data.repository.location.LocationRepositoryImpl
import com.campusmov.uniride.data.repository.profile.ProfileClassScheduleRepositoryImpl
import com.campusmov.uniride.data.repository.profile.ProfileRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.CarpoolRepositoryImpl
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.auth.repository.UserRepository
import com.campusmov.uniride.domain.intripcommunication.repository.InTripCommunicationRepository
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.campusmov.uniride.domain.profile.repository.ProfileClassScheduleRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.campusmov.uniride.domain.routingmatching.repository.CarpoolRepository
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
    fun provideProfileRepository(profileService: ProfileService): ProfileRepository = ProfileRepositoryImpl(profileService)

    @Provides
    fun provideProfileClassScheduleRepository(profileClassScheduleService: ProfileClassScheduleService): ProfileClassScheduleRepository = ProfileClassScheduleRepositoryImpl(profileClassScheduleService)

    @Provides
    fun provideCarpoolRepository(carpoolService: CarpoolService): CarpoolRepository = CarpoolRepositoryImpl(carpoolService)


    @Provides
    fun provideInTripCommunicationRepository(inTripCommunicationService: InTripCommunicationService): InTripCommunicationRepository = InTripCommunicationRepositoryImpl(inTripCommunicationService)
}