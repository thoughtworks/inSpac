package com.thoughtworks.inspac.integration.sample.service

import com.thoughtworks.inspac.integration.sample.model.property.OIDCProperty
import com.thoughtworks.inspac.sdk.model.dto.InitAuthRequest
import com.thoughtworks.inspac.sdk.utility.URLUtils
import javax.servlet.ServletContext
import org.springframework.stereotype.Service

@Service
class URLService(
    private val oidcProperty: OIDCProperty,
    private val servletContext: ServletContext
) {
    fun generateOIDCLoginURL(): String {
        val initAuthRequest = InitAuthRequest(
            oidcProperty.host,
            oidcProperty.login.endPoint,
            oidcProperty.clientId,
            oidcProperty.redirectURI
        )
        // OIDC: Step 2: generate an initiated authentication URL to redirect to Singpass
        /*
        this example for saving nonce and state to verify token with ServletContext
        or you can choose other save methods
        */
        val nonce = initAuthRequest.nonce
        val state = initAuthRequest.state
        servletContext.setAttribute(state, nonce)
        return URLUtils.generateInitAuthURL(
            initAuthRequest
        )
    }
}
