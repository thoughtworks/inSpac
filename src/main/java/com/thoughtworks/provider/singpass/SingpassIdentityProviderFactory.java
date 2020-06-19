package com.thoughtworks.provider.singpass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;
import org.keycloak.util.JsonSerialization;

/** @author yuexie.zhou */
public class SingpassIdentityProviderFactory
    extends AbstractIdentityProviderFactory<SingpassIdentityProvider> {

  public static final String PROVIDER_ID = "singpass";

  @Override
  public String getName() {
    return "Singpass";
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public SingpassIdentityProviderConfig createConfig() {
    return new SingpassIdentityProviderConfig();
  }

  @Override
  public SingpassIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
    return new SingpassIdentityProvider(session, new SingpassIdentityProviderConfig(model));
  }

  @Override
  public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
    OIDCConfigurationRepresentation rep;
    try {
      rep = JsonSerialization.readValue(inputStream, OIDCConfigurationRepresentation.class);
    } catch (IOException e) {
      throw new RuntimeException("failed to load openid connect metadata", e);
    }
    SingpassIdentityProviderConfig config = new SingpassIdentityProviderConfig(new IdentityProviderModel());
    config.setIssuer(rep.getIssuer());
    config.setLogoutUrl(rep.getLogoutEndpoint());
    config.setAuthorizationUrl(rep.getAuthorizationEndpoint());
    config.setTokenUrl(rep.getTokenEndpoint());
    config.setUserInfoUrl(rep.getUserinfoEndpoint());
    if (rep.getJwksUri() != null) {
      config.setValidateSignature(true);
      config.setUseJwksUrl(true);
      config.setJwksUrl(rep.getJwksUri());
    }
    return config.getConfig();
  }
}
