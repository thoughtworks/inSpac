package com.thoughtworks.inspac.sdk.utility

import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.inspac.sdk.exception.JWSSignatureVerifyException
import com.thoughtworks.inspac.sdk.model.ParseTokenParams
import com.thoughtworks.inspac.sdk.model.ParsedSubjectInfo
import com.thoughtworks.inspac.sdk.model.TokenRequest
import com.thoughtworks.inspac.sdk.model.TokenRequestParams
import com.thoughtworks.inspac.sdk.model.TokenResponse
import net.minidev.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

/** This is a token utils for building token request. */
object TokenUtils {
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
     * @param parseTokenParams parameter for decrypting, verify signature, verify payload
     * @return ParsedSubjectInfo with nricNumber and uuid
     */
    @JvmStatic
    fun parseTokenToSubjectInfo(
        token: TokenResponse,
        parseTokenParams: ParseTokenParams
    ): ParsedSubjectInfo = parseToken(token, parseTokenParams) {
        ParserUtils.extractSubject(it)
    }

    /**
     * The function is used to parse token to JSONObject
     * @param token parameter for token response from IDP
     * @param parseTokenParams parameter for decrypting, verify signature, verify payload
     * @param additionKey parameter for the key of claims to get JsonObject
     * @return JSONObject the value of the given key in payload
     */
    @JvmStatic
    fun parseTokenToJsonObject(
        token: TokenResponse,
        parseTokenParams: ParseTokenParams,
        additionKey: String
    ): JSONObject = parseToken(token, parseTokenParams) {
        ParserUtils.extract(it, additionKey)
    }

    /**
     * The function is used to parse token to T
     * @param token parameter for token response from IDP
     * @param parseTokenParams parameter for decrypting, verify signature, verify payload
     * @param extract parameter for extract corresponding value from payload of signedJWT
     * @return T
     */
    private fun <T> parseToken(
        token: TokenResponse,
        parseTokenParams: ParseTokenParams,
        extract: (signedJWT: SignedJWT) -> T
    ): T {
        val signedJWT = ParserUtils.decryptJWE(token.idToken, parseTokenParams.servicePrivateKey)
        if (ParserUtils.verifyJWS(signedJWT, parseTokenParams.idpPublicKey)) {
            ParserUtils.verifyJWTClaims(signedJWT, parseTokenParams)
            return extract(signedJWT)
        }
        throw JWSSignatureVerifyException()
    }
}
