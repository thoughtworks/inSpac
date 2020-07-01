package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.JWSSignatureVerifyException
import com.thoughtworks.sea.oidc.model.OIDCConfig
import com.thoughtworks.sea.oidc.model.ParsedSubjectInfo
import com.thoughtworks.sea.oidc.model.TokenRequestParams
import com.thoughtworks.sea.oidc.model.TokenResponse
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import net.minidev.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    internal fun `should return parsed subject info when parse with correct Token`() {
        // given
        val idToken = "idToken"
        val mockTokenResponse = TokenResponse(
            "accessToken",
            "refreshToken",
            idToken,
            "bearer",
            200,
            "openid"
        )
        val idpPublicKey = "idpPublicKey"
        val servicePrivateKey = "servicePrivateKey"
        val oidcConfig = OIDCConfig("any", "any", "any", idpPublicKey, servicePrivateKey)

        mockkObject(ParserUtils)
        val signedJWT = mockk<SignedJWT>()
        val expectedSubjectInfo = mockk<ParsedSubjectInfo>()
        every { ParserUtils.decryptJWE(idToken, servicePrivateKey) } returns signedJWT
        every { ParserUtils.verifyJWS(signedJWT, idpPublicKey) } returns true
        every { ParserUtils.verifyJWTClaims(signedJWT, oidcConfig) } just Runs
        every { ParserUtils.extractSubject(signedJWT) } returns expectedSubjectInfo

        // when
        val parsedSubjectInfo = TokenUtils.parseTokenToSubjectInfo(
            mockTokenResponse,
            oidcConfig
        )

        // then
        assertEquals(expectedSubjectInfo, parsedSubjectInfo)
    }

    @Test
    internal fun `should throw exception when parse invalid token`() {
        // given
        val idToken = "invalidToken"
        val mockTokenResponse = TokenResponse(
            "accessToken",
            "refreshToken",
            idToken,
            "bearer",
            200,
            "openid"
        )
        val idpPublicKey = "idpPublicKey"
        val servicePrivateKey = "servicePrivateKey"
        val oidcConfig = OIDCConfig("any", "any", "any", idpPublicKey, servicePrivateKey)

        mockkObject(ParserUtils)
        val signedJWT = mockk<SignedJWT>()
        every { ParserUtils.decryptJWE(idToken, servicePrivateKey) } returns signedJWT
        every { ParserUtils.verifyJWS(signedJWT, idpPublicKey) } returns false

        // when
        // then
        assertThrows<JWSSignatureVerifyException> {
            TokenUtils.parseTokenToSubjectInfo(
                mockTokenResponse,
                oidcConfig
            )
        }
    }

    @Test
    internal fun `should return addition JsonObject when parse with addition key`() {
        // given
        val idToken = "idToken"
        val mockTokenResponse = TokenResponse(
            "accessToken",
            "refreshToken",
            idToken,
            "bearer",
            200,
            "openid"
        )
        val idpPublicKey = "idpPublicKey"
        val servicePrivateKey = "servicePrivateKey"
        val oidcConfig = OIDCConfig("any", "any", "any", idpPublicKey, servicePrivateKey)
        val additionKey = "addition"

        mockkObject(ParserUtils)
        val signedJWT = mockk<SignedJWT>()
        val expectedJsonObject = mockk<JSONObject>()
        every { ParserUtils.decryptJWE(idToken, servicePrivateKey) } returns signedJWT
        every { ParserUtils.verifyJWS(signedJWT, idpPublicKey) } returns true
        every { ParserUtils.verifyJWTClaims(signedJWT, oidcConfig) } just Runs
        every { ParserUtils.extract(signedJWT, additionKey) } returns expectedJsonObject

        // when
        val parseJsonObject = TokenUtils.parseTokenToJsonObject(
            mockTokenResponse,
            oidcConfig,
            additionKey
        )

        // then
        assertEquals(expectedJsonObject, parseJsonObject)
    }

    @Test
    internal fun `should throw exception when parse invalid token with addition key`() {
        // given
        val idToken = "invalidToken"
        val mockTokenResponse = TokenResponse(
            "accessToken",
            "refreshToken",
            idToken,
            "bearer",
            200,
            "openid"
        )
        val idpPublicKey = "idpPublicKey"
        val servicePrivateKey = "servicePrivateKey"
        val oidcConfig = OIDCConfig("any", "any", "any", idpPublicKey, servicePrivateKey)

        mockkObject(ParserUtils)
        val signedJWT = mockk<SignedJWT>()
        every { ParserUtils.decryptJWE(idToken, servicePrivateKey) } returns signedJWT
        every { ParserUtils.verifyJWS(signedJWT, idpPublicKey) } returns false

        // when
        // then
        assertThrows<JWSSignatureVerifyException> {
            TokenUtils.parseTokenToJsonObject(
                mockTokenResponse,
                oidcConfig,
                "addition"
            )
        }
    }
}
