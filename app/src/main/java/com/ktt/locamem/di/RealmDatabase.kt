package com.ktt.locamem.di

import com.ktt.locamem.data.LocationRepository
import com.ktt.locamem.data.LocationRepositoryImpl
import com.ktt.locamem.data.UserRepository
import com.ktt.locamem.data.UserRepositoryImpl
import com.ktt.locamem.model.LocationData
import com.ktt.locamem.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RealmDatabase{

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                User::class,
                LocationData::class
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideUserRepository(realm: Realm): UserRepository {
        return UserRepositoryImpl(realm = realm)
    }

    @Singleton
    @Provides
    fun provideLocationRepository(realm: Realm): LocationRepository {
        return LocationRepositoryImpl(realm = realm)
    }
}