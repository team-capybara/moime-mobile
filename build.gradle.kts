plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.googleServices).apply(false)
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
