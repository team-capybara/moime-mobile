package team.capybara.moime.data.model

import io.ktor.http.HttpStatusCode

data class ApiException(
    val httpStatusCode: HttpStatusCode
) : RuntimeException("[${httpStatusCode.value}] ${httpStatusCode.description}")
