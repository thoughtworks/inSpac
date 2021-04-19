package com.thoughtworks.provider.singpass;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.thoughtworks.provider.singpass.utils.AESUtils;
import com.thoughtworks.provider.singpass.utils.PrivateKeyUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.text.ParseException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Objects;
import org.keycloak.broker.oidc.OIDCIdentityProvider;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.JsonWebToken;

/** @author yuexie.zhou */
public class SingpassIdentityProvider extends OIDCIdentityProvider {

  public static final String EMAIL_HOST = "@placeholder.com";

  public SingpassIdentityProvider(KeycloakSession session, SingpassIdentityProviderConfig config) {
    super(session, config);

    config.setPublicKeySignatureVerifier(extractPublicKeyFromCertificate());
  }

  @Override
  protected JsonWebToken validateToken(String encodedIdToken, boolean ignoreAudience) {
    if (encodedIdToken == null) {
      throw new IdentityBrokerException("No token from server.");
    }

    JsonWebToken token;
    try {
      JWEObject jweObject = JWEObject.parse(encodedIdToken);
      PrivateKey privateKey = PrivateKeyUtils.parsePrivateKey(getPrivateKey());
      jweObject.decrypt(new RSADecrypter(privateKey));

      JWSInput jws = new JWSInput(jweObject.getPayload().toString());
      if (!verify(jws)) {
        throw new IdentityBrokerException("token signature validation failed");
      }
      token = jws.readJsonContent(JsonWebToken.class);
    } catch (JWSInputException e) {
      throw new IdentityBrokerException("Invalid token", e);
    } catch (ParseException e) {
      throw new IdentityBrokerException("JWE parse failed", e);
    } catch (JOSEException e) {
      throw new IdentityBrokerException("JWE decrypt failed", e);
    } catch (IOException e) {
      throw new IdentityBrokerException("Read private key failed", e);
    } catch (Exception e) {
      throw new IdentityBrokerException("decrypt private key failed", e);
    }

    String iss = token.getIssuer();

    if (!token.isActive(getConfig().getAllowedClockSkew())) {
      throw new IdentityBrokerException("Token is no longer valid");
    }

    if (!ignoreAudience && !token.hasAudience(getConfig().getClientId())) {
      throw new IdentityBrokerException("Wrong audience from token.");
    }

    String trustedIssuers = getConfig().getIssuer();

    if (trustedIssuers != null) {
      String[] issuers = trustedIssuers.split(",");

      for (String trustedIssuer : issuers) {
        if (iss != null && iss.equals(trustedIssuer.trim())) {
          return token;
        }
      }

      throw new IdentityBrokerException(
          "Wrong issuer from token. Got: " + iss + " expected: " + getConfig().getIssuer());
    }

    return token;
  }

  @Override
  protected BrokeredIdentityContext extractIdentity(
      AccessTokenResponse tokenResponse, String accessToken, JsonWebToken idToken) {
    String id = idToken.getSubject();
    BrokeredIdentityContext identity = new BrokeredIdentityContext(id);

    String userName = id;
    String firstName = "";
    String lastName = "";
    String email = "";

    SingpassIdentityProviderConfig singpassConfig = getSingpassConfig();
    Entry supportFieldForCP = singpassConfig.getSupportFieldForCP();
    boolean isSupportCP = Objects.nonNull(supportFieldForCP);

    if (isSupportCP) {
      String firstField = supportFieldForCP.getKey().toString();
      String secondField = supportFieldForCP.getValue().toString();
      LinkedHashMap<String, String> inIdTokenCustomField =
          (LinkedHashMap<String, String>) idToken.getOtherClaims().get(firstField);

      if (Objects.isNull(inIdTokenCustomField)) {
        throw new IdentityBrokerException("Support Field For CP: can't get value form id token");
      }

      String subCustomField = inIdTokenCustomField.get(secondField);

      if (Objects.isNull(subCustomField)) {
        throw new IdentityBrokerException("Support Field For CP: can't get value form id token");
      }

      userName = subCustomField;

      if (singpassConfig.isAutoRegister()) {
        firstName = singpassConfig.getFirstNameBySub(id);
        lastName = singpassConfig.getLastNameBySub(id);
        email = userName + EMAIL_HOST;
      }
    } else if (singpassConfig.isAutoRegister()) {
      userName = singpassConfig.getUserNameBySub(id);
      firstName = singpassConfig.getFirstNameBySub(id);
      lastName = singpassConfig.getLastNameBySub(id);
      email = userName + EMAIL_HOST;
    }

    identity.getContextData().put(VALIDATED_ID_TOKEN, idToken);

    identity.setId(id);
    identity.setFirstName(firstName);
    identity.setLastName(lastName);
    identity.setEmail(email);
    identity.setBrokerUserId(getConfig().getAlias() + "." + id);
    identity.setUsername(userName);

    if (tokenResponse != null && tokenResponse.getSessionState() != null) {
      identity.setBrokerSessionId(getConfig().getAlias() + "." + tokenResponse.getSessionState());
    }
    if (tokenResponse != null)
      identity.getContextData().put(FEDERATED_ACCESS_TOKEN_RESPONSE, tokenResponse);
    if (tokenResponse != null) processAccessTokenResponse(identity, tokenResponse);

    return identity;
  }

  private SingpassIdentityProviderConfig getSingpassConfig() {
    return (SingpassIdentityProviderConfig) super.getConfig();
  }

  private String getPrivateKey() throws Exception {
    String encryptedPrivateKey = getSingpassConfig().getPrivateKey();

    return AESUtils.decrypt(encryptedPrivateKey, this.getConfig().getClientSecret());
  }

  private String extractPublicKeyFromCertificate() {
    try {
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

      Certificate certificate =
          certificateFactory.generateCertificate(
              new ByteArrayInputStream(
                  this.getSingpassConfig().getPublicKeySignatureVerifier().getBytes()));

      return Base64.getEncoder().encodeToString(certificate.getPublicKey().getEncoded());
    } catch (CertificateException e) {
      return this.getSingpassConfig().getPublicKeySignatureVerifier();
    }
  }
}
