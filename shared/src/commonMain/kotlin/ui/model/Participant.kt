package ui.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable

data class Participant(
    val id: Long,
    val profileImageUrl: String
) : JavaSerializable
