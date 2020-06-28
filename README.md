# Keycloak-idp-singpass

## Dependency Requirement

- Keycloak 10.0.1

## Feature

- Support auto register with singpass id_token info
- Support encrypted private key config
- Support `CP`

## Install

Copy `keycloak-idp-singpass-*.jar` to `$KEYCLOAK_HOME/standalone/deployments/`

## Config Singpass IDP

Generated PrivateKey and PublicKey in [Online RSA Key Generator](https://travistidwell.com/jsencrypt/demo/), *generate config **Key Size** must be greater than `2048bit`*

- Authorization URL: place singpass authentication request url like `https://host/authorize`
- Token URL: place singpass exchange token url like `https://host/token`
- Client ID: your client id registered to identity provider
- Client Secret: A shared secret code mutually agreed between SingPass OP and your RP, provided by singpass.
- Validate Signatures: turn on
- Use JWKS URL: turn off
- Validating Public Key: Singpass public key(SPK)

singpass public key exchanged in two ways:

    1. X509 certificate – this can be obtained via email by making a request to SingPass
    2. JWKS URL – this is a set of keys containing signing public keys. This can be obtained via JWKS URL(https://stg-saml-internet.singpass.gov.sg/mga/sps/oauth/oauth20/jwks/SingPassOP). The signing key which should be used for verification will be indicated by KID in the header of JWS message

8. Encrypted Private Key: place your generated private key(encrypted)

Open Encrypted Online: [AES Encryption and Decryption Online Tool](https://www.devglan.com/online-tools/aes-encryption-decryption)

    1. Input your private key or select private key file
    2. MODE: `CBC`
    3. Key Size: `192`
    4. IV: `tIo0D7UpXjpxsVvK`
    5. Secret key: your `client secret`, the length of the `cilent secret` must be 24. If it is less than 24, it needs to be filled in with the string 1 on the right, and if it is greater than 24, the first 24 characters are used
    6. Output Text Format: `Base64`
    7. Copy the output, place in `Encrypted Private Key`

![](/images/encryption_the_example.png)

> Or use `AESUtils.encrypt` in `com.thoughtworks.provider.singpass.utils` (client secret does not need to be processed in advance)

## Config auto redirect

Keycloak support custom Authentication flow:

1. 

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