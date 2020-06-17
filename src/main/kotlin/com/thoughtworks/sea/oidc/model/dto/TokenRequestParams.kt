package com.thoughtworks.sea.oidc.model.dto

/** Those are params for generating TokenRequest for restTemplate. */
data class TokenRequestParams(
    /** IDP host address without "https://" prefix. */
    val host: String,
    /** IDP issue token endpoint. */
    val endPoint: String,
    /** the client identifier. */
    val clientId: String,
    /** the client’s shared secret assigned to the IDP. */
    val clientSecret: String,
    /**  the URI to which the response should be sent. */
    val redirectUri: String,
    /** The value of authorization code received earlier. */
    val code: String,
    /** This value must be "authorization_code". */
    val grantType: String = "authorization_code",
    /** Request header content type. */
    val contentType: String = "application/x-www-form-urlencoded"
)
