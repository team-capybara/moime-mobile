package team.capybara.moime

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

actual fun initializeNotifierPlatformSpecific() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Ios(
            showPushNotification = true,
            askNotificationPermissionOnStart = true
        )
    )
}
