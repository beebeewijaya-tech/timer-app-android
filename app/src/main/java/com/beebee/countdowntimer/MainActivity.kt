package com.beebee.countdowntimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		createChannel(
			getString(R.string.notification_channel_id),
			getString(R.string.notification_channel_name)
		)
	}

	private fun createChannel(channelId: String, channelName: String) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel(
				channelId,
				channelName,
				NotificationManager.IMPORTANCE_HIGH
			)

			notificationChannel.enableLights(true)
			notificationChannel.lightColor = Color.RED
			notificationChannel.description = "Timer apps for set timer based on hour"
			notificationChannel.enableVibration(true)
			notificationChannel.apply { setShowBadge(false) }

			val notificationManager = getSystemService(NotificationManager::class.java)
			notificationManager.createNotificationChannel(notificationChannel)
		}
	}
}