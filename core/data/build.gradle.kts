plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.android")
    id("moime.convention.kmp.ios")
    id("moime.convention.kotlin.serialization")
}

android.namespace = "team.capybara.moime.core.data"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.common)

                implementation(libs.kotlinx.datetime)
            }
        }
    }
}
