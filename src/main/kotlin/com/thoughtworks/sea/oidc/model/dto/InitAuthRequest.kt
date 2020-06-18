package com.thoughtworks.sea.oidc.model.dto

import java.util.UUID.randomUUID

/** Request Body for initiating authentication request. */
data class InitAuthRequest(
    /** IDP host address without any protocol prefix. */
    val host: String,
    /** IDP endpoint for redirecting browser. */
    val endPoint: String,
    /** The client identifier assigned to the Relying Party during its registration. */
    val clientId: String,
    /** The URI to which the response should be sent. */
    val redirectURI: String,
    /** Default value is: "openid". */
    val scope: String = "openid",
    /** Default value is: "code". */
    val responseType: String = "code",
    /** A random generated UUID to avoid replay attack. */
    val nonce: String = randomUUID().toString(),
    /** A random generated UUID to maintain state between the request and the callback. */
    val state: String = randomUUID().toString()
)
