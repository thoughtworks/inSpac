# Keycloak-idp-singpass

## Dependency Requirement

- Keycloak 10.0.1

## Install

1. Copy `keycloak-idp-singpass-*.jar` to `$KEYCLOAK_HOME/standalone/deployments/`

## Singpass Note

JWE header include:

```json
{"alg":"RSA-OAEP","enc":"A128CBC-HS256","kid":"bza0dXf6Fjla1FQrVKmATuZb9-4M90LxDuf3ujLYbqg"}
```

- [Key Encryption Algorithm (alg)](https://tools.ietf.org/html/rfc7518#section-4.1) used to encrypt the content encryption key (CEK)
- [Content Encryption Algorithm (enc)](https://tools.ietf.org/html/rfc7518#section-5.1) used in conjunction with the CEK to encrypt the content.


## Quote

- [JSON Web Encryption (JWE)](https://tools.ietf.org/html/rfc7516)
- [How to decode JWE](https://stackoverflow.com/a/42503200)