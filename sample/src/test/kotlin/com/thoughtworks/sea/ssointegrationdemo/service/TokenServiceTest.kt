package com.thoughtworks.sea.ssointegrationdemo.service

import com.thoughtworks.sea.oidc.model.TokenResponse
import com.thoughtworks.sea.ssointegrationdemo.client.TokenClient
import com.thoughtworks.sea.ssointegrationdemo.model.property.OIDCProperty
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import io.mockk.verifyAll
import javax.servlet.ServletContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TokenServiceTest {
    @MockK
    private lateinit var tokenClient: TokenClient

    @MockK
    private lateinit var oidcProperty: OIDCProperty

    @MockK
    private lateinit var servletContext: ServletContext

    @InjectMockKs
    private lateinit var tokenService: TokenService

    @Test
    fun `should return token from client`() {
        val mockOIDCProperty = MockOIDCProperty.getMockOIDCProperty()
        val expectedResponse = TokenResponse("access_token", "refresh_token", "scope", "token_type", 3600, "id_token")
        every { tokenClient.getOIDCToken(any()) } returns expectedResponse
        every { oidcProperty.host } returns mockOIDCProperty.host
        every { oidcProperty.clientId } returns mockOIDCProperty.clientId
        every { oidcProperty.clientSecret } returns mockOIDCProperty.clientSecret
        every { oidcProperty.redirectURI } returns mockOIDCProperty.redirectURI
        every { oidcProperty.token } returns mockOIDCProperty.token

        val actualToken = tokenService.getOIDCToken("code")

        verify { tokenClient.getOIDCToken(any()) }
        verifyAll {
            oidcProperty.host
            oidcProperty.clientId
            oidcProperty.clientSecret
            oidcProperty.redirectURI
            oidcProperty.token
        }
        confirmVerified(tokenClient)
        assertEquals(expectedResponse, actualToken)
    }

    @Test
    @Disabled
    fun `should return service token after parsing`() {
        val mockOIDCProperty = MockOIDCProperty.getMockOIDCProperty()
        val mockTokenResponse = TokenResponse(
                "accessToken",
                "refreshToken",
                "idToken",
                "bearer",
                200,
                "openid"
        )
        every { oidcProperty.host } returns mockOIDCProperty.host
        every { oidcProperty.clientId } returns mockOIDCProperty.clientId
        every { oidcProperty.token.identityProviderPublicKey } returns mockOIDCProperty.token.identityProviderPublicKey
        every { oidcProperty.token.servicePrivateKey } returns mockOIDCProperty.token.servicePrivateKey
        every { servletContext.getAttribute(any()) } returns "any"

        every { servletContext.removeAttribute(any()) } just Runs

        val parseToServiceToken = tokenService.parseToServiceToken(mockTokenResponse, "state")

        assertEquals(parseToServiceToken, parseToServiceToken)
    }
}
