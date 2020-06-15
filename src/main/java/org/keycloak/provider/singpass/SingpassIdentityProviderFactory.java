package org.keycloak.provider.singpass;

import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

/** @author yuexie.zhou */
public class SingpassIdentityProviderFactory
    extends AbstractIdentityProviderFactory<SingpassIdentityProvider>
    implements SocialIdentityProviderFactory<SingpassIdentityProvider> {

  public static final String PROVIDER_ID = "singpass";

  @Override
  public String getName() {
    return "Singpass";
  }

  @Override
  public SingpassIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
    System.out.println("SingpassIdentityProviderFactory: init provider");
    System.out.println(model.toString());

    return new SingpassIdentityProvider(session, new SingpassIdentityProviderConfig(model));
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }
}
