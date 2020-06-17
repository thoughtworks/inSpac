package com.thoughtworks.sea.oidc.model.dto

import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap

data class TokenRequest(
    val url: String,
    val httpEntity: HttpEntity<LinkedMultiValueMap<String, String>>
)
