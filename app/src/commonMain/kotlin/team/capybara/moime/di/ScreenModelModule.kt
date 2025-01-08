package team.capybara.moime.di

import org.koin.dsl.module
import team.capybara.moime.ui.friend.FriendScreenModel
import team.capybara.moime.ui.login.LoginScreenModel
import team.capybara.moime.ui.main.MainScreenModel
import team.capybara.moime.ui.main.home.HomeScreenModel
import team.capybara.moime.ui.main.insight.InsightScreenModel
import team.capybara.moime.ui.splash.SplashScreenModel

val screenModelModule = module {
    single { SplashScreenModel(get()) }
    single { LoginScreenModel(get()) }

    scope<String> {
        scoped { MainScreenModel(get(), get()) }
        scoped { HomeScreenModel(get()) }
        scoped { FriendScreenModel(get()) }
        scoped { InsightScreenModel(get()) }
    }
}
