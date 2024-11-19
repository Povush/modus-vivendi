package com.povush.modusvivendi.data.di

import com.povush.modusvivendi.data.firebase.AccountService
import com.povush.modusvivendi.data.firebase.StorageService
import com.povush.modusvivendi.data.firebase.impl.AccountServiceImpl
import com.povush.modusvivendi.data.firebase.impl.StorageServiceImpl
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
}