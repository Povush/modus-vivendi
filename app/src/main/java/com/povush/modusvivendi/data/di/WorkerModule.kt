package com.povush.modusvivendi.data.di

import android.content.Context
import com.povush.modusvivendi.data.workers.PovSonchik
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    @Singleton
    fun providePovSonchik(@ApplicationContext context: Context): PovSonchik {
        return PovSonchik(context)
    }
}