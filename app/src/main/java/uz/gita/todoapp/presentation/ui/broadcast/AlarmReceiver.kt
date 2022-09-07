package uz.gita.todoapp.presentation.ui.broadcast

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import uz.gita.todoapp.utils.sendNotification

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.let {
            ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            )
        } as NotificationManager

        val desc = intent?.getStringExtra("dataDesc")
        val id = intent?.getStringExtra("dataId")

        notificationManager.sendNotification(desc?:"empty body", context, (id?:0) as Int)
    }
}