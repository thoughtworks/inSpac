package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.InvalidJWTClaimException
import com.thoughtworks.sea.oidc.model.OIDCConfig
import com.thoughtworks.sea.oidc.model.ParsedSubjectInfo
import java.time.Instant
import java.util.UUID
import net.minidev.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParserUtilsTest {
    @Test
    internal fun `should return jws when decrypt jwe success`() {
        val signedJWT = MockPassJWT.JWSBuilder().build()
        val expectedJWS = signedJWT.serialize()
        val idToken = MockPassJWT.encryptMockPassJWS(signedJWT)
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()

        val actualJWS = ParserUtils.decryptJWE(idToken, privateKey)

        assertEquals(expectedJWS, actualJWS.serialize())
    }

    @Test
    internal fun `should return true when verify valid jws with public key`() {
        val signedJWT = MockPassJWT.JWSBuilder().build()
        val publicKey = this::class.java.getResource("/certs/idpPublicKey.pub").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKey))
    }

    @Test
    internal fun `should return true when verify valid jws with cert`() {
        val signedJWT = MockPassJWT.JWSBuilder().build()
        val publicKeyCert = this::class.java.getResource("/certs/idp.crt").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKeyCert))
    }

    @Test
    internal fun `should throw exception when verify jwt claims without nonce`() {
        val signedJWT = MockPassJWT.JWSBuilder().nonce(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is missing")
    }

    @Test
    internal fun `should throw exception when verify nonce of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassJWT.JWSBuilder().nonce("different nonce").build()
        val oidcConfig = generateOIDCConfig(nonce = "a9424553-a6b2-4c1e-9d18-0d7d9b11f4f8")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iat`() {
        val signedJWT = MockPassJWT.JWSBuilder().iat(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iat is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with iat after now`() {
        val signedJWT = MockPassJWT.JWSBuilder().iat(Instant.now().plusSeconds(999)).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iat should before now")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without exp`() {
        val signedJWT = MockPassJWT.JWSBuilder().exp(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before iat`() {
        val now = Instant.now()
        val signedJWT = MockPassJWT.JWSBuilder()
            .exp(now.minusSeconds(100))
            .iat(now)
            .build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp should after iat")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before now`() {
        val now = Instant.now()
        val signedJWT = MockPassJWT.JWSBuilder()
            .exp(now.minusSeconds(50))
            .iat(now.minusSeconds(100))
            .build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is expired")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iss`() {
        val signedJWT = MockPassJWT.JWSBuilder().iss(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is missing")
    }

    @Test
    internal fun `should throw exception when verify iss of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassJWT.JWSBuilder().iss("invalidIss").build()
        val oidcConfig = generateOIDCConfig(host = "localhost:5156")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify aud of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassJWT.JWSBuilder().aud("invalidAud").build()
        val oidcConfig = generateOIDCConfig(clientId = "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Aud is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without sub`() {
        val signedJWT = MockPassJWT.JWSBuilder().sub(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sub is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid sub`() {
        val signedJWT = MockPassJWT.JWSBuilder().sub("s=1000").build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sud should contain uuid at least")
    }

    @Test
    internal fun `should return parsed subject info when extract sub in jwt`() {
        val expectedSubject = ParsedSubjectInfo("S7515010E", UUID.randomUUID().toString())
        val signedJWT =
            MockPassJWT.JWSBuilder().sub("s=${expectedSubject.nricNumber},u=${expectedSubject.uuid}").build()

        val actualSubject = ParserUtils.extractSubject(signedJWT)

        assertEquals(expectedSubject, actualSubject)
    }

    @Test
    internal fun `should return parsed subject info with null nric when extract sub without nric in jwt`() {
        val expectedSubject = ParsedSubjectInfo(null, UUID.randomUUID().toString())
        val signedJWT =
            MockPassJWT.JWSBuilder().sub("u=${expectedSubject.uuid}").build()

        val actualSubject = ParserUtils.extractSubject(signedJWT)

        assertEquals(expectedSubject, actualSubject)
    }

    @Test
    internal fun `should return a json object when extract with a key`() {
        val expectedJsonObject = JSONObject(mapOf("name" to "json", "gender" to "female"))
        val signedJWT = MockPassJWT.JWSBuilder().addition(expectedJsonObject).build()
        val key = "addition"

        val actualJsonObject = ParserUtils.extract(signedJWT, key)

        assertEquals(expectedJsonObject.toJSONString(), actualJsonObject.toJSONString())
    }

    private fun assertThrowsInvalidJWTClaimException(
        signedJWT: SignedJWT,
        oidcConfig: OIDCConfig,
        exceptedMessage: String
    ) {
        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals(exceptedMessage, exception.message)
    }

    private fun generateOIDCConfig(
        nonce: String = "a9424553-a6b2-4c1e-9d18-0d7d9b11f4f8",
        host: String = "localhost:5156",
        clientId: String = "clientId"
    ) = OIDCConfig(nonce, host, clientId, "idpPublicKey", "servicePrivateKey")
}
