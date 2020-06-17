package org.thoughtworks.provider.singpass;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import java.io.IOException;
import java.security.PrivateKey;
import java.text.ParseException;
import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.OIDCIdentityProvider;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.JsonWebToken;
import org.thoughtworks.provider.singpass.utils.PrivateKeyUtils;

/** @author yuexie.zhou */
public class SingpassIdentityProvider extends OIDCIdentityProvider {

  private static final Logger log = Logger.getLogger(SingpassIdentityProvider.class);

  public SingpassIdentityProvider(KeycloakSession session, SingpassIdentityProviderConfig config) {
    super(session, config);

    System.out.println("SingpassIdentityProvider: ");
    System.out.println(config.getPrivateKey());
    System.out.println(getConfig().toString());
  }

  @Override
  protected JsonWebToken validateToken(String encodedIdToken, boolean ignoreAudience) {
    if (encodedIdToken == null) {
      throw new IdentityBrokerException("No token from server.");
    }

    JsonWebToken token = new JsonWebToken();
    try {
      // convert string to object
      //      ObjectMapper mapper = new ObjectMapper();
      //      mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
      //      SingpassJWEResponse jweResponse = mapper.readValue(encodedIdToken,
      // SingpassJWEResponse.class);

      // https://connect2id.com/products/nimbus-jose-jwt/examples/jwe-with-preset-cek
      logger.info(encodedIdToken);
      JWEObject jweObject = JWEObject.parse(encodedIdToken);
      PrivateKey privateKey = PrivateKeyUtils.getPrivateKey(getSingpassConfig().getPrivateKey());
      jweObject.decrypt(new RSADecrypter(privateKey));

      System.out.println("JWE payload");
      System.out.println(jweObject.getPayload().toString());

      JWSInput jws = new JWSInput(jweObject.getPayload().toString());
      if (!verify(jws)) {
        throw new IdentityBrokerException("token signature validation failed");
      }
    } catch (JWSInputException e) {
      throw new IdentityBrokerException("Invalid token", e);
    } catch (ParseException e) {
      throw new IdentityBrokerException("JWE parse failed", e);
    } catch (JOSEException e) {
      throw new IdentityBrokerException("JWE decrypt failed", e);
    } catch (IOException e) {
      throw new IdentityBrokerException("Read private key failed", e);
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

    System.out.println(token.toString());
    return token;
  }

  private SingpassIdentityProviderConfig getSingpassConfig() {
    return (SingpassIdentityProviderConfig) super.getConfig();
  }
}
