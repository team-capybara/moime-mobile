package team.capybara.moime.core.model

data class Location(
    val name: String,
    val lat: Float,
    val lng: Float
) : JavaSerializable
