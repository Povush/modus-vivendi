package com.povush.modusvivendi.data.di

import androidx.activity.ComponentActivity
import com.povush.modusvivendi.framework.PermissionManager
import com.povush.modusvivendi.framework.PermissionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object FrameworkModule {

    @Provides
    fun providePermissionManager(activity: ComponentActivity): PermissionManager {
        return PermissionManagerImpl(activity)
    }
}