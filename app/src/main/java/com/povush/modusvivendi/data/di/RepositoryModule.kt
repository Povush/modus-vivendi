package com.povush.modusvivendi.data.di

import com.povush.modusvivendi.data.repository.QuestlinesRepositoryImpl
import com.povush.modusvivendi.domain.QuestlinesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideQuestlinesRepository(impl: QuestlinesRepositoryImpl): QuestlinesRepository
}