package com.stellkey.android.view.notification

sealed class NotificationViewType {
    data class Header(
        var title: String,
        var titleFr: String
    ): NotificationViewType()

    data class Item(
        var header: String,
        var id: String,
        var title: String,
        var titleFr: String,
        var description: String,
        var receivedAt: String,
        var fromType: NotificationType = NotificationType.GENERAL
    ): NotificationViewType()
}