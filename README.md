# Keycloak-idp-singpass

## Documentation

- [KeyCloak Basic Introduction](documents/KeyCloak-Basic-Introduction.md)
- [How to Start SEA-SC-Integration-Demo Locally](documents/How-To-Start-SEA-SC-Integration-Demo-Locally.md)
- [Integration Keycloak Plugin With SP](documents/Install-And-Config-Keycloak-Plugin.md)
- [Common Error](documents/Common-Error.md)

## Dependency Requirement

- Keycloak 10.0.1

## Feature

- Support auto register with singpass id_token info
- Support encrypted private key config
- Support `CP`

## [Install and Config](https://github.com/ThoughtWorksInc/SEA-SC-OpenID/blob/keycloak/documents/Install-And-Config-Keycloak-Plugin.md#install-keycloak-idp-singpass-plugin-for-keycloak)

## Config auto redirect

Keycloak support automatically redirect to a [default identity provider](https://www.keycloak.org/docs/latest/server_admin/#default_identity_provider) instead of displaying the login form.

## Singpass Note

JWE header include:

```json
{"alg":"RSA-OAEP","enc":"A128CBC-HS256","kid":"bza0dXf6Fjla1FQrVKmATuZb9-4M90LxDuf3ujLYbqg"}
```

- [Key Encryption Algorithm (alg)](https://tools.ietf.org/html/rfc7518#section-4.1) used to encrypt the content encryption key (CEK)
- [Content Encryption Algorithm (enc)](https://tools.ietf.org/html/rfc7518#section-5.1) used in conjunction with the CEK to encrypt the content.

## Roadmap

1. Document
    1. Keycloak basic introduction
    2. Demo
    3. Keycloak installation
    4. Keycloak integration
        Keycloak setup
        SP setup
        keycloak SDK setup
        Check if it works
    5. Common mistakes
    6. Supporter list
2. E2E


## Quote

- [JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516)
- [How to decode JWE](https://stackoverflow.com/a/42503200)