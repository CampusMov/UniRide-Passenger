package com.campusmov.uniride.di

import com.campusmov.uniride.data.datasource.local.datastore.LocalDataStore
import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.datasource.remote.service.ProfileService
import com.campusmov.uniride.data.repository.auth.AuthRepositoryImpl
import com.campusmov.uniride.data.repository.location.LocationRepositoryImpl
import com.campusmov.uniride.data.repository.profile.ProfileRepositoryImpl
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.location.repository.LocationRepository
import com.campusmov.uniride.domain.profile.repository.ProfileRepository
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(authService: AuthService, localDataStore: LocalDataStore): AuthRepository = AuthRepositoryImpl(authService, localDataStore)

    @Provides
    fun provideLocationRepository(locationDataSource: LocationDataSource, placesClient: PlacesClient): LocationRepository = LocationRepositoryImpl(locationDataSource, placesClient)

    @Provides
    fun provideProfileRepository(profileService: ProfileService): ProfileRepository = ProfileRepositoryImpl(profileService)
}