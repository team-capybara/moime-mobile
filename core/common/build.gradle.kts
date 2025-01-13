plugins {
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
}

android.namespace = "team.capybara.moime.core.common"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.ktor.client.auth)
            implementation(libs.koin.core)
            implementation(libs.settings)
        }
    }
}
