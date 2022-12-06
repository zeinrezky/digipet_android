package com.stellkey.android.view.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import timber.log.Timber

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val payloadData = JSONObject(message.data["data"].orEmpty())

        /*val notificationItem = NotificationViewType.Item(
            id = emptyString,
            title = message.data["title"].toString(),
            description = message.data["body"].toString(),
            header = emptyString,
            receivedAt = emptyString
        )

        applicationContext.showNotification(notificationItem)*/

        if (message.data.isNotEmpty()) {
            Timber.e("Notification Data : ${message.data}")
            Timber.e("Notification ID : ${message.messageId}")
            Timber.e("Notification Type : ${payloadData.getString("from_type")}")
        }
    }
}