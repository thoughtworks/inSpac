package com.thoughtworks.sea.oidc

data class TokenRequestEntity(
    val url: String,
    val tokenRequestHeader: TokenRequestHeader,
    val tokenRequestBody: TokenRequestBody
)

data class TokenRequestBody(
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val grantType: String? = "authorization_code"
)

data class TokenRequestHeader(val contentType: String? = "APPLICATION_FORM_URLENCODED")

data class TokenRequestParams(
    val host: String,
    val endPoint: String,
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)
