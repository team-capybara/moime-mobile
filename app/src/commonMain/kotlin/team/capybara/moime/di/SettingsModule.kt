package team.capybara.moime.di

import com.russhwolf.settings.Settings
import org.koin.dsl.module

internal val settingsModule = module {
    single { Settings() }
}
