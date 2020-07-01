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
import java.time.Instant
import java.util.Date
import java.util.UUID
import net.minidev.json.JSONObject

internal class MockPassJWT private constructor() {

    data class JWSBuilder(
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

    companion object {
        internal fun encryptMockPassJWS(signedJWT: SignedJWT): String {
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
}
