package com.povush.modusvivendi.data.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.povush.modusvivendi.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PovSonchikNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters
) : Worker(context, params) {
    override fun doWork(): Result {
        sendNotification(applicationContext)
        return Result.success()
    }

    private fun sendNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "povsonchik_channel"
        val channelName = "PovSonchik Notifications"
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Полночь наступила!")
            .setContentText("Давай пов-соньчик?")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()

        notificationManager.notify(1, notification)
    }
}