package com.povush.modusvivendi.data.di

import com.povush.modusvivendi.data.network.firebase.AccountService
import com.povush.modusvivendi.data.network.firebase.CloudMessagingService
import com.povush.modusvivendi.data.network.firebase.StorageService
import com.povush.modusvivendi.data.network.firebase.impl.AccountServiceImpl
import com.povush.modusvivendi.data.network.firebase.impl.CloudMessagingServiceImpl
import com.povush.modusvivendi.data.network.firebase.impl.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideCloudMessagingService(impl: CloudMessagingServiceImpl): CloudMessagingService
}