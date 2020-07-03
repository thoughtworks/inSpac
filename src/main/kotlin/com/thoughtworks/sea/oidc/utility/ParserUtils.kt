package com.thoughtworks.sea.oidc.utility

import com.nimbusds.jose.JWEObject
import com.nimbusds.jose.crypto.RSADecrypter
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import com.thoughtworks.sea.oidc.exception.InvalidJWTClaimException
import com.thoughtworks.sea.oidc.model.ParseTokenParams
import com.thoughtworks.sea.oidc.model.ParsedSubjectInfo
import java.time.Instant

/** A utility class for parsing token. */
class ParserUtils {
    companion object {

        private const val ISSUED_AT_CLAIM = "iat"
        private const val EXPIRATION_TIME_CLAIM = "exp"
        private const val NONCE_CLAIM = "nonce"
        private const val SUBJECT_NRIC_PREFIX = "s="
        private const val SUBJECT_UUID_PREFIX = "u="
        private const val COMMA = ","

        /**
         * The function is used to decrypt JWE to SignedJWT
         * @param idToken parameter for idToken from token response
         * @param privateKeyPem parameter for service private key to decrypt JWE
         * @return SignedJWT that a signed JSON Web Token (JWT) representation of this payload
         */
        // not support PKCS#1 private key decrypt yet
        internal fun decryptJWE(idToken: String, privateKeyPem: String): SignedJWT {
            val jweObject = JWEObject.parse(idToken)
            val jwk = JWK.parseFromPEMEncodedObjects(privateKeyPem)
            jweObject.decrypt(RSADecrypter(jwk.toRSAKey()))

            return jweObject.payload.toSignedJWT()
        }

        /**
         * The function is used to verify the signature of this JWS object with the specified verifier
         * @param signedJWT parameter for a signed JSON Web Token (JWT) representation of this payload
         * @param key parameter for IDP public key to verify signature
         * @return {@code true} if the signature was successfully verified,
         *         else {@code false}
         */
        internal fun verifyJWS(signedJWT: SignedJWT, key: String): Boolean {
            val jwk = JWK.parseFromPEMEncodedObjects(key)

            return signedJWT.verify(RSASSAVerifier(jwk.toRSAKey()))
        }

        /**
         * The function is used to verify payload of JWS object
         * @param signedJWT parameter for a signed JSON Web Token (JWT) representation of this payload
         * @param parseTokenParams parameter for verifying
         * @throws InvalidJWTClaimException if the payload was unsuccessful verified
         */
        // Mockpass not implement refresh token yet, `rt_hash` depends on it, and unknown the hash algorithm
        // Mockpass not implement access token yet, `at_hash` depends on it, and unknown the hash algorithm
        internal fun verifyJWTClaims(signedJWT: SignedJWT, parseTokenParams: ParseTokenParams) {
            val jwtClaimsSet = signedJWT.jwtClaimsSet

            verifyNonce(jwtClaimsSet, parseTokenParams)
            verifyIssuedAndExpirationTime(signedJWT)
            verifyIssuer(jwtClaimsSet, parseTokenParams)
            verifyAudience(jwtClaimsSet, parseTokenParams)
            verifySubject(jwtClaimsSet)
        }

        /**
         * The function is used to extract nricNumber and uuid from sub of payload
         * @param signedJWT parameter for a signed JSON Web Token (JWT) representation of this payload
         * @return ParsedSubjectInfo with nricNumber and uuid
         */
        internal fun extractSubject(signedJWT: SignedJWT): ParsedSubjectInfo {
            val subjectInfos = signedJWT.jwtClaimsSet.subject.split(COMMA)
            val nricNumber = subjectInfos.find { it.startsWith(SUBJECT_NRIC_PREFIX) }
            val uuid = subjectInfos.find { it.startsWith(SUBJECT_UUID_PREFIX) }.orEmpty()

            return ParsedSubjectInfo(
                nricNumber?.substring(SUBJECT_NRIC_PREFIX.length),
                uuid.substring(SUBJECT_UUID_PREFIX.length)
            )
        }

        /**
         * The function is used to extract corresponding value by the key of claims from payload
         * @param signedJWT parameter for a signed JSON Web Token (JWT) representation of this payload
         * @param key parameter for the key of claims to get JsonObject
         * @return The value of a key of payload, {@code null} if not found.
         */
        internal fun extract(signedJWT: SignedJWT, key: String) = signedJWT.jwtClaimsSet.getJSONObjectClaim(key)

        private fun verifySubject(jwtClaimsSet: JWTClaimsSet) {
            val sub = jwtClaimsSet.subject ?: throw InvalidJWTClaimException("Sub is missing")
            if (!sub.contains(SUBJECT_UUID_PREFIX)) {
                throw InvalidJWTClaimException("Sud should contain uuid at least")
            }
        }

        private fun verifyAudience(jwtClaimsSet: JWTClaimsSet, parseTokenParams: ParseTokenParams) {
            if (jwtClaimsSet.audience.none { it == parseTokenParams.clientId }) {
                throw InvalidJWTClaimException("Aud is not equal to client id")
            }
        }

        private fun verifyIssuer(jwtClaimsSet: JWTClaimsSet, parseTokenParams: ParseTokenParams) {
            val iss = jwtClaimsSet.issuer ?: throw InvalidJWTClaimException("Iss is missing")
            if (iss != parseTokenParams.host) {
                throw InvalidJWTClaimException("Iss is not equal to idp host")
            }
        }

        private fun verifyIssuedAndExpirationTime(signedJWT: SignedJWT) {
            val jsonObject = signedJWT.payload.toJSONObject()
            val iat = jsonObject.getAsNumber(ISSUED_AT_CLAIM) ?: throw InvalidJWTClaimException("Iat is missing")
            val now = Instant.now()
            if (now.epochSecond < iat.toLong()) {
                throw InvalidJWTClaimException("Iat should before now")
            }
            val exp = jsonObject.getAsNumber(EXPIRATION_TIME_CLAIM) ?: throw InvalidJWTClaimException("Exp is missing")
            if (exp.toLong() < iat.toLong()) {
                throw InvalidJWTClaimException("Exp should after iat")
            }
            if (exp.toLong() < now.epochSecond) {
                throw InvalidJWTClaimException("Exp is expired")
            }
        }

        private fun verifyNonce(jwtClaimsSet: JWTClaimsSet, parseTokenParams: ParseTokenParams) {
            val nonce = jwtClaimsSet.getStringClaim(NONCE_CLAIM) ?: throw InvalidJWTClaimException("Nonce is missing")
            if (nonce != parseTokenParams.nonce) {
                throw InvalidJWTClaimException("Nonce is not equal to the nonce in init auth request")
            }
        }
    }
}
