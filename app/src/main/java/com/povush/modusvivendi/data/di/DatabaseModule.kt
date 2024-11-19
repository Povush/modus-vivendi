package com.povush.modusvivendi.data.di

import android.content.Context
import com.povush.modusvivendi.data.db.ModusVivendiDatabase
import com.povush.modusvivendi.data.db.dao.QuestDao
import com.povush.modusvivendi.data.db.dao.TaskDao
import com.povush.modusvivendi.data.db.offline_repository.OfflineQuestlinesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ModusVivendiDatabase {
        return ModusVivendiDatabase.getDatabase(context)
    }

    @Provides
    fun provideQuestDao(database: ModusVivendiDatabase): QuestDao {
        return database.questDao()
    }

    @Provides
    fun provideTaskDao(database: ModusVivendiDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideOfflineQuestlinesRepository(
        questDao: QuestDao,
        taskDao: TaskDao
    ): OfflineQuestlinesRepository {
        return OfflineQuestlinesRepository(questDao, taskDao)
    }
}