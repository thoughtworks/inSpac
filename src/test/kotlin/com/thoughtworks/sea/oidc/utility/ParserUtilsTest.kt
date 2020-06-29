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
    internal fun `should throw exception when verify jwt claims without nonce`() {
        val signedJWT = SignedJWT.parse(MockJOSEData.JWS)
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString(), "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid nonce`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODI4NzA2NzgsImV4cCI6MTU5MzA2OTI3MDY3OCwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.QFjaJc6n9xNa4Zei5rLtvVk4dcAkkPBDlZTZv37X1HnVPMfP8UYbOp8MvYzX5jT8tj7Q9cdzSR4VGJ8bnR90cFIagcvCqRyQfQreYyRUwnyyg56c8i8F_2OP1T1Cc6ccv8NzZ3rRdCBuZvMIkGYn9PWQyB1jdJRC0ID9GqnCQoy1bKQDOz6id1P1zV6l6j8Xdmt05-FTb9qqvM0bEqlc_KMGpJLUbp7Mg_PQIExGiD2Mzrl6w4QTWNM0a_39UeD8v_Yrkz00dGZqzbHGxJK1iwgjJYurNnKNPgm7jISEpzBJSpjWUvV7p0nCSJLVX5Dql97OElaev6XUGeZ0MvPptw")
        val oidcConfig = OIDCConfig(UUID.randomUUID().toString(), "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Nonce is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJleHAiOjE1OTMwNjk5NTIxNDksImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.jUnGoYyJhRlxKm-mZRCs5yFpnDI-N311VTmPWxaXerMrS1LtN3qx6U4vWe_B7r0qpZ8PD7I8V6DwEWXjo2auWKSVID2zPLBYvjX2hCwQ3Es68AqwPsb14YaiwHpG5-wpHTInOV0skVKx-06wM622h2or4yO8_uiYog09GQmKrTGCu_VGjVAsIP3lQf8NUNc3biciqTessgP1sL1yP9gaAnNv-3dTeVGvWEv8YADFoGMjbusMDM2OR4PnYxYnnXno2UxzmLStvHrbc0Rt2kzRgBW3olevw6IQKDvEWrz4j_jEC2r-lsTfwBtBdol2gM3BmI3Nsc66NE3bhW5M_BLvgQ")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iat is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without exp`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU0MTg2MTEsImlzcyI6ImxvY2FsaG9zdDo1MTU2IiwiYW1yIjpbInB3ZCJdLCJhdWQiOiIxMjEyMSIsInN1YiI6InM9dW5kZWZpbmVkLHU9LTExIn0.RH-6QADtrh-9tAz-Ibb1OGhuHE3Xu6eiFLg4kYGbgkkItc94bn1KSskm_PyEY7kiozXdzBiI8uw8RfW1fLN21KrVfC6vvBwnFowZspSZM9VPGKM1wxy8h_QC9OwjSl2tZdodZqa12JjUmxfiX3MQB5VOL8rDgDre-9bSBeiXjlR0oJwDEFwSv0ng2n8Kew7nFpmW-sbt2alQcYhy1rIY35hM2FGC78-xNMDIILzIDZG05awLY_QvrsVe8a01JmRktC1rtFG2f8SxI4f4QxTBGr5XSwz-wK0HrpiGpw53yZwvjHSKrB43-U9WdJGwpBQERp2T3i5ppTK4S6nBf9lq7Q")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before iat`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODU2Mjk0ODUsImV4cCI6MTU5Mjg5OTIyOTQ4NSwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.LCpL_vBgAk4qoVPedrzfATG8hHpt4ZsF9vDC90wdzb2-I9GKAXcesDxuhm2ElRluClM9PubzZP5hoq71BGyiqHHJTaTe7Wsa4uoTtVHHD6NSOFKJRiu8OHLJmIQIrpHWDGrb-A8uzPJscg6fvkmZ4dY8D2alcbeDNgQWcZtNKtxHi80lLar092GA5J9rljR5TFBGkScLDDAB7oDhad9lnp7z2jtxEoYrA4mOAJjCKXbbgGE_pc0zYyHT2U2A8Izcu_jrGnNOSYpxuZKu-av6Qyl0Dhli-21WGojnwjMeNg4s3WCk6YVlg6Ois2KrPjlImetJujsCQmNdDbVk0_hBaw")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp should after iat")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with exp before now`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6InAtQjhVZldNMzR4OFYzbkdkYlI0bURoR0tJV3JwV013NHdOU1ZYY0FwZU0ifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTI5ODYwNzI3NjUsImV4cCI6MTU5Mjk4NjA3Mjc2NiwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6IjEyMTIxIiwic3ViIjoicz11bmRlZmluZWQsdT0tMTEifQ.hb2MS4SA_rxA6T-1N5FOCX9cytGfWZ4TxXuyHfSu4pV1KZ8jUi_-3o-Vn9F4SJHInxE20fhyj12WDu4BYQO-aFBmxWsMGip5AeYgv0ybL-YtM5ATOMLcuVuKpNrI4KDmh7btdRfGKjTpaGOofkE7QHQD5bCjUrDtU9SdxReeFcNm3lxX4jSjVMh8OITGGv-RbXGnTGI6bapb5vSjATTzCqf8BlY0qYOXpOBGVd97FzPiHXXBDlrUGbRIB8ePUZXf0CSLgt1v16E6eC5or9vta5MI_6k1XApt5ZHqIRQolun0YIisOUasae4CPa8HcCagGfmHwL6nNmz9zRnUrZsLdw")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Exp is expired")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without iss`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFZREkybS1URlpJN19LY0VXMjBfRzkya2N2NFJubDdHdDg2VTdNQXNQN2sifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTM0MDI5MjIzMTAsImV4cCI6MTU5MzQ4OTMyMjMxMCwiYW1yIjpbInB3ZCJdLCJhdWQiOiJjbGllbnRJZCIsInN1YiI6InM9dW5kZWZpbmVkIn0.VvszOm_b1WrRumVJO4Vc775hPs016U73R9cFISfoPt1dMofVN60d6Fc5jLbBA2A8Veo8seefRaZ8-EQ3_mNnwb-vGCqO6sdlEQFralem3WNltVv8Cis58HoCxq5YeFSOXfDlxj2AtylblcZLYJce2fPARITl7IDcjdTdk2F61qhjfIZnNsdXtvKElGeNLJ8UytxXGV3jq1-TUuKVfhvDB5GV0R_eQLkak2J42T0YB0WzXZBtth3hw2XXPgYPpYsf8H22Si-1w2GEwbvuJLGdi4vKZ6ZezUxS3ZteMP78ac4Ql8McP1auu9N_vFc9A341OhpbFkJTe0K_lui293_s4w")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid iss`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFZREkybS1URlpJN19LY0VXMjBfRzkya2N2NFJubDdHdDg2VTdNQXNQN2sifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTM0MDMxNTUxMDIsImV4cCI6MTU5MzQ4OTU1NTEwMiwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6ImNsaWVudElkIiwic3ViIjoicz11bmRlZmluZWQifQ.QW4zv1tifHho4t4Hq1iAC_QFow9sTwa86kebzPDasZYZAJ2ZHc2bHJs3ZNyFa43ld-4D2gQ3W3frMo0kQuur-H6_Yob7nMcQiYKLsUcMmgWCTk8II-oo1ACnOq6Df3Dxe7Mwys-DeLonsyriQdVhvMQWZTgr5g6NNEEunXGjhsQhIl_lDnXJNuEviGKEJY3yBAwFMfUg7Fdo0AeoGY13Rjeztu8id4rV-ATI-_2TZMLsxOFn99JFyTt-XXHp3T9vcVh7J4GUsvJcFS1Cbb6Ay2mK0mVeblD9esNJLLz1izfATEhPDtUHhFczIp0ym4Qq8DmqJyZEhqXCxxmxQR7gLQ")
        val oidcConfig = OIDCConfig("qweradsf", "invalidIss", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Iss is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid aud`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFZREkybS1URlpJN19LY0VXMjBfRzkya2N2NFJubDdHdDg2VTdNQXNQN2sifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTM0MDI5OTIyMTcsImV4cCI6MTU5MzQ4OTM5MjIxNywiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6ImNsaWVudCIsInN1YiI6InM9dW5kZWZpbmVkIn0.huXFQvrdg4C70zkVQXZ3bk6alDVHHMYiixS9B9NKJHKVoT9UfuMy7Hd-fKI_Z28zWnamjxnrAlL9BHl-43rWr2KABn7ULbvfdKmwrITJFCw-GPrJm8oyR2rlYPaMNu0M7uGhMU46LkuKKDoH4eGV5yqNIKB9vpLSyJQx7ouNN_B_jjXNXMxbtth2Ue7CyE79ZFQ_Z7-XxAUn5mN95vOA3oYyyZJU-heLvzDUxw0Pd0kks2Z8Qx4X-2frE5jO0TB4gK6TbvzmTKvqJGateU17uyfjgWW11XxB1B8UQMwQ50USPsaBCQEuurDGnPsx9tK3LJQHeuRg2h1ItzRq28UM7A")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Aud is not equal to OIDC config")
    }

    @Test
    internal fun `should throw exception when verify jwt claims without sub`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFZREkybS1URlpJN19LY0VXMjBfRzkya2N2NFJubDdHdDg2VTdNQXNQN2sifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTM0MDE5MTAzODksImV4cCI6MTU5MzQ4ODMxMDM4OSwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6ImNsaWVudElkIn0.f32aPva3VPP4X9ilf2bF8TVznRqocvDu4VU4jivcxETmm0JS4zuD-qPzPzC4XAWhUbJABHV8CgyyCS_6uau_4VDVjPjLU_ucZR2-0uoTG7NEh92XPyXUQa2lHrfo9_7_JHcQQROTPltKF84XGlR8xrgye0Ho1G_PbC13VgMNRdj3o6wKVU-yr2s_QhCjNEFkLYFVRyaRRRQaF_oCY28sjWEQXLPRLWSnhSm-LWDUXX-zXHxaW9b957beEf7mHE1m6cKkLpLZhMSYkw1byjH-CpXaoZ9APuP9b2LH0zZE3KHhgSICgey9DmrmusWy9L53IoYpsZ-7lq-fml5rU5k1fw")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sub is missing")
    }

    @Test
    internal fun `should throw exception when verify jwt claims with invalid sub`() {
        val signedJWT =
            SignedJWT.parse("eyJhbGciOiJSUzI1NiIsImtpZCI6ImFZREkybS1URlpJN19LY0VXMjBfRzkya2N2NFJubDdHdDg2VTdNQXNQN2sifQ.eyJydF9oYXNoIjoiIiwiYXRfaGFzaCI6IiIsIm5vbmNlIjoicXdlcmFkc2YiLCJpYXQiOjE1OTM0MDIxNjYxNzUsImV4cCI6MTU5MzQ4ODU2NjE3NSwiaXNzIjoibG9jYWxob3N0OjUxNTYiLCJhbXIiOlsicHdkIl0sImF1ZCI6ImNsaWVudElkIiwic3ViIjoicz11bmRlZmluZWQifQ.CxqDiI2lj1ys28ivP52u6ctadTNXcdLeMDFQ6gw1xZ42Z4b_n3uwexItNkiN35ZAvnU38c4h95BwjuaJjZsWRKim5eY8mytf8ahrVox6d2V2HmbPVjcuDHHPN2CK4h4oiJjp_P0bgA12RvmAVrtHhxgk0x-tSLh3tl0kLbm5vZqJKwP1oW1iKyROP95Cw1qjWbUqSkxyT0t6iT6D4heH0AZTs2oC4F8C1-F8uucwXPH_3tS5G3ap9M4x61kRVCj_GDFWDiDnlgTb_VTSBAMbRfODJyMKWA-xrGGJdSzrmaOKCfBPsTrwe8a-E4thZyyLpPDMfOvKHwQCQvEBlEnN3A")
        val oidcConfig = OIDCConfig("qweradsf", "localhost:5156", "clientId")

        assertThrowsInvalidJWTClaimException(signedJWT, oidcConfig, "Sud should contain uuid at least")
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
}
