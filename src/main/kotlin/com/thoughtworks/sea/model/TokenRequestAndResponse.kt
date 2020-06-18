package com.thoughtworks.sea.model

import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

/** Those are params for getting token by using restTemplate. */
data class TokenRequest(
    /** Getting token url. */
    val url: String,
    /** HttpEntity contains request body and header. */
    val httpEntity: HttpEntity<LinkedMultiValueMap<String, String>>
)

/** Those are token response from IDP. */
data class TokenResponse(
    /**
     *  The access token resembles the concept of a physical token or ticket.
     *  It gives the holder access to a specific HTTP resource or web service,
     *  which is typically limited by scope and has an expiration time.
     */
    val accessToken: String,
    /** carry the information necessary to get a new access token. */
    val refreshToken: String,
    /**
     *  The ID Token is a security token that contains Claims about an End-User.
     *  The ID Token is represented as a JSON Web Token (JWT).
     */
    val idToken: String,
    /**
     *  Used to specify the scope of the requested authorisation in OAuth.
     *  The scope value "openid" signals a request for OpenID authentication and ID token.
     */
    val scope: String,
    /** expire time for access token  */
    val expiresIn: Int,
    /** The value MUST be Bearer or another token_type value that the Client has negotiated with the Authorization Server. */
    val tokenType: String
)
