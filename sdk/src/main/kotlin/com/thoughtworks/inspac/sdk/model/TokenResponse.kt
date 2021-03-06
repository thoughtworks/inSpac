package com.thoughtworks.inspac.sdk.model

import com.fasterxml.jackson.annotation.JsonProperty

/** Those are token response from IDP. */
data class TokenResponse(
    /**
     *  The access token resembles the concept of a physical token or ticket.
     *  It gives the holder access to a specific HTTP resource or web service,
     *  which is typically limited by scope and has an expiration time.
     */
    @JsonProperty("access_token")
    val accessToken: String,
    /** carry the information necessary to get a new access token. */
    @JsonProperty("refresh_token")
    val refreshToken: String,
    /**
     *  The ID Token is a security token that contains Claims about an End-User.
     *  The ID Token is represented as a JSON Web Encryption (JWE).
     */
    @JsonProperty("id_token")
    val idToken: String,
    /**
     *  Used to specify the scope of the requested authorisation in OAuth.
     *  The scope value "openid" signals a request for OpenID authentication and ID token.
     */
    val scope: String,
    /** expire time for access token  */
    @JsonProperty("expires_in")
    val expiresIn: Int,
    /** The value MUST be Bearer or another token_type value that the Client has negotiated with the Authorization Server. */
    @JsonProperty("token_type")
    val tokenType: String
)
