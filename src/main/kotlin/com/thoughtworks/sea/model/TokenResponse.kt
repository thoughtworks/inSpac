package com.thoughtworks.sea.model

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val scope: String
)
