package team.capybara.moime

import android.app.Application
import team.capybara.moime.di.initKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifier.initialize()
        initKoin()
    }
}
