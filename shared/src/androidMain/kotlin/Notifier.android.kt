import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import team.capybara.moime.R

actual fun initializeNotifierPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.mipmap.ic_launcher,
            showPushNotification = true,
        )
    )
}
