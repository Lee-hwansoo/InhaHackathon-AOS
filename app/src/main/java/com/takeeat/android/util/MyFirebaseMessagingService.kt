package com.takeeat.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.FirebaseApp
import com.takeeat.android.MainActivity
import com.takeeat.android.R
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    // 푸시 알림을 표시할 채널 ID
    private val CHANNELID = ""
    private val CHANNELNAME = ""

    // 토큰 가져오기
    fun getFirebaseToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d(TAG, "token=${it}")
            UserSharedPreferences.setFCMToken(it)
        }
    }

    //토큰 갱싱
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "new Token: $token")
        UserSharedPreferences.setFCMToken(token)
        // 토큰을 이용한 추가 작업 수행
        // 예: 서버에 토큰 전송, 로컬에 토큰 저장 등
        // token 변수에 발급된 토큰이 들어있습니다.
    }

    // FCM 수신
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {notification ->
            Log.d(TAG, "Message Notification Body: ${notification.body}")

            val title = notification.title
            val message = notification.body
            title?.let { nonNullTitle ->
                message?.let { nonNullMessage ->
                    showNotification(applicationContext, nonNullTitle, nonNullMessage)
                }
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    /** 알림 생성 메서드 */
    private fun showNotification(context: Context, title: String, message: String) {
        val notificationId = System.currentTimeMillis().toInt()

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, CHANNELID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSound(soundUri)

        // 알림을 클릭하면 앱으로 이동하도록 인텐트를 설정합니다.
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        notificationBuilder.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo 이상에서는 알림 채널을 생성하여 설정해야 합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNELID,
                CHANNELNAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 알림을 표시합니다.
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}