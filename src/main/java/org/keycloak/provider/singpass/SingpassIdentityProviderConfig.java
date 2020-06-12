package org.keycloak.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);
  }

  public SingpassIdentityProviderConfig() {}

  public void setPrivateKeySignatureVerifier(String privateKey) {
    getConfig().put("privateKeySignatureVerifier", privateKey);
  }

  public String getPrivateKeySignatureVerifier() {
    return getConfig().get("privateKeySignatureVerifier");
  }
}
