package org.keycloak.provider.singpass.mapper;

import org.keycloak.broker.oidc.mappers.UsernameTemplateMapper;
import org.keycloak.provider.singpass.SingpassIdentityProviderFactory;

public class SingpassUsernameTemplateMapper extends UsernameTemplateMapper {

  private static final String MAPPER_NAME = "singpass-username-template-mapper";

  @Override
  public String[] getCompatibleProviders() {
    return new String[] {SingpassIdentityProviderFactory.PROVIDER_ID};
  }

  @Override
  public String getId() {
    return MAPPER_NAME;
  }
}
