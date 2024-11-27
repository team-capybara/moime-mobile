package di

import data.Api
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url.takeFrom(URLBuilder().takeFrom(Api.BASE_URL))
                //header("x-dummy-auth-id", "31")
            }
            install(Auth) {
                val bearerTokenStorage: BearerTokenStorage = get()
                bearer {
                    loadTokens {
                        bearerTokenStorage.lastOrNull()
                    }
                    refreshTokens {
                        bearerTokenStorage.lastOrNull()
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}
