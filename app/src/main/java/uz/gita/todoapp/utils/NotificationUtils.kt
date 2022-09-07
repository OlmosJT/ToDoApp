package uz.gita.todoapp.utils

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import uz.gita.todoapp.MainActivity
import uz.gita.todoapp.R
import uz.gita.todoapp.presentation.ui.broadcast.ActionReceiver

// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification
 */
@SuppressLint("UnspecifiedImmutableFlag", "WrongConstant")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, notificationId: Int) {
    val notificationStyle = NotificationCompat.BigTextStyle()

    val notificationLargeImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.calendar
    )

    /**
     * ActionPendingIntent performs action itself without launching application
     */
    val actionIntent = Intent(applicationContext, ActionReceiver::class.java)
    val actionPendingIntent: PendingIntent = PendingIntent.getBroadcast(
        applicationContext,
        REQUEST_CODE,
        actionIntent,
        FLAGS
    )

    /**
     * Content intent launches activity when user clicks on notification
     */
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        notificationId,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    /**
     * Builder Notification
     */
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.task_notification_channel_id)
    )
        .setSmallIcon(R.drawable.calendar)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
//        ContentPendingIntent launches app when clicks on notifications and autoCancel removes notification
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
//        Add default style for notification. To change a style: go up and find "notificationStyle" variable
        .setStyle(notificationStyle)
        .setLargeIcon(notificationLargeImage)
//        BroadCast actions to perform some actions
        .addAction(
            R.drawable.correct,
            applicationContext.getString(R.string.notification_done),
            actionPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // END TO END NOTIFY Notification
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

fun NotificationManager.cancelNotification(id: Int) {
    cancel(id)
}
