package mgmix.dev.collection_of_android_project.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mgmix.dev.collection_of_android_project.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        createNotificationChannel()

        val type = msg.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = msg.data["title"]
        val message = msg.data["message"]

        type ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)


        }

    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, NotificationActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                builder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "\uD83D\uDC76 \uD83D\uDC67 \uD83E\uDDD2 \uD83D\uDC66 \uD83D\uDC69 \uD83E\uDDD1 \uD83D\uDC68 \uD83D\uDC69\u200D\uD83E\uDDB1 \uD83E\uDDD1\u200D\uD83E\uDDB1 \uD83D\uDC68\u200D\uD83E\uDDB1 \uD83D\uDC69\u200D\uD83E\uDDB0 \uD83E\uDDD1\u200D\uD83E\uDDB0 \uD83D\uDC68\u200D\uD83E\uDDB0 \uD83D\uDC71\u200D♀️ \uD83D\uDC71 \uD83D\uDC71\u200D♂️ \uD83D\uDC69\u200D\uD83E\uDDB3 \uD83E\uDDD1\u200D\uD83E\uDDB3 \uD83D\uDC68\u200D\uD83E\uDDB3 \uD83D\uDC69\u200D\uD83E\uDDB2 \uD83E\uDDD1\u200D\uD83E\uDDB2 \uD83D\uDC68\u200D\uD83E\uDDB2 \uD83E\uDDD4 \uD83D\uDC75 \uD83E\uDDD3 \uD83D\uDC74 \uD83D\uDC72 \uD83D\uDC73\u200D♀️ \uD83D\uDC73 \uD83D\uDC73\u200D♂️ \uD83E\uDDD5 \uD83D\uDC6E\u200D♀️ \uD83D\uDC6E \uD83D\uDC6E\u200D♂️ \uD83D\uDC77\u200D♀️ \uD83D\uDC77 \uD83D\uDC77\u200D♂️ \uD83D\uDC82\u200D♀️ \uD83D\uDC82 \uD83D\uDC82\u200D♂️ \uD83D\uDD75️\u200D♀️ \uD83D\uDD75️ \uD83D\uDD75️\u200D♂️ \uD83D\uDC69\u200D⚕️ \uD83E\uDDD1\u200D⚕️ \uD83D\uDC68\u200D⚕️ \uD83D\uDC69\u200D\uD83C\uDF3E \uD83E\uDDD1\u200D\uD83C\uDF3E \uD83D\uDC68\u200D\uD83C\uDF3E \uD83D\uDC69\u200D\uD83C\uDF73 \uD83E\uDDD1\u200D\uD83C\uDF73 \uD83D\uDC68\u200D\uD83C\uDF73 \uD83D\uDC69\u200D\uD83C\uDF93 \uD83E\uDDD1\u200D\uD83C\uDF93 \uD83D\uDC68\u200D\uD83C\uDF93 \uD83D\uDC69\u200D\uD83C\uDFA4 \uD83E\uDDD1\u200D\uD83C\uDFA4 \uD83D\uDC68\u200D\uD83C\uDFA4 \uD83D\uDC69\u200D\uD83C\uDFEB \uD83E\uDDD1\u200D\uD83C\uDFEB \uD83D\uDC68\u200D\uD83C\uDFEB \uD83D\uDC69\u200D\uD83C\uDFED \uD83E\uDDD1\u200D\uD83C\uDFED \uD83D\uDC68\u200D\uD83C\uDFED \uD83D\uDC69\u200D\uD83D\uDCBB \uD83E\uDDD1\u200D\uD83D\uDCBB \uD83D\uDC68\u200D\uD83D\uDCBB \uD83D\uDC69\u200D\uD83D\uDCBC \uD83E\uDDD1\u200D\uD83D\uDCBC \uD83D\uDC68\u200D\uD83D\uDCBC \uD83D\uDC69\u200D\uD83D\uDD27 \uD83E\uDDD1\u200D\uD83D\uDD27 "
                        )

                )
            }
            NotificationType.CUSTOM -> {
                builder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_notification_custom
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }
        return builder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party 위한 채널"
        private const val CHANNEL_ID = "channel_id"
    }

}