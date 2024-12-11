package com.povush.modusvivendi.trash

//class PovSonchik @Inject constructor(
//    @ApplicationContext context: Context
//) {
//    private val workRequest = PeriodicWorkRequestBuilder<PovSonchikNotificationWorker>(1, TimeUnit.DAYS)
//        .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
//        .build()
//    private val workManager = WorkManager.getInstance(context)
//
//    fun doPovSonchik() {
//        val operation = workManager.enqueueUniquePeriodicWork(
//            "PovSonchikNotification",
//            ExistingPeriodicWorkPolicy.KEEP,
//            workRequest
//        )
//        operation.state.observeForever { state ->
//            Log.d("POVSON", state.toString())
//        }
//    }
//
//    private fun calculateInitialDelay(): Long {
//        val now = Calendar.getInstance()
//        val targetTime  = Calendar.getInstance().apply {
//            if (now.get(Calendar.HOUR_OF_DAY) >= 1 && now.get(Calendar.MINUTE) >= 1) {
//                add(Calendar.DAY_OF_YEAR, 1)
//            }
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
//            set(Calendar.SECOND, 0)
//            set(Calendar.MILLISECOND, 0)
//        }
//        return targetTime.timeInMillis - now.timeInMillis
//    }
//}