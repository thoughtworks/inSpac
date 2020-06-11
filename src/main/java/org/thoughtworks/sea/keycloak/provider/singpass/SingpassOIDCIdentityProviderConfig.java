package org.thoughtworks.sea.keycloak.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

public class SingpassOIDCIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassOIDCIdentityProviderConfig(IdentityProviderModel identityProviderModel) {
    super(identityProviderModel);
  }

  public SingpassOIDCIdentityProviderConfig() {
    super();
  }
}
