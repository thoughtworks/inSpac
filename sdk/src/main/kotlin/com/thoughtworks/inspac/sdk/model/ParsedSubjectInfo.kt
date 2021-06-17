package com.thoughtworks.inspac.sdk.model

/**
 * @description The subject information from OIDC response
 * @param uuid: user's unique id
 */
data class ParsedSubjectInfo(val nricNumber: String?, val uuid: String)
