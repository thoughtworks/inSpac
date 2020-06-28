package com.thoughtworks.provider.singpass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.thoughtworks.provider.singpass.SingpassIdentityProviderConfig.LinkToType;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.JsonWebToken;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
public class SingpassIdentityProviderTest {
  @Mock() private KeycloakSession session;
  @Mock() private SingpassIdentityProviderConfig config;

  @BeforeAll
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);

    setSingpassConfig();
  }

  @Test
  void should_success_when_validate_id_token() {
    // given

    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    String encryptIdToken =
        "eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJiemEwZFhmNkZqbGExRlFyVkttQVR1WmI5LTRNOTBMeER1ZjN1akxZYnFnIn0.LXallKksW19_hV9qaSd_WmDup5TGC76WXTgkJZVT3wmj0bQtStpztCD1B3jYWFnoE9OWWHrfCld7UvOFTT5SLe9l4OfC4Fs6bJz7cM8yiFsHykztTtl1I7XY3Qwl5v8MBnK7eayBjRInbUM70bZdKl8rNKSHZwsp4Gld9ckl40B6KV6WuQL2NttpUxaWiCgcizbGFHK7ScfM797cgTdGM-9R8--V3jfgiw7UwAVCfLC392ecpitI_Lsxjkw4RZwuaNrJ51svKnb5sF3E9TR0Ztbaylp54q803bOaR5CmAEoHwj3z6nd8iaLJa0E8nK2JK3lAvulOATTKrB08hbZUng.k4LSvbdrLnUmoJ5ECl0-KA.9t5zyClkPiUWyJ7Rouw1sSD2KdXCRnKIidNnxZwa6upOjVzIKSylMAhSQpnfCQz5bwAnjSSVAu4Pa756WRK8qziJmipzo9t1CYwe7CmqO1FKyFbiJi589Nz1BjP57dzjfpbETgaRs2qp1x5uwPd0Hvo6QwUqUJoiugyXzFUGJBrJXmd7YBpivdTY_gqjpuA5L4dsAXIyWLOp6YaUaNG4Y8Y53rILcPOTHj2Ryc_xvRA2t-QSpoEX507CLN__WmluHL2MvbEFmWn0Lo_LFJqrupKfLT2rI07T8qyBnFQKszJS2xCLXf8Dn7VKxuy-ZUg4vvamWwQgM2rlTFIxoSamWxNUwngH2xzsUbBlg6v2U0cBP_pW1H1nPSwqXHYUpk3TZcCkeZZ8sXhjBKa0zZP5zGKSuE3K3lZxxYP8HRnlmR8WX-okHEXFPzp_jNuizedQrk8PX56XpTrbfEb1skLRvsYs7z0gZonr2mPnTKwkxR9676qfs-QugpPZAxb0WXOMD8rBB1vAkVAuU06u-rg2OxKMH2hCgUwnCYrzsii7MVyLRZ6Wa2omxhYSPors3oTm6gv040JpaM8HcO4AJLRu5opQfsulbXhflcSin-HRVk_dLOLoWMZbL94Aa2OmfMlYh-IhgneNjzzEsjkpuXmwZ-xTBrfiqtgpuUTN0CPhPUOAqipb2oxc9BT0IkSxPkmU-EdavE4mZF3ySsaaeWD3afB6clc9DmuaxeB1O9NtD8HQyPe4Zzm4utiX0OLI_YtFDurSyMB8rYZpdKyjkbYZL-_IcUouAkSiM7EedrzUMLzh0rs3JuApCqfVV2sWpuO1XQplmfT_JTOoLmZYh8eyBqmmgghnRjg9SyRYGbbDEv17C4_1TJr0_NoEmmh4BT_5ERuEZy136m-I8wtVNJh0grY4j6h9w-zbUwpJYINCvD4.P1RwZwpm9df_ZF090vNGQg";

    // when
    JsonWebToken token = singpassIdentityProvider.validateToken(encryptIdToken, true);

    // then
    assertNotNull(token);
    assertArrayEquals(token.getAudience(), Collections.singletonList("client_id").toArray());
  }

  @Test
  void should_parse_sub_success_when_sub_only_have_uuid() {
    String uuid = "c302a626-b1de-11ea-b3de-0242ac130010";
    String subject = "u=" + uuid;
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject(subject);

    BrokeredIdentityContext identity =
        singpassIdentityProvider.extractIdentity(null, null, jsonWebToken);

    assertEquals(subject, identity.getId());
    assertEquals("NONE", identity.getFirstName());
    assertEquals(subject, identity.getLastName());
    assertEquals(uuid, identity.getUsername());
    assertEquals(uuid + SingpassIdentityProvider.EMAIL_HOST, identity.getEmail());
  }

  @Test
  void should_parse_sub_success_when_sub_has_nric_and_uuid() {
    String nric = "R12312312D";
    String uuid = "c302a626-b1de-11ea-b3de-0242ac130010";
    String subject = "s=" + nric + "," + "u=" + uuid;
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject(subject);

    BrokeredIdentityContext identity =
        singpassIdentityProvider.extractIdentity(null, null, jsonWebToken);

    assertEquals(subject, identity.getId());
    assertEquals(nric, identity.getFirstName());
    assertEquals(subject, identity.getLastName());
    assertEquals(uuid, identity.getUsername());
    assertEquals(uuid + SingpassIdentityProvider.EMAIL_HOST, identity.getEmail());
  }

  @Test
  void should_parse_sub_success_when_sub_has_nric_and_uuid_and_country() {
    String nric = "R12312312D";
    String uuid = "c302a626-b1de-11ea-b3de-0242ac130010";
    String country = "SG";
    String subject = "s=" + nric + "," + "u=" + uuid + "," + "c=" + country;
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject(subject);

    BrokeredIdentityContext identity =
        singpassIdentityProvider.extractIdentity(null, null, jsonWebToken);

    assertEquals(subject, identity.getId());
    assertEquals(nric, identity.getFirstName());
    assertEquals(subject, identity.getLastName());
    assertEquals(uuid, identity.getUsername());
    assertEquals(uuid + SingpassIdentityProvider.EMAIL_HOST, identity.getEmail());
  }

  private void setSingpassConfig() throws IOException {
    IdentityProviderModel model = new IdentityProviderModel();
    config = new SingpassIdentityProviderConfig(model);
    config.setDefaultScope("openid");
    config.setClientId("client_id");
    config.setClientSecret("tIo0D7UpXjpxsVvKo9o9");
    config.setValidateSignature(false);
    config.setPrivateKey(
        IOUtils.toString(this.getClass().getResourceAsStream("/keycloakEncryptedPrivateKey.txt"), "UTF-8"));
    config.getConfig().put("firstNameLinkToType", LinkToType.NRIC.name());
    config.getConfig().put("userNameLinkToType", LinkToType.UUID.name());
    config.getConfig().put("lastNameLinkToType", LinkToType.SUB.name());
    config.getConfig().put("autoRegister", "true");
  }
}
