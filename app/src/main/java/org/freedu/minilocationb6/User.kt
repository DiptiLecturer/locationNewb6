package org.freedu.minilocationb6

data class User(
    val userId: String = "",
    val email: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
)