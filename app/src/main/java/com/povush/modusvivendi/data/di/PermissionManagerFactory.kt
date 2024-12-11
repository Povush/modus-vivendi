package com.povush.modusvivendi.data.di

import androidx.activity.ComponentActivity
import com.povush.modusvivendi.framework.PermissionManagerImpl
import dagger.assisted.AssistedFactory

@AssistedFactory
interface PermissionManagerFactory {
    fun create(activity: ComponentActivity): PermissionManagerImpl
}