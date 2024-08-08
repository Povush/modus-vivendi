package com.povush.modusvivendi.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.povush.modusvivendi.ui.navigation.ModusVivendiApp
import com.povush.modusvivendi.ui.theme.NationalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NationalTheme {
                ModusVivendiApp()
            }
        }
    }
}
