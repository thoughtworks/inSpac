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
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString(), "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Nonce is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid nonce`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODI4NzA2NzgsImV4cCI6MTU5MzA2OTI3MDY3OCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.QFjaJc6n9xNa4Zei5rLtvVk4dcAkkPBDlZTZv37X1HnVPMfP8UYbOp8MvYzX5jT8tj7Q9cdzSR4VGJ8bnR90cFIagcvCqRyQfQreYyRUwnyyg56c8i8F_2OP1T1Cc6ccv8NzZ3rRdCBuZvMIkGYn9PWQyB1jdJRC0ID9GqnCQoy1bKQDOz6id1P1zV6l6j8Xdmt05-FTb9qqvM0bEqlc_KMGpJLUbp7Mg_PQIExGiD2Mzrl6w4QTWNM0a_39UeD8v_Yrkz00dGZqzbHGxJK1iwgjJYurNnKNPgm7jISEpzBJSpjWUvV7p0nCSJLVX5Dql97OElaev6XUGeZ0MvPptw")
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString(), "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Nonce is not equal to OIDC config", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJleHAiOjE1OTMwNjk5NTIxNDksImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.jUnGoYyJhRlxKm-mZRCs5yFpnDI-N311VTmPWxaXerMrS1LtN3qx6U4vWe_B7r0qpZ8PD7I8V6DwEWXjo2auWKSVID2zPLBYvjX2hCwQ3Es68AqwPsb14YaiwHpG5-wpHTInOV0skVKx-06wM622h2or4yO8_uiYog09GQmKrTGCu_VGjVAsIP3lQf8NUNc3biciqTessgP1sL1yP9gaAnNv-3dTeVGvWEv8YADFoGMjbusMDM2OR4PnYxYnnXno2UxzmLStvHrbc0Rt2kzRgBW3olevw6IQKDvEWrz4j_jEC2r-lsTfwBtBdol2gM3BmI3Nsc66NE3bhW5M_BLvgQ")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Iat is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without exp`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU0MTg2MTEsImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.RH-6QADtrh-9tAz-Ibb1OGhuHE3Xu6eiFLg4kYGbgkkItc94bn1KSskm_PyEY7kiozXdzBiI8uw8RfW1fLN21KrVfC6vvBwnFowZspSZM9VPGKM1wxy8h_QC9OwjSl2tZdodZqa12JjUmxfiX3MQB5VOL8rDgDre-9bSBeiXjlR0oJwDEFwSv0ng2n8Kew7nFpmW-sbt2alQcYhy1rIY35hM2FGC78-xNMDIILzIDZG05awLY_QvrsVe8a01JmRktC1rtFG2f8SxI4f4QxTBGr5XSwz-wK0HrpiGpw53yZwvjHSKrB43-U9WdJGwpBQERp2T3i5ppTK4S6nBf9lq7Q")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU2Mjk0ODUsImV4cCI6MTU5Mjg5OTIyOTQ4NSwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.LCpL_vBgAk4qoVPedrzfATG8hHpt4ZsF9vDC90wdzb2-I9GKAXcesDxuhm2ElRluClM9PubzZP5hoq71BGyiqHHJTaTe7Wsa4uoTtVHHD6NSOFKJRiu8OHLJmIQIrpHWDGrb-A8uzPJscg6fvkmZ4dY8D2alcbeDNgQWcZtNKtxHi80lLar092GA5J9rljR5TFBGkScLDDAB7oDhad9lnp7z2jtxEoYrA4mOAJjCKXbbgGE_pc0zYyHT2U2A8Izcu_jrGnNOSYpxuZKu-av6Qyl0Dhli-21WGojnwjMeNg4s3WCk6YVlg6Ois2KrPjlImetJujsCQmNdDbVk0_hBaw")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp should after iat", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before now`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODYwNzI3NjUsImV4cCI6MTU5Mjk4NjA3Mjc2NiwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.hb2MS4SA_rxA6T-1N5FOCX9cytGfWZ4TxXuyHfSu4pV1KZ8jUi_-3o-Vn9F4SJHInxE20fhyj12WDu4BYQO-aFBmxWsMGip5AeYgv0ybL-YtM5ATOMLcuVuKpNrI4KDmh7btdRfGKjTpaGOofkE7QHQD5bCjUrDtU9SdxReeFcNm3lxX4jSjVMh8OITGGv-RbXGnTGI6bapb5vSjATTzCqf8BlY0qYOXpOBGVd97FzPiHXXBDlrUGbRIB8ePUZXf0CSLgt1v16E6eC5or9vta5MI_6k1XApt5ZHqIRQolun0YIisOUasae4CPa8HcCagGfmHwL6nNmz9zRnUrZsLdw")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Exp is expired", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iss`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODc3NDg2NDcsImV4cCI6MTU5MzA3NDE0ODY0NywiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.oKUaFIQ9igSSZXonmAn-a2VQwHd9YCnb2Ru_iQa97U-GSZSCSc8VOP-Q7KfIGyblfqhJ-6r_b98q0A5hD2wS7JeexKDpsunKLIeGAXjBeM2BL-OSohs2zroHMgOW2D0ukBJeD0Chx8lzhWE0-Xjah_4klWj7fV3aSt8rmB91MLFqjK7Uw9fIJ3H48dlZtqlWckX6VtvV7MAlVrzLTx94_MRQJV1wsPRR9YDlyYw_WaHXBQh4xHadMYiANaTheOucHBlnV96AtbdO-je42TCs-4Oo_YWFaCBhHl-PwyxoFnwktYogWve1zWgPON-dwgwJ8VpivxlF1KWJTroi0C92LQ")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Iss is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid iss`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODgwOTI0MjgsImV4cCI6MTU5MzA3NDQ5MjQyOCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.bCM7OyLMB-VthSYm7FjYt33zfNigEW76RfeMmCRYwE_MdobFIrZvCp2RCWC5WGxGjc8yrlPGMwXt9BuAJ79M4q6aBHEW3sSafkxegmv8bzQA0HxKJ8EE2E8SDB8LuRT6HNO9tLhMsndZUfX0mrwvEA8Web1irr_PvO1LAC1kFHYKmIHGJtmcojjaDnZ35BsBmLuMtQmvxlkMMNK1kavu46DtJp0bLF-YSjdo3aT0SKCWlMlQTCaILV0-8Ed56dUBjse-oWwR7AYOY7HEbYX02XU-1k3lVsNqwCaOcaF8dkLNflnqjP9qd3_TEH7v4OaMB2fy6PUwwTUVi9fkfDK28Q")
        val oidcConfig = OIDCConfig("qweradsf", "invalidIss", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Iss is not equal to OIDC config", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims without aud`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODgzODE5NDYsImV4cCI6MTU5MzA3NDc4MTk0NiwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.Tp-nBwBSynU1sAv-eJaBZuYzcuz5LFL6WM_e4pOyIK9Cob7qRDchR81uXf1fRnZ4dq4gKhEtn4EUPMl_d5W10_oM55J4p9IrQ1vPQ60zGF90q3gJx0IrP0N8dUSacksPOReT9BhrtmRbV8T8GMpqRmFn0hngSmng1h_kXfqt5l15z6oTfnyeMT9G9CWoceWb8P3MG0fPeJ0t-O-OaFtgcpdsUzoyY2c1BI5qi52k0L1i9NkL505145H4R8undY9kWzYJk0CguWKdUNA-Aiyo7UrICDTqH63O4Jgq1JcY07Bpc71jI4kwontAwMesAYen7OYpVP0wdu-wL41wwh_GKg")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Aud is missing", exception.message)
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid aud`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODg2NDEyMzgsImV4cCI6MTU5MzA3NTA0MTIzOCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.VFPrTmL1cfYaUcfBe5iZKshUvEojxbTnP73S8fXiWVMIUkHUf64WuBDhjyjjsZlW-56sLCgH3E4Ikf09yhp9XdXXsLnO5bCKH-GhHMmWJJn7mzvB-B6CwA5cosvSWmt2QNGogOYDxJWYylg7RbqphRkR5HLJhytmb4-ZINKdUTYqEJrq1dvUNZvOg5_j6UbPtk1mCPQtK-JyUAShmSOwMEw7RvVMwN_mXxcP5n0N1BprAVT9E7mshgqNMziC6PdP-LOS4mOaKfFC21kSTxUjHSbpRJIM9TqVjiLEqC0kHJ0tFovkgAkTSFtYXy5rJKR11nyG2vG8CC2NQSX3G7WJ0A")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        val exception = assertThrows<InvalidJWTClaimException> {
            ParserUtils.verifyJWTClaims(signedJWT, oidcConfig)
        }
        assertEquals("Aud is not equal to OIDC config", exception.message)
    }
}
