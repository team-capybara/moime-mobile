package team.capybara.moime.core.common

import platform.Foundation.NSBundle

actual fun getPlatform() = Platform.iOS

actual fun getVersionString() = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String ?: "Unknown"
