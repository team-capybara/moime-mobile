package team.capybara.moime.app

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifier.initialize()
        initKoin()
    }
}
