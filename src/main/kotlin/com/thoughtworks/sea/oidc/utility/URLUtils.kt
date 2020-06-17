package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.dto.InitAuthRequest

class URLUtils {
    companion object {
        @JvmStatic
        fun generateInitAuthURL(initAuthRequest: InitAuthRequest): String =
            "https://${initAuthRequest.host}" +
                    "${initAuthRequest.endPoint}?" +
                    "scope=${initAuthRequest.scope}&" +
                    "response_type=${initAuthRequest.responseType}&" +
                    "client_id=${initAuthRequest.clientId}&" +
                    "nonce=${initAuthRequest.nonce}&" +
                    "state=${initAuthRequest.state}&" +
                    "redirect_uri=${initAuthRequest.redirectURI}"
    }
}
