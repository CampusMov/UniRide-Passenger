package com.campusmov.uniride.di

import com.campusmov.uniride.data.datasource.local.datastore.LocalDataStore
import com.campusmov.uniride.data.datasource.location.LocationDataSource
import com.campusmov.uniride.data.datasource.remote.service.AuthService
import com.campusmov.uniride.data.repository.auth.AuthRepositoryImpl
import com.campusmov.uniride.data.repository.routingmatching.LocationRepositoryImpl
import com.campusmov.uniride.domain.auth.repository.AuthRepository
import com.campusmov.uniride.domain.routingmatching.repository.LocationRepository
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
    fun provideLocationRepository(locationDataSource: LocationDataSource): LocationRepository = LocationRepositoryImpl(locationDataSource)
}