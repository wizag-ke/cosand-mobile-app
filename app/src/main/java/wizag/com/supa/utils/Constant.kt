/*
package wizag.com.supa.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import wizag.com.supa.R

object Constant {
    lateinit var notificationManager: NotificationManagerCompat
    @JvmStatic
    fun sendNotification(context: Context, message: String, header: String, pendingClass: Class<*>, defaultNotification: Boolean) {
        notificationManager = NotificationManagerCompat.from(context)
        val notificationId = 1
        val chanelid = "chanelid_1"
        val intent = Intent(context, pendingClass)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // you must create a notification channel for API 26 and Above
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(chanelid, header, importance)
            channel.description = message
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        }

        val mBuilder = NotificationCompat.Builder(context, chanelid)
                .setSmallIcon(R.drawable.ic_traffic)
                .setContentTitle(header)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(when (defaultNotification) {
                    true -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    else -> Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notif)
                })


        val notification = mBuilder.build()
        notification.flags = Notification.FLAG_ONGOING_EVENT or Notification.FLAG_NO_CLEAR
        notificationManager.notify(notificationId, notification)
    }

    @JvmStatic
    fun clearNotification(notificationId: Int) {
        if (::notificationManager.isInitialized)
            if (notificationId == 0)
                notificationManager.cancelAll()
            else
                notificationManager.cancel(notificationId)
    }

}*/
