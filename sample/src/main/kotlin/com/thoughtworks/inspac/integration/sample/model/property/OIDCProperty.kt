package com.thoughtworks.inspac.integration.sample.model.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/*
* if you use the @ConfigurationProperties annotation, you should add @EnableConfigurationProperties
* into your application class, like `InspacIntegrationSampleApplication.kt`
* */
@ConstructorBinding
@ConfigurationProperties(prefix = "oidc")
/** In this Demo OIDCProperty just suit a IDP.
 * if you need to support multiple IDPs, you can create different implements of OIDCProperty. */
data class OIDCProperty(
    val host: String,
    val clientId: String,
    val clientSecret: String,
    val redirectURI: String,
    val login: Login,
    val token: Token
) {
    data class Login(
        val endPoint: String
    )

    data class Token(
        val endPoint: String,
        val identityProviderPublicKey: String,
        val servicePrivateKey: String
    )
}
