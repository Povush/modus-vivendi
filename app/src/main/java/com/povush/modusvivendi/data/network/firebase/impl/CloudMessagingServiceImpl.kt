package com.povush.modusvivendi.data.network.firebase.impl

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.povush.modusvivendi.data.network.firebase.CloudMessagingService
import javax.inject.Inject

class CloudMessagingServiceImpl @Inject constructor() : CloudMessagingService {
    override fun subscribeToAllTopics() {
        Firebase.messaging.subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Subscribed to topic 'all'")
                } else {
                    Log.e(TAG, "Failed to subscribe to topic 'all'", task.exception)
                }
            }
    }
}