package com.thoughtworks.inspac.sdk.utility

import com.thoughtworks.inspac.sdk.model.dto.InitAuthRequest

/** A utility class for handling and processing URL. */
object URLUtils {
    /**
     * Generate a initiated authentication URL
     * @param initAuthRequest a InitAuthRequest DTO model
     * @return Initiate authentication URL
     */
    @JvmStatic
    fun generateInitAuthURL(initAuthRequest: InitAuthRequest): String =
        initAuthRequest.host +
                "${initAuthRequest.endPoint}?" +
                "scope=${initAuthRequest.scope}&" +
                "response_type=${initAuthRequest.responseType}&" +
                "client_id=${initAuthRequest.clientId}&" +
                "nonce=${initAuthRequest.nonce}&" +
                "state=${initAuthRequest.state}&" +
                "redirect_uri=${initAuthRequest.redirectURI}"
}
