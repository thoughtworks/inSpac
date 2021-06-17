# inSpac Keycloak Plugin
[![Keycloak plugin test](https://github.com/thoughtworks/inSpac/actions/workflows/keycloak-plugin-test.yaml/badge.svg)](https://github.com/thoughtworks/inSpac/actions/workflows/keycloak-plugin-test.yaml)

## About
A Keycloak plugin for faster integration with SingPass platform on `OpenID-Connect 1.0` auth scheme.

The plugin runs on Keycloak platform as the Service Provider.

### Feature

- Support auto register with singpass id_token info
- Support encrypted private key config

## Documentation
- [KeyCloak Basic Introduction](documents/KeyCloak-Basic-Introduction.md)
- [How to start inSpac-integration-sample Locally](documents/How-to-start-inSpac-integration-sample-locally.md)
- [Integration Keycloak Plugin With SP](documents/Install-And-Config-Keycloak-Plugin.md)
- [Common Error](documents/Common-Error.md)

## Project Setup

### Install & Config
Please check the [tutorial](documents/Install-And-Config-Keycloak-Plugin.md#install-keycloak-idp-singpass-plugin-for-keycloak)

### Auto redirect configuration
Keycloak support automatically redirect to a [default identity provider](https://www.keycloak.org/docs/latest/server_admin/#default_identity_provider) instead of displaying the login form.

### SingPass Note

JWE header include:

```json
{"alg":"RSA-OAEP","enc":"A128CBC-HS256","kid":"bza0dXf6Fjla1FQrVKmATuZb9-4M90LxDuf3ujLYbqg"}
```

- [Key Encryption Algorithm (alg)](https://tools.ietf.org/html/rfc7518#section-4.1) used to encrypt the content encryption key (CEK)
- [Content Encryption Algorithm (enc)](https://tools.ietf.org/html/rfc7518#section-5.1) used in conjunction with the CEK to encrypt the content.

#### Reference
- [JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516)
- [How to decode JWE](https://stackoverflow.com/a/42503200)

## Dependencies & Plugins
The project relies on dependencies and plugins below.

Dependency | Scope
---- | ---
org.keycloak:keycloak-core:10.0.1 | provided
org.keycloak:keycloak-server-spi-private:10.0.1 |  provided
org.keycloak:keycloak-server-spi:10.0.1 | provided
org.keycloak:keycloak-services:10.0.1 | provided
com.nimbusds:nimbus-jose-jwt:8.3 | compile
org.bouncycastle:bcpkix-jdk15on:1.65 | compile
org.junit.jupiter:junit-jupiter:RELEASE | test
org.mockito:mockito-core:2.23.4 | test
junit:junit:4.12 | test


Plugin |
:---- |
org.apache.maven.plugins:maven-assembly-plugin:2.5.5 |
org.apache.maven.plugins:maven-surefire-plugin:2.22.2 |
