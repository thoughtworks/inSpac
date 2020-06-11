package com.thoughtworks.sea.oidc

import com.thoughtworks.sea.oidc.Constants.TOKEN_REQUEST_DEFAULT_CONTENT_TYPE
import com.thoughtworks.sea.oidc.Constants.TOKEN_REQUEST_DEFAULT_GRANT_TYPE

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
    val grantType: String? = TOKEN_REQUEST_DEFAULT_GRANT_TYPE
)

data class TokenRequestHeader(val contentType: String? = TOKEN_REQUEST_DEFAULT_CONTENT_TYPE)

data class TokenRequestParams(
    val host: String,
    val endPoint: String,
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)
