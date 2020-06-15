package org.keycloak.provider.singpass.mapper;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.provider.singpass.SingpassIdentityProviderFactory;

/** @author yuexie.zhou */
public class SingpassUserAttributeMapper extends AbstractJsonUserAttributeMapper {

  private static final String[] CP = new String[] { SingpassIdentityProviderFactory.PROVIDER_ID };

  @Override
  public String[] getCompatibleProviders() {
    return CP;
  }

  @Override
  public String getId() {
    return "singpass-user-attribute-mapper";
  }
}
