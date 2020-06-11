package org.thoughtworks.sea.keycloak.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProvider;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.JsonWebToken;

/** @author yuexie.zhou */
public class SingpassOIDCIdentityProvider extends OIDCIdentityProvider {

  public SingpassOIDCIdentityProvider(
      KeycloakSession session, SingpassOIDCIdentityProviderConfig config) {
    super(session, config);
  }

  @Override
  protected JsonWebToken validateToken(String encodedIdToken, boolean ignoreAudience) {
    if (encodedIdToken == null) {
      throw new IdentityBrokerException("No token from server.");
    }

    JsonWebToken token = new JsonWebToken();

    return token;
  }
}
