plugins {
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kmp.compose")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.core.feature"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.common)
            implementation(projects.core.model)
            implementation(projects.core.data)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.coroutines.core)

            implementation(libs.koin.core)

            implementation(libs.moko.permissions.core)
            implementation(libs.moko.permissions.compose)

            implementation(libs.moko.geo)
            implementation(libs.moko.geo.compose)

            implementation(libs.webview.multiplatform)

            implementation(libs.settings)
            implementation(libs.settings.noarg)

            api(libs.kmpnotifier)
            implementation(libs.stately.common)

            implementation(libs.bundles.voyager)

            implementation(libs.haze)
            implementation(libs.calendar)
            implementation(libs.pullrefresh)
            implementation(libs.kim)

            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)

            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)

            implementation(libs.ktor.client.auth)
        }
    }
}
