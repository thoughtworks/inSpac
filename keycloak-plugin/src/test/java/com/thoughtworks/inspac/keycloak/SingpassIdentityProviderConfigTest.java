package com.thoughtworks.inspac.keycloak;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.keycloak.models.IdentityProviderModel;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
public class SingpassIdentityProviderConfigTest {
  @Mock() private SingpassIdentityProviderConfig config;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    IdentityProviderModel model = new IdentityProviderModel();
    config = new SingpassIdentityProviderConfig(model);
  }

  @Test
  void should_get_nric_success_when_invoke_getFirstNameBySub() {
    String subject = "s=R42188888E";
    config.getConfig().put("firstNameLinkToType", SingpassIdentityProviderConfig.LinkToType.NRIC.name());

    String nric = config.getFirstNameBySub(subject);

    assertEquals(nric, "R42188888E");
  }

  @Test
  void should_get_uuid_success_when_invoke_getFirstNameBySub() {
    String subject = "u=c302a626-b1de-11ea-b3de-0242ac130010";
    config.getConfig().put("firstNameLinkToType", SingpassIdentityProviderConfig.LinkToType.UUID.name());

    String uuid = config.getFirstNameBySub(subject);

    assertEquals(uuid, "c302a626-b1de-11ea-b3de-0242ac130010");
  }

  @Test
  void should_get_sub_success_when_invoke_getFirstNameBySub() {
    String subject = "s=R324234E,u=c302a626-b1de-11ea-b3de-0242ac130010";
    config.getConfig().put("firstNameLinkToType", SingpassIdentityProviderConfig.LinkToType.SUB.name());

    String sub = config.getFirstNameBySub(subject);

    assertEquals(sub, subject);
  }

  @Test
  void should_get_sub_success_when_invoke_getFirstNameBySub_with_mistake_type() {
    String subject = "s=R324234E,u=c302a626-b1de-11ea-b3de-0242ac130010";
    config.getConfig().put("firstNameLinkToType", "NOTHING");

    String sub = config.getFirstNameBySub(subject);

    assertEquals(sub, subject);
  }
}
