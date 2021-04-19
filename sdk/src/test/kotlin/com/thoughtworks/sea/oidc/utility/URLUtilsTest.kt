package com.thoughtworks.sea.oidc.utility

import com.thoughtworks.sea.oidc.model.dto.InitAuthRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class URLUtilsTest {

    @Test
    fun `should generate SingPass Login URL when calling generateLoginAddress method`() {

        val initAuthRequest = InitAuthRequest(
            "http://localhost",
            "/auth2.0",
            "SDK-TEST-001",
            "https://google.com",
            nonce = "UUID-NONCE",
            state = "UUID-STATE"
        )

        val actualURL =
            URLUtils.generateInitAuthURL(initAuthRequest)
        val expectedURL =
            "http://localhost/auth2.0?scope=openid&response_type=code&client_id=SDK-TEST-001&nonce=UUID-NONCE&state=UUID-STATE&redirect_uri=https://google.com"

        assertEquals(actualURL, expectedURL)
    }
}
