package di

import org.koin.dsl.module
import ui.friend.FriendScreenModel
import ui.login.LoginScreenModel
import ui.main.MainScreenModel
import ui.main.home.HomeScreenModel
import ui.main.insight.InsightScreenModel
import ui.splash.SplashScreenModel

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
