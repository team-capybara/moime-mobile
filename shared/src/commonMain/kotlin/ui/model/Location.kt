package ui.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable

data class Location(
    val name: String,
    val lat: Float,
    val lng: Float
) : JavaSerializable
