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
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODI4NzA2NzgsImV4cCI6MTU5MzA2OTI3MDY3OCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.QFjaJc6n9xNa4Zei5rLtvVk4dcAkkPBDlZTZv37X1HnVPMfP8UYbOp8MvYzX5jT8tj7Q9cdzSR4VGJ8bnR90cFIagcvCqRyQfQreYyRUwnyyg56c8i8F_2OP1T1Cc6ccv8NzZ3rRdCBuZvMIkGYn9PWQyB1jdJRC0ID9GqnCQoy1bKQDOz6id1P1zV6l6j8Xdmt05-FTb9qqvM0bEqlc_KMGpJLUbp7Mg_PQIExGiD2Mzrl6w4QTWNM0a_39UeD8v_Yrkz00dGZqzbHGxJK1iwgjJYurNnKNPgm7jISEpzBJSpjWUvV7p0nCSJLVX5Dql97OElaev6XUGeZ0MvPptw")
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString())

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Nonce is not equal to previous", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJleHAiOjE1OTMwNjk5NTIxNDksImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.jUnGoYyJhRlxKm-mZRCs5yFpnDI-N311VTmPWxaXerMrS1LtN3qx6U4vWe_B7r0qpZ8PD7I8V6DwEWXjo2auWKSVID2zPLBYvjX2hCwQ3Es68AqwPsb14YaiwHpG5-wpHTInOV0skVKx-06wM622h2or4yO8_uiYog09GQmKrTGCu_VGjVAsIP3lQf8NUNc3biciqTessgP1sL1yP9gaAnNv-3dTeVGvWEv8YADFoGMjbusMDM2OR4PnYxYnnXno2UxzmLStvHrbc0Rt2kzRgBW3olevw6IQKDvEWrz4j_jEC2r-lsTfwBtBdol2gM3BmI3Nsc66NE3bhW5M_BLvgQ")
        val oidcConfig = OIDCConfig("qweradsf")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Iat is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without exp`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU0MTg2MTEsImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.RH-6QADtrh-9tAz-Ibb1OGhuHE3Xu6eiFLg4kYGbgkkItc94bn1KSskm_PyEY7kiozXdzBiI8uw8RfW1fLN21KrVfC6vvBwnFowZspSZM9VPGKM1wxy8h_QC9OwjSl2tZdodZqa12JjUmxfiX3MQB5VOL8rDgDre-9bSBeiXjlR0oJwDEFwSv0ng2n8Kew7nFpmW-sbt2alQcYhy1rIY35hM2FGC78-xNMDIILzIDZG05awLY_QvrsVe8a01JmRktC1rtFG2f8SxI4f4QxTBGr5XSwz-wK0HrpiGpw53yZwvjHSKrB43-U9WdJGwpBQERp2T3i5ppTK4S6nBf9lq7Q")
        val oidcConfig = OIDCConfig("qweradsf")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU2Mjk0ODUsImV4cCI6MTU5Mjg5OTIyOTQ4NSwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.LCpL_vBgAk4qoVPedrzfATG8hHpt4ZsF9vDC90wdzb2-I9GKAXcesDxuhm2ElRluClM9PubzZP5hoq71BGyiqHHJTaTe7Wsa4uoTtVHHD6NSOFKJRiu8OHLJmIQIrpHWDGrb-A8uzPJscg6fvkmZ4dY8D2alcbeDNgQWcZtNKtxHi80lLar092GA5J9rljR5TFBGkScLDDAB7oDhad9lnp7z2jtxEoYrA4mOAJjCKXbbgGE_pc0zYyHT2U2A8Izcu_jrGnNOSYpxuZKu-av6Qyl0Dhli-21WGojnwjMeNg4s3WCk6YVlg6Ois2KrPjlImetJujsCQmNdDbVk0_hBaw")
        val oidcConfig = OIDCConfig("qweradsf")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp should after iat", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before now`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODYwNzI3NjUsImV4cCI6MTU5Mjk4NjA3Mjc2NiwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.hb2MS4SA_rxA6T-1N5FOCX9cytGfWZ4TxXuyHfSu4pV1KZ8jUi_-3o-Vn9F4SJHInxE20fhyj12WDu4BYQO-aFBmxWsMGip5AeYgv0ybL-YtM5ATOMLcuVuKpNrI4KDmh7btdRfGKjTpaGOofkE7QHQD5bCjUrDtU9SdxReeFcNm3lxX4jSjVMh8OITGGv-RbXGnTGI6bapb5vSjATTzCqf8BlY0qYOXpOBGVd97FzPiHXXBDlrUGbRIB8ePUZXf0CSLgt1v16E6eC5or9vta5MI_6k1XApt5ZHqIRQolun0YIisOUasae4CPa8HcCagGfmHwL6nNmz9zRnUrZsLdw")
        val oidcConfig = OIDCConfig("qweradsf")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp is expired", exception.message)
    }
}
