package com.thoughtworks.inspac.integration.sample.service

import com.thoughtworks.inspac.integration.sample.model.property.OIDCProperty
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import javax.servlet.ServletContext
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class URLServiceTest {

    @MockK
    private lateinit var oidcProperty: OIDCProperty

    @MockK
    private lateinit var servletContext: ServletContext

    @InjectMockKs
    private lateinit var urlService: URLService

    @Test
    internal fun `should generate url successfully`() {
        val mockOIDCProperty = MockOIDCProperty.getMockOIDCProperty()
        every { oidcProperty.host } returns mockOIDCProperty.host
        every { oidcProperty.clientId } returns mockOIDCProperty.clientId
        every { oidcProperty.redirectURI } returns mockOIDCProperty.redirectURI
        every { oidcProperty.login } returns mockOIDCProperty.login

        justRun { servletContext.setAttribute(any(), any()) }

        val generateOIDCLoginURL = urlService.generateOIDCLoginURL()

        Assertions.assertTrue(generateOIDCLoginURL.contains("localhost/auth2.0?scope=openid&response_type=code&client_id=SDK-TEST-001"))
    }
}
