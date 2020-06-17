package com.thoughtworks.sea.oidc.model.dto

import java.util.UUID

data class TokenRequestParams(
    val host: String,
    val endPoint: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val code: String = UUID.randomUUID().toString(),
    val grantType: String = "authorization_code",
    val contentType: String = "application/x-www-form-urlencoded"
)
