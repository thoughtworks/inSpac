package com.thoughtworks.sea.oidc.model.dto

import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

/** Those are params for getting token by using restTemplate. */
data class TokenRequest(
    /** Getting token url. */
    val url: String,
    /** HttpEntity contains request body and header. */
    val httpEntity: HttpEntity<LinkedMultiValueMap<String, String>>
)
