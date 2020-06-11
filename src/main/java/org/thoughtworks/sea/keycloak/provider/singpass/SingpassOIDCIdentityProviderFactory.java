package org.thoughtworks.sea.keycloak.provider.singpass;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.protocol.oidc.representations.OIDCConfigurationRepresentation;
import org.keycloak.util.JsonSerialization;

/** @author yuexie.zhou */
public class SingpassOIDCIdentityProviderFactory
    extends AbstractIdentityProviderFactory<SingpassOIDCIdentityProvider> {

  public static final String PROVIDER_ID = "singpass";

  @Override
  public String getName() {
    return "Singpass";
  }

  @Override
  public SingpassOIDCIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
    return new SingpassOIDCIdentityProvider(session, new SingpassOIDCIdentityProviderConfig(model));
  }

  @Override
  public SingpassOIDCIdentityProviderConfig createConfig() {
    return new SingpassOIDCIdentityProviderConfig();
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public Map<String, String> parseConfig(KeycloakSession session, InputStream inputStream) {
    return parseOIDCConfig(session, inputStream);
  }

  protected static Map<String, String> parseOIDCConfig(
      KeycloakSession session, InputStream inputStream) {
    OIDCConfigurationRepresentation rep;
    try {
      rep = JsonSerialization.readValue(inputStream, OIDCConfigurationRepresentation.class);
    } catch (IOException e) {
      throw new RuntimeException("failed to load openid connect metadata", e);
    }
    SingpassOIDCIdentityProviderConfig config = new SingpassOIDCIdentityProviderConfig();
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
