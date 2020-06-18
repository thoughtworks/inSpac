package com.thoughtworks.sea.oidc.model

import java.util.UUID.randomUUID

data class InitAuthRequest(
    val host: String,
    val endPoint: String,
    val clientId: String,
    val redirectURI: String,
    val scope: String = "openid",
    val responseType: String = "code",
    val nonce: String = randomUUID().toString(),
    val state: String = randomUUID().toString()
)
