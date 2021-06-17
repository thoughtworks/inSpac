package com.thoughtworks.inspac.integration.sample.service

import com.thoughtworks.inspac.integration.sample.model.property.OIDCProperty

class MockOIDCProperty {
    companion object {
        @JvmStatic
        fun getMockOIDCProperty(): OIDCProperty {
            val login = OIDCProperty.Login("/auth2.0")
            val token = OIDCProperty.Token("/token", "", "")
            return OIDCProperty("localhost", "SDK-TEST-001", "client_secret", "https://google.com", login, token)
        }
    }
}
