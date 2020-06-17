package com.thoughtworks.sea.oidc.model.dto

import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

data class TokenRequest(val url: String, val httpEntity: HttpEntity<LinkedMultiValueMap<String, String>>)

data class TokenResponse(
    val access_token: String,
    val refresh_token: String,
    val scope: String,
    val token_type: String,
    val id_token: IDToken
) {
    data class IDToken(
        val recipients: List<Recipient>,
        val protected: String,
        val iv: String,
        val ciphertext: String,
        val tag: String
    ) {
        data class Recipient(
            val encrypted_key: String
        )
    }
}
