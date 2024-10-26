package com.povush.modusvivendi.data.firebase.module

import com.povush.modusvivendi.data.firebase.AccountService
import com.povush.modusvivendi.data.firebase.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService
}