package team.capybara.moime.core.common.di

import com.russhwolf.settings.Settings
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.dsl.module
import team.capybara.moime.core.common.ACCESS_TOKEN_KEY
import team.capybara.moime.core.common.model.BearerTokenStorage

val tokenModule = module {
    single<BearerTokenStorage> {
        val settings: Settings = get()
        val accessToken = settings.getStringOrNull(ACCESS_TOKEN_KEY)
        accessToken?.let { mutableListOf(BearerTokens(it, null)) } ?: mutableListOf()
    }
}
