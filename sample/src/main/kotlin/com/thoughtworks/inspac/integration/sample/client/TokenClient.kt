package com.thoughtworks.inspac.integration.sample.client

import com.thoughtworks.inspac.sdk.model.TokenRequestParams
import com.thoughtworks.inspac.sdk.model.TokenResponse
import com.thoughtworks.inspac.sdk.utility.TokenUtils
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Component
class TokenClient(private val restTemplate: RestTemplate) {

    fun getOIDCToken(tokenTokenRequestParams: TokenRequestParams): TokenResponse? {
        // OIDC: Step 3: When Service wants to send Tokens request with authorization code.
        // You need to generate a Token Request to send request.
        val tokenRequest = TokenUtils.buildTokenRequest(tokenTokenRequestParams)
        val response = try {
            restTemplate.exchange(
                    tokenRequest.url,
                    HttpMethod.POST,
                    tokenRequest.httpEntity,
                    TokenResponse::class.java
            )
        } catch (e: RestClientException) {
            throw RestClientException("Request to S/C for getting token fails:", e)
        }

        return response.body
    }
}
