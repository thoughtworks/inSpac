package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.dto.CPLogoutParams
import com.thoughtworks.sea.oidc.model.dto.InitAuthRequest

/** A utility class for handling and processing URL. */
class URLUtils {
    companion object {
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

        /**
         * Generate a CP logout URL
         * @param cpLogoutParams parameter for building CP logout URL
         * @return CP logout URL
         */
        @JvmStatic
        fun generateCPLogoutURL(cpLogoutParams: CPLogoutParams): String =
            "${cpLogoutParams.host}/cplogout?return_uri=${cpLogoutParams.returnURI}"
    }
}
