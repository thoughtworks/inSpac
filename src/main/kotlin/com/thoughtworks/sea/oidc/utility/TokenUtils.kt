package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jwt.SignedJWT
import com.sun.org.apache.xml.internal.security.utils.Base64
import com.thoughtworks.sea.oidc.model.TokenRequest
import com.thoughtworks.sea.oidc.model.TokenRequestParams
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
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

        internal fun decryptJWE(idToken: String, privateKeyPem: String): SignedJWT {
            val jweObject = JWEObject.parse(idToken)
            val privateKey = parsePrivateKey(privateKeyPem)
            jweObject.decrypt(RSADecrypter(privateKey))

            return jweObject.payload.toSignedJWT()
        }

        private fun parsePrivateKey(privateKeyPem: String): PrivateKey {
            val keyFactory = KeyFactory.getInstance("RSA")

            val encodedPrivateKey = privateKeyPem
                .replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----\n", "")
            val keySpec = PKCS8EncodedKeySpec(Base64.decode(encodedPrivateKey))

            return keyFactory.generatePrivate(keySpec)
        }
    }
}
