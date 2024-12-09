package com.povush.modusvivendi.data.workers

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PovSonchik @Inject constructor(
    @ApplicationContext context: Context
) {
    private val workRequest = PeriodicWorkRequestBuilder<PovSonchikNotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
        .build()
    private val workManager = WorkManager.getInstance(context)

    fun doPovSonchik() {
        val operation = workManager.enqueueUniquePeriodicWork(
            "PovSonchikNotification",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workRequest
        )
        operation.state.observeForever { state ->
            Log.d("POVSON", state.toString())
        }
    }

    private fun calculateInitialDelay(): Long {
        val now = Calendar.getInstance()
        val targetTime  = Calendar.getInstance().apply {
            if (now.get(Calendar.HOUR_OF_DAY) >= 1 && now.get(Calendar.MINUTE) >= 1) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return targetTime.timeInMillis - now.timeInMillis
    }
}