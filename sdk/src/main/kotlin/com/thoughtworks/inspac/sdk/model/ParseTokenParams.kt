package com.thoughtworks.inspac.sdk.model

/** Those are used to decrypt, verify signature, verify payload */
data class ParseTokenParams(
    /** The parameter of initiate authentication request
     * you should save it when initiate authentication request. */
    val nonce: String,
    /** IDP host address with protocol prefix. */
    val host: String,
    /** The client identifier assigned to the Relying Party during its registration. */
    val clientId: String,
    /** A public key used by the relying party to verify the signed JWT (JWS) messages received from IDP. */
    val idpPublicKey: String,
    /** A private key used by the relying party to decrypt the encrypted JWT (JWE) messages received from IDP. */
    val servicePrivateKey: String
)
