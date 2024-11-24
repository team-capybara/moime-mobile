package di

import com.russhwolf.settings.Settings
import io.ktor.client.plugins.auth.providers.BearerTokens
import org.koin.dsl.module
import ui.jsbridge.ACCESS_TOKEN_KEY

typealias BearerTokenStorage = MutableList<BearerTokens>

internal val tokenModule = module {
    single<BearerTokenStorage> {
        val settings: Settings = get()
        val accessToken = settings.getStringOrNull(ACCESS_TOKEN_KEY)
        accessToken?.let { mutableListOf(BearerTokens(it, null)) } ?: mutableListOf()
    }
}
