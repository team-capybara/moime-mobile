package ui.util

expect object ShareUtil {
    fun shareText(text: String)

    fun shareImage(image: ByteArray)
}
