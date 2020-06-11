package com.thoughtworks.sea.oidc

class TokenRequestGenerator {
    fun generate(tokenTokenRequestParams: TokenRequestParams): TokenRequestEntity = TokenRequestEntity(
        tokenTokenRequestParams.host + tokenTokenRequestParams.endPoint,
        TokenRequestHeader(),
        TokenRequestBody(tokenTokenRequestParams.code, tokenTokenRequestParams.clientId, tokenTokenRequestParams.clientSecret, tokenTokenRequestParams.redirectUri)
    )
}
