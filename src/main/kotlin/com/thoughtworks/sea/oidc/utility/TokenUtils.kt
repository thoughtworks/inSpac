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
         * @param params parameter for building token request
         * @return contains url and httpEntity
         */
        @JvmStatic
        fun buildTokenRequest(params: TokenRequestParams): TokenRequest {
            val url = "${params.host}${params.endPoint}"

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

        /**
         * The function is used to parse token to SubjectInfo
         * @param token parameter for token response from IDP
         * @param oidcConfig parameter for decrypting, verify signature, verify payload
         * @return ParsedSubjectInfo with nricNumber and uuid
         */
        @JvmStatic
        fun parseTokenToSubjectInfo(
            token: TokenResponse,
            oidcConfig: OIDCConfig
        ): ParsedSubjectInfo = parseToken(token, oidcConfig) {
            ParserUtils.extractSubject(it)
        }

        /**
         * The function is used to parse token to JSONObject
         * @param token parameter for token response from IDP
         * @param oidcConfig parameter for decrypting, verify signature, verify payload
         * @param additionKey parameter for the key of claims to get JsonObject
         * @return JSONObject the value of the given key in payload
         */
        @JvmStatic
        fun parseTokenToJsonObject(
            token: TokenResponse,
            oidcConfig: OIDCConfig,
            additionKey: String
        ): JSONObject = parseToken(token, oidcConfig) {
            ParserUtils.extract(it, additionKey)
        }

        /**
         * The function is used to parse token to T
         * @param token parameter for token response from IDP
         * @param oidcConfig parameter for decrypting, verify signature, verify payload
         * @param extract parameter for extract corresponding value from payload of signedJWT
         * @return T
         */
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
