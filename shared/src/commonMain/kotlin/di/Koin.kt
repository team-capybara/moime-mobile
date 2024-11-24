package di

import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(networkModule)
    modules(tokenModule)
    modules(settingsModule)
    modules(screenModelModule)
    modules(repositoryModule)
}
