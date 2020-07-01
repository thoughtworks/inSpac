package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.JWSSignatureVerifyException
import com.thoughtworks.sea.oidc.model.OIDCConfig
import com.thoughtworks.sea.oidc.model.ParsedSubjectInfo
import com.thoughtworks.sea.oidc.model.TokenRequest
import com.thoughtworks.sea.oidc.model.TokenRequestParams
import com.thoughtworks.sea.oidc.model.TokenResponse
import net.minidev.json.JSONObject
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
        fun parseTokenToSubjectInfo(
            token: TokenResponse,
            oidcConfig: OIDCConfig
        ): ParsedSubjectInfo = parseToken(token, oidcConfig) {
            ParserUtils.extractSubject(it)
        }

        @JvmStatic
        fun parseTokenToJsonObject(
            token: TokenResponse,
            oidcConfig: OIDCConfig,
            additionKey: String
        ): JSONObject = parseToken(token, oidcConfig) {
            ParserUtils.extract(it, additionKey)
        }

        private fun <T> parseToken(
            token: TokenResponse,
            oidcConfig: OIDCConfig,
            extract: (signedJWT: SignedJWT) -> T
        ): T {
            val signedJWT = ParserUtils.decryptJWE(token.idToken, oidcConfig.servicePrivateKey)
            if (ParserUtils.verifyJWS(signedJWT, oidcConfig.idpPublicKey)) {
                ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
                return extract(signedJWT)
            }
            throw JWSSignatureVerifyException()
        }
    }
}
