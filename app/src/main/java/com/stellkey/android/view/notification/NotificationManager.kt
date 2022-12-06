package com.stellkey.android.view.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.stellkey.android.R
import com.stellkey.android.helper.extension.color
import kotlin.random.Random

object HandleNotifications {
    private val CHANNEL_ID get() = Random.nextInt(100000).toString()

    private fun getNotification(
        context: Context,
        channelId: String,
        item: NotificationViewType.Item
    ): NotificationCompat.Builder {
        /*val pendingIntent = when (item.fromType) {

        }*/

        val style = NotificationCompat.BigTextStyle()
            .bigText(item.description)
        val inboxStyle = NotificationCompat.InboxStyle()

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(item.title)
            .setContentText(item.description)
            .setStyle(style)
            .setAutoCancel(true)
            .setColor(context.color(R.color.colorPrimary))
            /*.setContentIntent(pendingIntent)*/
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val summaryNotification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(item.title)
            .setContentText(item.description)
            .setStyle(inboxStyle)
            .setColor(context.color(R.color.colorPrimary))
            .setGroupSummary(true)

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        val id = Random(System.currentTimeMillis()).nextInt(100000)
        val summaryId = 1232764321
        notificationManager.notify(id, notificationBuilder.build())
        notificationManager.notify(summaryId, summaryNotification.build())

        return notificationBuilder
    }

    @TargetApi(25)
    object PreO {
        fun createNotification(
            context: Context,
            item: NotificationViewType.Item
        ): NotificationCompat.Builder {
            return getNotification(context, CHANNEL_ID, item)
        }
    }

    @TargetApi(26)
    object O {
        fun createNotification(
            context: Context,
            item: NotificationViewType.Item
        ): NotificationCompat.Builder {
            val channelId = createChannel(context, item.fromType)
            return getNotification(context, channelId, item)
        }

        private fun createChannel(
            context: Context,
            type: NotificationType
        ): String {
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            val importance = NotificationManager.IMPORTANCE_HIGH

            val channelId = when (type) {
                NotificationType.GENERAL -> GENERAL_CHANNEL_ID
                else -> GENERAL_CHANNEL_ID
            }.toString()

            val channelName = when (type) {
                NotificationType.GENERAL -> GENERAL_CHANNEL_NAME

                else -> GENERAL_CHANNEL_NAME
            }

            NotificationChannel(channelId, channelName, importance)
                .apply {
                    setShowBadge(true)
                    enableLights(type == NotificationType.CREATED_NEW_TASK)
                }.also {
                    notificationManager.createNotificationChannel(it)
                }

            return channelId
        }
    }

    fun Context.showNotification(
        item: NotificationViewType.Item
    ) {
        val isPreOreo = Build.VERSION.SDK_INT < Build.VERSION_CODES.O

        if (isPreOreo) PreO.createNotification(this, item)
        else O.createNotification(this, item)
    }

    /*private fun getIntent(context: Context): PendingIntent {
        val bundle = bundleOf()

        *//*return NavDeepLinkBuilder(context)*//*
    }*/

    private const val GENERAL_CHANNEL_NAME = "General"

    private const val GENERAL_CHANNEL_ID = 10111291

    private const val GROUP_KEY = "com.stellkey.android"
    private const val GROUP_SUMMARY = 0
}