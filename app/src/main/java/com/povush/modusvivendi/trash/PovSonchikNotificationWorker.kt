package com.povush.modusvivendi.trash

//@HiltWorker
//class PovSonchikNotificationWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted params: WorkerParameters
//) : Worker(context, params) {
//    override fun doWork(): Result {
//        sendNotification(applicationContext)
//        return Result.success()
//    }
//
//    private fun sendNotification(context: Context) {
//        val notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val channelId = "povsonchik_channel"
//        val channelName = "PovSonchik Notifications"
//        val channel = NotificationChannel(
//            channelId,
//            channelName,
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager.createNotificationChannel(channel)
//
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setContentTitle("Полночь наступила!")
//            .setContentText("Давай пов-соньчик?")
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setVibrate(longArrayOf(0, 500, 200, 500))
//            .build()
//
//        notificationManager.notify(1, notification)
//    }
//}