package com.jin.template.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jin.template.util.Debug
import io.karn.notify.Notify
import kotlin.random.Random

@Suppress("UNUSED")
class Notify private constructor(private val context: Context, private val channel: String) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val notificationBuilder = NotificationCompat.Builder(context, channel)

    private var notificationId = 0
    private var priorityHigh = true

    fun setNotificationId(id: Int) = apply { this.notificationId = id }
    fun setPriorityHigh(high: Boolean) = apply {
        priorityHigh = high
        notificationBuilder.priority =
            if (high) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_LOW
    }

    fun setCancellable(cancellable: Boolean) = apply {
        notificationBuilder
            .setAutoCancel(cancellable)
            .setOngoing(!cancellable)
    }

    fun setIcon(@DrawableRes icon: Int) = apply { notificationBuilder.setSmallIcon(icon) }
    fun setColor(@ColorRes color: Int) =
        apply { notificationBuilder.color = ContextCompat.getColor(context, color) }

    fun setTitle(title: String) = apply { notificationBuilder.setContentTitle(title) }
    fun setContent(content: String) = apply { notificationBuilder.setContentText(content) }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setFullScreenIntent(intent: Intent) = apply {
        notificationBuilder.setFullScreenIntent(
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT),
            priorityHigh
        )
    }

    fun show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channel,
                channel,
                if (priorityHigh) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(serviceChannel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun delete(id: Int = notificationId) {
        notificationManager.cancel(id)
    }

    companion object {
        fun with(context: Context, channel: String) = Notify(context, channel)
        fun delete(context: Context, channel: String, notificationId: Int) =
            Notify(context, channel).delete(notificationId)

        fun cancel(notificationId: Int) = try {
            Notify.cancelNotification(notificationId)
        } catch (e: Exception) {
            Debug.e(e.message.toString())
        }
    }

    class Builder(private val context: Context) {
        private var mChannel = ""
        private var mTitle = ""
        private var mContent = ""
        private var mIcon = 0
        private var mColor = 0
        private var mClickIntent: Intent? = null
        private var mSoundUri: Uri? = null

        fun setChannel(channel: String) = apply { mChannel = channel }
        fun setTitle(title: String) = apply { mTitle = title }
        fun setContent(content: String) = apply { mContent = content }
        fun setIcon(@DrawableRes icon: Int) = apply { mIcon = icon }
        fun setColor(@ColorRes color: Int) =
            apply { mColor = ContextCompat.getColor(context, color) }

        fun setClickIntent(intent: Intent) = apply { mClickIntent = intent }
        fun setClickIntent(activity: Class<*>, configIntent: Intent.() -> Unit = {}) =
            apply {
                mClickIntent = Intent(context, activity).apply(configIntent)
            }

        fun setSound(uri: String) = apply { mSoundUri = Uri.parse(uri) }
        fun setSound(uri: Uri) = apply { mSoundUri = uri }
        fun setSound(@RawRes sound: Int) = apply {
            mSoundUri =
                Uri.parse("android.resource://${context.applicationContext.packageName}/$sound")
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun build(notificationId: Int? = null) {
            if (mChannel.isEmpty() || mTitle.isEmpty() || mContent.isEmpty()) return
            Notify.with(context)
                .meta {
                    if (mClickIntent != null) clickIntent = PendingIntent.getActivity(
                        context,
                        Random.nextInt(10000),
                        mClickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                }
                .alerting(mChannel) {
                    channelName = mChannel
                    channelDescription = mChannel
                    vibrationPattern = listOf(100, 200, 100, 200)
                    if (mSoundUri != null) sound = mSoundUri!!
                }
                .header {
                    if (mIcon != 0) icon = mIcon
                    if (mColor != 0) color = mColor
                }
                .content {
                    title = mTitle
                    text = mContent
                }
                .show(notificationId)
        }
    }
}