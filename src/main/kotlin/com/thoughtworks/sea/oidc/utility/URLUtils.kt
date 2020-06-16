package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.dto.SPCPLoginRequest

class URLUtils {
    fun generateLoginURL(spcpLoginRequest: SPCPLoginRequest): String =
        "https://${spcpLoginRequest.host}" +
                "${spcpLoginRequest.endPoint}?" +
                "scope=${spcpLoginRequest.scope}&" +
                "response_type=${spcpLoginRequest.responseType}&" +
                "client_id=${spcpLoginRequest.clientId}&" +
                "nonce=${spcpLoginRequest.nonce}&" +
                "state=${spcpLoginRequest.state}&" +
                "redirect_uri=${spcpLoginRequest.redirectURI}"
}
