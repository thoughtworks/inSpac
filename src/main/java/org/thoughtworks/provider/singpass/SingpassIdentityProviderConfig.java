package org.thoughtworks.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/** @author yuexie.zhou */
public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);

    System.out.println("SingpassIdentityProviderConfig: model");
  }

  public SingpassIdentityProviderConfig() {}

  public void setPrivateKey(String privateKey) {
    System.out.println("SingpassIdentityProviderConfig: set value");
    System.out.println(privateKey);

    getConfig().put("privateKey", privateKey);
  }

  public String getPrivateKey() {
    System.out.println("SingpassIdentityProviderConfig: get value");

    return getConfig().get("privateKey");
  }
}
