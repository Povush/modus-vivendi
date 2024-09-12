package com.povush.modusvivendi

import android.app.Application
import com.povush.modusvivendi.data.AppDataContainer

class ModusVivendiApplication : Application() {

    lateinit var container: AppDataContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
