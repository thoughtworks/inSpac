package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.dto.TokenRequest
import com.thoughtworks.sea.oidc.model.dto.TokenRequestParams
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

class TokenUtils {
    companion object {
        @JvmStatic
        fun buildHttpEntityForRestTemplate(params: TokenRequestParams): TokenRequest {
            val url = "https://${params.host}${params.endPoint}"

            val bodyMap = LinkedMultiValueMap<String, String>()
            bodyMap.add("code", params.code)
            bodyMap.add("client_id", params.clientId)
            bodyMap.add("client_secret", params.clientSecret)
            bodyMap.add("redirect_uri", params.redirectUri)
            bodyMap.add("grant_type", params.grantType)

            val headerMap = LinkedMultiValueMap<String, String>()
            headerMap.add("content_type", params.contentType)

            return TokenRequest(url, HttpEntity(
                bodyMap, headerMap
            ))
        }
    }
}
