package com.thoughtworks.sea.oidc.model.dto

/** Params for generating CP logout URL */
data class CPLogoutParams(
    /** CP host address without "https://" prefix. */
    val host: String,
    /** redirect URI after logout successful */
    val returnURI: String
)
