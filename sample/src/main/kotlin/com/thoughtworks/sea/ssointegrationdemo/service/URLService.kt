package com.thoughtworks.sea.ssointegrationdemo.service

import com.thoughtworks.sea.oidc.model.dto.InitAuthRequest
import com.thoughtworks.sea.oidc.utility.URLUtils
import com.thoughtworks.sea.ssointegrationdemo.model.property.OIDCProperty
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
        // OIDC: Step 2: generate an initiated authentication URL to redirect to SingPass/CorpPass
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
