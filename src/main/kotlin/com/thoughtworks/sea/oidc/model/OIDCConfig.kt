package com.thoughtworks.sea.oidc.model

data class OIDCConfig(
    val nonce: String,
    val iss: String
)
