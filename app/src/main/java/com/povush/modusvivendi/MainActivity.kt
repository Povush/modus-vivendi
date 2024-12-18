package com.povush.modusvivendi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.povush.modusvivendi.data.di.assisted_factory.PermissionManagerFactory
import com.povush.modusvivendi.data.network.firebase.AccountService
import com.povush.modusvivendi.data.network.firebase.CloudMessagingService
import com.povush.modusvivendi.framework.PermissionManager
import com.povush.modusvivendi.ui.theme.NationalTheme
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var accountService: AccountService
    @Inject lateinit var cloudMessagingService: CloudMessagingService

    @Inject lateinit var permissionManagerFactory: PermissionManagerFactory
    private lateinit var permissionManager: PermissionManager

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        permissionManager = permissionManagerFactory.create(this)
        setContent {
            NationalTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    val hasAccount = accountService.hasProfile()
                    ModusVivendiApp(windowSize, hasAccount)
                }
            }
        }
        permissionManager.askNotificationPermission()
        cloudMessagingService.subscribeToAllTopics()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}