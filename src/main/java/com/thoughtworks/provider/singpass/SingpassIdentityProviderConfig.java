package com.thoughtworks.provider.singpass;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/** @author yuexie.zhou */
public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);
  }

  public SingpassIdentityProviderConfig() {
    this.setValidateSignature(true);
    this.setUseJwksUrl(false);
  }

  public void setPrivateKey(String privateKey) {
    getConfig().put("privateKey", privateKey);
  }

  public String getPrivateKey() {
    return getConfig().get("privateKey");
  }
}
