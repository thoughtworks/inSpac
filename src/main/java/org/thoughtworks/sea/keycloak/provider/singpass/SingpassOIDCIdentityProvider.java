package org.thoughtworks.sea.keycloak.provider.singpass;

import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.provider.ExchangeExternalToken;
import org.keycloak.models.KeycloakSession;

/** @author yuexie.zhou */
public class SingpassOIDCIdentityProvider
    extends AbstractOAuth2IdentityProvider<OIDCIdentityProviderConfig>
    implements ExchangeExternalToken {

  public static final String DEFAULT_SCOPE = "openid";

  public SingpassOIDCIdentityProvider(KeycloakSession session, OIDCIdentityProviderConfig config) {
    super(session, config);
  }

  @Override
  protected String getDefaultScopes() {
    return DEFAULT_SCOPE;
  }
}
