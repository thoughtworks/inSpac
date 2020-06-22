# Keycloak-idp-singpass

## Dependency Requirement

- Keycloak 10.0.1

## Install

Copy `keycloak-idp-singpass-*.jar` to `$KEYCLOAK_HOME/standalone/deployments/`

## Config

Generated PrivateKey and PublicKey in [Online RSA Key Generator](https://travistidwell.com/jsencrypt/demo/), *generate config **Key Size** must be greater than `2048bit`*

    * Singpass public key: SPK

1. Authorization URL: place singpass authentication request url like `https://host/authorize`

2. Token URL: place singpass exchange token url like `https://host/token`

3. Client ID: your client id registered to identity provider

4. Client Secret: A shared secret code mutually agreed between SingPass OP and your RP, provided by singpass.

5. Validate Signatures: turn on

6. Use JWKS URL: turn off

7. Validating Public Key: SPK

8. Validating Private Key: place your generated private key

singpass public key exchanged in two ways:

1. X509 certificate – this can be obtained via email by making a request to SingPass
2. JWKS URL – this is a set of keys containing signing public keys. This can be obtained via JWKS URL(https://stg-saml-internet.singpass.gov.sg/mga/sps/oauth/oauth20/jwks/SingPassOP). The signing key which should be used for verification will be indicated by KID in the header of JWS message

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