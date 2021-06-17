package com.thoughtworks.inspac.integration.sample.service

import com.thoughtworks.inspac.integration.sample.client.TokenClient
import com.thoughtworks.inspac.integration.sample.model.property.OIDCProperty
import com.thoughtworks.inspac.sdk.model.ParseTokenParams
import com.thoughtworks.inspac.sdk.model.TokenRequestParams
import com.thoughtworks.inspac.sdk.model.TokenResponse
import com.thoughtworks.inspac.sdk.utility.TokenUtils
import javax.servlet.ServletContext
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenClient: TokenClient,
    private val oidcProperty: OIDCProperty,
    private val servletContext: ServletContext
) {
    fun getServiceToken(code: String, state: String): Map<String, String> {
        /*
        this example for verifying state,
        continue to get OIDC token if state same as the sate in the init auth request
        return invalid message if state not save in servletContext
        */
        if (servletContext.attributeNames.toList().contains(state)) {
            val oidcToken = getOIDCToken(code)
            return oidcToken?.let { parseToServiceToken(it, state) } ?: emptyMap()
        }
        return mapOf("message" to "invalid state")
    }

    internal fun getOIDCToken(code: String): TokenResponse? {
        val tokenRequestParams = TokenRequestParams(
                oidcProperty.host,
                oidcProperty.token.endPoint,
                oidcProperty.clientId,
                oidcProperty.clientSecret,
                oidcProperty.redirectURI,
                code
        )

        return tokenClient.getOIDCToken(tokenRequestParams)
    }

    internal fun parseToServiceToken(token: TokenResponse, state: String): Map<String, String> {
        val parseTokenParams = ParseTokenParams(
                // this example for extract nonce from servletContext by state for the OIDC token verification
                servletContext.getAttribute(state) as String,
                oidcProperty.host,
                oidcProperty.clientId,
                oidcProperty.token.identityProviderPublicKey,
                oidcProperty.token.servicePrivateKey
        )
        servletContext.removeAttribute(state)
        // OIDC: Step 4: When Service wants to parse response with Id-Token
        // to get SubjectInfo or additional information
        val (nric, uuid) = TokenUtils.parseTokenToSubjectInfo(token, parseTokenParams)
        /*
        this example for connecting user from IdP with service's user record
        maybe you need to implement your own token issuer
        */
        return mapOf(
            "nric" to (nric ?: ""),
            "uuid" to uuid
        )
    }
}
