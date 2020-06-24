package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.InvalidArgumentException
import com.thoughtworks.sea.oidc.exception.InvalidJWTClaimException
import com.thoughtworks.sea.oidc.model.OIDCConfig
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParserUtilsTest {
    @Test
    internal fun `should return jws when decrypt jwe success`() {
        val idToken = MockJOSEData.JWE
        val expectedJWS = MockJOSEData.JWS
        val privateKey = this::class.java.getResource("/certs/servicePrivateKey.pem").readText()

        val actualJWS = ParserUtils.decryptJWE(idToken, privateKey)

        assertEquals(expectedJWS, actualJWS.serialize())
    }

    @Test
    internal fun `should return true when verify valid jws with public key`() {
        val signedJWT = SignedJWT.parse(MockJOSEData.JWS)
        val publicKey = this::class.java.getResource("/certs/idpPublicKey.pub").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKey))
    }

    @Test
    internal fun `should return true when verify valid jws with cert`() {
        val signedJWT = SignedJWT.parse(MockJOSEData.JWS)
        val publicKeyCert = this::class.java.getResource("/certs/idp.crt").readText()

        assertTrue(ParserUtils.verifyJWS(signedJWT, publicKeyCert))
    }

    @Test
    internal fun `should throw exception when verify valid jws with invalid key`() {
        val signedJWT = SignedJWT.parse(MockJOSEData.JWS)

        val exception = assertThrows<InvalidArgumentException> {
            ParserUtils.verifyJWS(signedJWT, "invalidKey")
        }
        assertEquals("Invalid Public Key or Certificate", exception.message)
    }

//    rt_hash: '',
//    at_hash: '',

//     TODO: Figure a way to return the nonce
//    iat: Date.now(),
//    exp: Date.now() + 24 * 60 * 60 * 1000,
//    iss: req.get('host'),
//    aud, client id
//    sub,

    @Test
    internal fun `should throw exception when verify jwt claims with null or blank nonce`() {
        val signedJWT = SignedJWT.parse(MockJOSEData.JWS)
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString())

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Nonce is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid nonce`() {
        val signedJWT = SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODI4NzA2NzgsImV4cCI6MTU5MzA2OTI3MDY3OCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.QFjaJc6n9xNa4Zei5rLtvVk4dcAkkPBDlZTZv37X1HnVPMfP8UYbOp8MvYzX5jT8tj7Q9cdzSR4VGJ8bnR90cFIagcvCqRyQfQreYyRUwnyyg56c8i8F_2OP1T1Cc6ccv8NzZ3rRdCBuZvMIkGYn9PWQyB1jdJRC0ID9GqnCQoy1bKQDOz6id1P1zV6l6j8Xdmt05-FTb9qqvM0bEqlc_KMGpJLUbp7Mg_PQIExGiD2Mzrl6w4QTWNM0a_39UeD8v_Yrkz00dGZqzbHGxJK1iwgjJYurNnKNPgm7jISEpzBJSpjWUvV7p0nCSJLVX5Dql97OElaev6XUGeZ0MvPptw")
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString())

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Nonce is not equal to previous", exception.message)
    }
}
