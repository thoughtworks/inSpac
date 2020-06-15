package org.keycloak.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/** @author yuexie.zhou */
public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);

    System.out.println("SingpassIdentityProviderConfig: model");
  }

  public void setPrivateKeySignatureVerifier(String privateKey) {
    System.out.println("SingpassIdentityProviderConfig: set value");
    System.out.println(privateKey);

    getConfig().put("privateKeySignatureVerifier", privateKey);
  }

  public String getPrivateKeySignatureVerifier() {
    System.out.println("SingpassIdentityProviderConfig: get value");

    return getConfig().get("privateKeySignatureVerifier");
  }
}
