package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.TokenRequestParams
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

@ExtendWith(MockKExtension::class)
internal class TokenUtilsTest {
    @Test
    internal fun `should return correct artifact from tokenUtils`() {
        // given
        val tokenRequestParams = TokenRequestParams(
            host = "test.com",
            endPoint = "/test/token",
            code = "code",
            clientId = "clientid",
            clientSecret = "clientsecret",
            redirectUri = "https://google.com"
        )

        val bodyMap = LinkedMultiValueMap<String, String>()
        bodyMap.add("code", tokenRequestParams.code)
        bodyMap.add("client_id", tokenRequestParams.clientId)
        bodyMap.add("client_secret", tokenRequestParams.clientSecret)
        bodyMap.add("redirect_uri", tokenRequestParams.redirectUri)
        bodyMap.add("grant_type", tokenRequestParams.grantType)

        val headerMap = LinkedMultiValueMap<String, String>()
        headerMap.add("content_type", tokenRequestParams.contentType)

        val expectedHttpEntity = HttpEntity(bodyMap, headerMap)

        // when
        val tokenRequest =
            TokenUtils.buildTokenRequest(
                tokenRequestParams
            )

        // then
        assertEquals("https://test.com/test/token", tokenRequest.url)
        assertEquals(expectedHttpEntity, tokenRequest.httpEntity)
    }

    @Test
    internal fun `should return jws when decrypt jwe success`() {
        val idToken = MockJOSEData.JWE
        val expectedJWS = MockJOSEData.JWS
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()

        val actualJWS = TokenUtils.decryptJWE(idToken, privateKey)

        assertEquals(expectedJWS, actualJWS.serialize())
    }

    @Test
    internal fun `should return true when verify valid jws`() {
        val idToken = MockJOSEData.JWE
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()
        val publicKey = this::class.java.getResource("/certs/signPublicKey.pub").readText()

        val signedJWT = TokenUtils.decryptJWE(idToken, privateKey)

        assertTrue(TokenUtils.verifyJWS(signedJWT, publicKey))
    }
}
