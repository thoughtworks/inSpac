package com.thoughtworks.sea.oidc.utility

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ParserUtilsTest {
    @Test
    internal fun `should return jws when decrypt jwe success`() {
        val idToken = MockJOSEData.JWE
        val expectedJWS = MockJOSEData.JWS
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()

        val actualJWS = ParserUtils.decryptJWE(idToken, privateKey)

        Assertions.assertEquals(expectedJWS, actualJWS.serialize())
    }

    @Test
    internal fun `should return true when verify valid jws`() {
        val idToken = MockJOSEData.JWE
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()
        val publicKey = this::class.java.getResource("/certs/signPublicKey.pub").readText()

        val signedJWT = ParserUtils.decryptJWE(idToken, privateKey)

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKey))
    }
}
