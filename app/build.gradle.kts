import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("moime.convention.base")
    id("moime.convention.kmp")
    id("moime.convention.kmp.compose")
    id("moime.convention.kmp.ios")
    id("moime.convention.android.application")
    id("moime.convention.google.services")
}

android.namespace = "team.capybara.moime.app"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    targets.filterIsInstance<KotlinNativeTarget>().forEach {
        it.binaries.framework {
            baseName = "app"
            isStatic = true
            export(libs.kmpnotifier)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.ui)
            implementation(projects.core.common)
            implementation(projects.core.data)
            implementation(projects.core.network)
            implementation(projects.feature)

            implementation(libs.koin.core)

            implementation(libs.coil.compose)
            implementation(libs.coil.ktor)

            api(libs.kmpnotifier)
            implementation(libs.stately.common)

            implementation(libs.bundles.voyager)

            implementation(libs.haze)
            implementation(libs.filekit.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.lifecycle.compose)
            implementation(libs.koin.androidx.compose)
        }
    }
}
