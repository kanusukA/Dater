@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.dater.alarms.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import com.example.dater.Data.Journey.domain.model.Journey
import com.example.dater.Data.Reminder.domain.model.Reminder
import com.example.dater.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class DaterNotification(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java) as NotificationManager

    private val reminderNotificationChannel = context.getString(R.string.reminder_channel_id)
    private val journeyNotificationChannel = context.getString(R.string.journey_channel_id)
    fun notify(reminder: Reminder){
        val builder = NotificationCompat.Builder(context,reminderNotificationChannel)
            .setSmallIcon(R.drawable.dater_launcher_foreground)
            .setContentTitle(reminder.title)
            .setContentText(reminder.description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(reminder.id,builder.build())
    }

    fun notify(journey: Journey){
        val builder = NotificationCompat.Builder(context,journeyNotificationChannel)
            .setSmallIcon(R.drawable.dater_launcher_foreground)
            .setContentTitle(journey.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(journey.id,builder.build())
    }

    fun reminderNotify(title: String, text: String, id: Int){
        val builder = NotificationCompat.Builder(context,reminderNotificationChannel)
            .setSmallIcon(R.drawable.dater_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(id,builder.build())
    }

    fun journeyNotify(title: String, id: Int){
        val builder = NotificationCompat.Builder(context,journeyNotificationChannel)
            .setSmallIcon(R.drawable.dater_launcher_foreground)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(id,builder.build())
    }



    @Composable
    fun notificationCheck(){
        val notificationPermission = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        val journeyNotificationId = stringResource(id = R.string.journey_channel_id)
        val reminderNotificationId = stringResource(id = R.string.reminder_channel_id)
        LaunchedEffect(key1 = true){
            if (!notificationPermission.status.isGranted) {
                notificationPermission.launchPermissionRequest()
            }



            val journeyNotificationChannel = NotificationChannel(journeyNotificationId,"Journey",NotificationManager.IMPORTANCE_DEFAULT)
            val reminderNotificationChannel = NotificationChannel(reminderNotificationId,"Reminder",NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(journeyNotificationChannel)
            notificationManager.createNotificationChannel(reminderNotificationChannel)
        }
    }

}