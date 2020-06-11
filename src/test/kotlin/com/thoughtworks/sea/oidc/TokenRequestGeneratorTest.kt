package com.thoughtworks.sea.oidc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenRequestGeneratorTest {
    @Test
    fun shouldReturnARequestEntity() {
        val requestGenerator = TokenRequestGenerator()
        val requestParams =
            TokenRequestParams("https://idptest.com", "/token", "code", "clientID", "clientSecret", "https://sptest.com")
        val actualRequestEntity = requestGenerator.generate(requestParams)

        val expectRequestEntity = TokenRequestEntity(
            "https://idptest.com/token",
            TokenRequestHeader(),
            TokenRequestBody("code", "clientID", "clientSecret", "https://sptest.com")
        )

        assertEquals(expectRequestEntity, actualRequestEntity)
    }
}
