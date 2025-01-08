package team.capybara.moime

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun initializeNotifierPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.mipmap.ic_launcher,
            showPushNotification = true,
        )
    )
}
