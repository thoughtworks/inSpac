package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jose.EncryptionMethod
import com.nimbusds.jose.JWEAlgorithm
import com.nimbusds.jose.JWEHeader
import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSAEncrypter
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.InvalidJWTClaimException
import com.thoughtworks.sea.oidc.model.OIDCConfig
import com.thoughtworks.sea.oidc.model.ParsedSubjectInfo
import java.time.Instant
import java.util.Date
import java.util.UUID
import net.minidev.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParserUtilsTest {
    @Test
    internal fun `should return jws when decrypt jwe success`() {
        val signedJWT = MockPassSignedJWT.Builder().build()
        val expectedJWS = signedJWT.serialize()
        val idToken = encryptMockPassJWS(signedJWT)
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()

        val actualJWS = ParserUtils.decryptJWE(idToken, privateKey)

        assertEquals(expectedJWS, actualJWS.serialize())
    }

    @Test
    internal fun `should return true when verify valid jws with public key`() {
        val signedJWT = MockPassSignedJWT.Builder().build()
        val publicKey = this::class.java.getResource("/certs/idpPublicKey.pub").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKey))
    }

    @Test
    internal fun `should return true when verify valid jws with cert`() {
        val signedJWT = MockPassSignedJWT.Builder().build()
        val publicKeyCert = this::class.java.getResource("/certs/idp.crt").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKeyCert))
    }

    @Test
    internal fun `should throw exception when verify jwt claims without nonce`() {
        val signedJWT = MockPassSignedJWT.Builder().nonce(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is missing")
    }

    @Test
    internal fun `should throw exception when verify nonce of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassSignedJWT.Builder().nonce("different nonce").build()
        val oidcConfig = generateOIDCConfig(nonce = "a9424553-a6b2-4c1e-9d18-0d7d9b11f4f8")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iat`() {
        val signedJWT = MockPassSignedJWT.Builder().iat(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iat is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with iat after now`() {
        val signedJWT = MockPassSignedJWT.Builder().iat(Instant.now().plusSeconds(999)).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iat should before now")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without exp`() {
        val signedJWT = MockPassSignedJWT.Builder().exp(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before iat`() {
        val now = Instant.now()
        val signedJWT = MockPassSignedJWT.Builder()
            .exp(now.minusSeconds(100))
            .iat(now)
            .build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp should after iat")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before now`() {
        val now = Instant.now()
        val signedJWT = MockPassSignedJWT.Builder()
            .exp(now.minusSeconds(50))
            .iat(now.minusSeconds(100))
            .build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is expired")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iss`() {
        val signedJWT = MockPassSignedJWT.Builder().iss(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is missing")
    }

    @Test
    internal fun `should throw exception when verify iss of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassSignedJWT.Builder().iss("invalidIss").build()
        val oidcConfig = generateOIDCConfig(host = "localhost:5156")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify aud of jwt claims not equal to OIDCConfig`() {
        val signedJWT = MockPassSignedJWT.Builder().aud("invalidAud").build()
        val oidcConfig = generateOIDCConfig(clientId = "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Aud is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without sub`() {
        val signedJWT = MockPassSignedJWT.Builder().sub(null).build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sub is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid sub`() {
        val signedJWT = MockPassSignedJWT.Builder().sub("s=1000").build()
        val oidcConfig = generateOIDCConfig()

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sud should contain uuid at least")
    }

    @Test
    internal fun `should return parsed subject info when extract sub in jwt`() {
        val expectedSubject = ParsedSubjectInfo("S7515010E", UUID.randomUUID().toString())
        val signedJWT =
            MockPassSignedJWT.Builder().sub("s=${expectedSubject.nricNumber},u=${expectedSubject.uuid}").build()

        val actualSubject = ParserUtils.extractSubject(signedJWT)

        assertEquals(expectedSubject, actualSubject)
    }

    @Test
    internal fun `should return parsed subject info with null nric when extract sub without nric in jwt`() {
        val expectedSubject = ParsedSubjectInfo(null, UUID.randomUUID().toString())
        val signedJWT =
            MockPassSignedJWT.Builder().sub("u=${expectedSubject.uuid}").build()

        val actualSubject = ParserUtils.extractSubject(signedJWT)

        assertEquals(expectedSubject, actualSubject)
    }

    @Test
    internal fun `should return a json object when extract with a key`() {
        val expectedJsonObject = JSONObject(mapOf("name" to "json", "gender" to "female"))
        val signedJWT = MockPassSignedJWT.Builder().addition(expectedJsonObject).build()
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
    ) = OIDCConfig(nonce, host, clientId)

    private class MockPassSignedJWT private constructor() {

        data class Builder(
            var nonce: String? = "a9424553-a6b2-4c1e-9d18-0d7d9b11f4f8",
            var iat: Instant? = Instant.now(),
            var exp: Instant? = Instant.now().plusSeconds(100),
            var iss: String? = "localhost:5156",
            var aud: String? = "clientId",
            var sub: String? = "u=${UUID.randomUUID()}",
            var addition: JSONObject? = null
        ) {

            fun nonce(nonce: String?) = apply { this.nonce = nonce }
            fun iat(iat: Instant?) = apply { this.iat = iat }
            fun exp(exp: Instant?) = apply { this.exp = exp }
            fun iss(iss: String?) = apply { this.iss = iss }
            fun aud(aud: String?) = apply { this.aud = aud }
            fun sub(sub: String?) = apply { this.sub = sub }
            fun addition(addition: JSONObject) = apply { this.addition = addition }
            fun build(): SignedJWT {
                val idpPrivateKey = this::class.java.getResource("/certs/idpPrivateKey.pem").readText()
                val jwk = JWK.parseFromPEMEncodedObjects(idpPrivateKey)
                val jwsHeader = JWSHeader.Builder(JWSAlgorithm.RS256).keyID(jwk.keyID).build()
                val jwtClaimsSet = JWTClaimsSet.Builder().claim("nonce", this.nonce)
                    .issueTime(this.iat?.let { Date.from(it) })
                    .expirationTime(this.exp?.let { Date.from(it) })
                    .issuer(this.iss)
                    .audience(this.aud)
                    .subject(this.sub)
                    .claim("addition", this.addition)
                    .build()
                return SignedJWT(jwsHeader, jwtClaimsSet).apply {
                    sign(RSASSASigner(jwk.toRSAKey()))
                }
            }
        }
    }

    private fun encryptMockPassJWS(signedJWT: SignedJWT): String {
        val servicePublicKey = this::class.java.getResource("/certs/servicePublicKey.pub").readText()
        val jwk = JWK.parseFromPEMEncodedObjects(servicePublicKey)
        val jweObject = JWEObject(
            JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512)
                .contentType("JWT")
                .build(),
            Payload(signedJWT)
        )
        jweObject.encrypt(RSAEncrypter(jwk.toRSAKey()))
        return jweObject.serialize()
    }
}
