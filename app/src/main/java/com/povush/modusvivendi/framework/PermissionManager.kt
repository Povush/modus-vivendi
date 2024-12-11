package com.povush.modusvivendi.framework

import androidx.activity.result.ActivityResultLauncher

interface PermissionManager {
    val requestPermissionLauncher: ActivityResultLauncher<String>
    fun askNotificationPermission()
}