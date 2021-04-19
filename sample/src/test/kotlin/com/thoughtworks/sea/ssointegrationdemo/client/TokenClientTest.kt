package com.thoughtworks.sea.ssointegrationdemo.client

import com.thoughtworks.sea.oidc.model.TokenRequest
import com.thoughtworks.sea.oidc.model.TokenRequestParams
import com.thoughtworks.sea.oidc.model.TokenResponse
import com.thoughtworks.sea.oidc.utility.TokenUtils
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.verify
import javax.swing.text.html.parser.Entity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
internal class TokenClientTest {
    @MockK
    private lateinit var restTemplate: RestTemplate

    private lateinit var tokenRequest: TokenRequest

    private lateinit var tokenRequestParams: TokenRequestParams

    @InjectMockKs
    private lateinit var tokenClient: TokenClient

    @BeforeAll
    fun setUp() {
        val url = "https://host.com"
        val bodyMap = LinkedMultiValueMap<String, String>()
        bodyMap.add("code", "code")
        bodyMap.add("client_id", "client_id")
        bodyMap.add("client_secret", "client_secret")
        bodyMap.add("redirect_uri", "redirect_url")
        bodyMap.add("grant_type", "authorization_code")
        val headerMap = LinkedMultiValueMap<String, String>()
        headerMap.add("content_type", "application/x-www-form-urlencoded")
        tokenRequest = TokenRequest(url, HttpEntity(
                bodyMap, headerMap
        ))

        tokenRequestParams = TokenRequestParams("host", "endpoint", "client_id", "client_secret", "redirect_url", "code")

        mockkObject(TokenUtils)
        every { TokenUtils.buildTokenRequest(tokenRequestParams) } returns tokenRequest
    }

    @Test
    fun `should get token from mock server`() {
        // given
        val expectedResponse = TokenResponse("access_token", "refresh_token", "scope", "token_type", 3600, "id_token")
        every {
            restTemplate.exchange(any<String>(), any(), any<HttpEntity<Entity>>(), any<Class<TokenResponse>>())
        } returns ResponseEntity.ok(expectedResponse)

        // when
        val actualToken = tokenClient.getOIDCToken(tokenRequestParams)

        // then
        verify { TokenUtils.buildTokenRequest(tokenRequestParams) }
        verify { restTemplate.exchange(any<String>(), any(), any<HttpEntity<Entity>>(), any<Class<TokenResponse>>()) }
        confirmVerified(TokenUtils)
        confirmVerified(restTemplate)
        assertEquals(expectedResponse, actualToken)
    }

    @Test
    fun `should throw exception when call mock server failed`() {
        every { restTemplate.exchange(any<String>(), any(), any<HttpEntity<Entity>>(), any<Class<TokenResponse>>()) } throws RestClientException("error")

        val result = assertThrows<RestClientException> { tokenClient.getOIDCToken(tokenRequestParams) }
        assertThat(result.message).startsWith("Request to S/C for getting token fails:")
        verify { TokenUtils.buildTokenRequest(tokenRequestParams) }
        verify { restTemplate.exchange(any<String>(), any(), any<HttpEntity<Entity>>(), any<Class<TokenResponse>>()) }
        confirmVerified(TokenUtils)
        confirmVerified(restTemplate)
    }
}
