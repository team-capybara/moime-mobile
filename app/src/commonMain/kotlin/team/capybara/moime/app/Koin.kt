package team.capybara.moime.app

import org.koin.core.context.startKoin
import team.capybara.moime.core.common.di.tokenModule
import team.capybara.moime.data.network.di.networkModule
import team.capybara.moime.data.network.di.repositoryModule
import team.capybara.moime.data.network.di.settingsModule
import team.capybara.moime.feature.di.screenModelModule

fun initKoin() = startKoin {
    modules(networkModule)
    modules(tokenModule)
    modules(settingsModule)
    modules(screenModelModule)
    modules(repositoryModule)
}
