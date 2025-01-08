package team.capybara.moime.feature.di

import org.koin.dsl.module
import team.capybara.moime.feature.friend.FriendScreenModel
import team.capybara.moime.feature.home.HomeScreenModel
import team.capybara.moime.feature.insight.InsightScreenModel
import team.capybara.moime.feature.login.LoginScreenModel
import team.capybara.moime.feature.main.MainScreenModel
import team.capybara.moime.feature.splash.SplashScreenModel

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
