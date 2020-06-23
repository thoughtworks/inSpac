package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.ParsedTokenResult
import com.thoughtworks.sea.oidc.model.TokenRequest
import com.thoughtworks.sea.oidc.model.TokenRequestParams
import com.thoughtworks.sea.oidc.model.TokenResponse
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

/** This is a token utils for building token request. */
class TokenUtils {
    companion object {
        /**
         * The function is used to build token request
         * @param TokenRequestParams parameter for building token request
         * @return contains url and httpEntity
         */
        @JvmStatic
        fun buildTokenRequest(params: TokenRequestParams): TokenRequest {
            val url = "https://${params.host}${params.endPoint}"

            val bodyMap = LinkedMultiValueMap<String, String>()
            bodyMap.add("code", params.code)
            bodyMap.add("client_id", params.clientId)
            bodyMap.add("client_secret", params.clientSecret)
            bodyMap.add("redirect_uri", params.redirectUri)
            bodyMap.add("grant_type", params.grantType)

            val headerMap = LinkedMultiValueMap<String, String>()
            headerMap.add("content_type", params.contentType)

            return TokenRequest(
                url, HttpEntity(
                    bodyMap, headerMap
                )
            )
        }

        @JvmStatic
        fun parseToken(token: TokenResponse): ParsedTokenResult {
            print(token)
            // TODO Yi: will implement parse method
            return ParsedTokenResult("S3000024B", null)
        }
    }
}
