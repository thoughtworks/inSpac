package com.thoughtworks.provider.singpass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.thoughtworks.provider.singpass.SingpassIdentityProviderConfig.LinkToType;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
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
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @BeforeEach
  public void beforeEach() throws IOException {
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

  @Test
  void
      should_set_username_from_id_token_userinfo_when_set_support_field_cp_and_off_auto_register() {
    // given
    config.getConfig().put("supportFieldForCP", "userInfo.fullName");
    config.getConfig().put("autoRegister", "false");
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject("s=R12312312D");
    String username = "li bobo";
    JSONObject idTokenUserInfo = new JSONObject();
    idTokenUserInfo.put("fullName", username);
    jsonWebToken.setOtherClaims("userInfo", idTokenUserInfo);
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // when
    BrokeredIdentityContext identity =
        singpassIdentityProvider.extractIdentity(null, null, jsonWebToken);

    // then
    assertEquals(username, identity.getUsername());
  }

  @Test
  void should_set_name_success_when_set_support_cp_field_and_open_auto_register() {
    // given
    config.getConfig().put("supportFieldForCP", "userInfo.fullName");
    config.getConfig().put("autoRegister", "true");
    config.getConfig().put("userNameLinkToType", LinkToType.CP_INFO.name());
    config.getConfig().put("firstNameLinkToType", LinkToType.NRIC.name());
    config.getConfig().put("lastNameLinkToType", LinkToType.SUB.name());
    String nric = "R12312312D";
    String sub = "s=" + nric;
    String username = "li bobo";
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject(sub);
    JSONObject idTokenUserInfo = new JSONObject();
    idTokenUserInfo.put("fullName", username);
    jsonWebToken.setOtherClaims("userInfo", idTokenUserInfo);
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // when
    BrokeredIdentityContext identity =
        singpassIdentityProvider.extractIdentity(null, null, jsonWebToken);

    // then
    assertEquals(username, identity.getUsername());
    assertEquals(nric, identity.getFirstName());
    assertEquals(sub, identity.getLastName());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "a.b",
      "userInfo.b"
  })
  void should_throw_exception_when_set_support_cp_field_with_invalid_path(String path) {
    // given
    config.getConfig().put("supportFieldForCP", path);
    config.getConfig().put("autoRegister", "false");
    String NRIC = "R12312312D";
    JsonWebToken jsonWebToken = new JsonWebToken();
    jsonWebToken.setSubject("s=" + NRIC);
    String username = "li bobo";
    JSONObject idTokenUserInfo = new JSONObject();
    idTokenUserInfo.put("fullName", username);
    jsonWebToken.setOtherClaims("userInfo", idTokenUserInfo);
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // when
    Assertions.assertThrows(
        IdentityBrokerException.class,
        () -> singpassIdentityProvider.extractIdentity(null, null, jsonWebToken),
        "Support Field For CP: can't get value form id token");
  }

  @Test
  void should_success_when_validate_id_token_with_certificate() throws IOException {
    // given
    config.setPublicKeySignatureVerifier(
        IOUtils.toString(
            this.getClass().getResourceAsStream("/singpassCertificate.cert"), "UTF-8"));
    // when
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // then
    String publicKeySignatureVerifier =
        singpassIdentityProvider.getConfig().getPublicKeySignatureVerifier();
    assertEquals(
        publicKeySignatureVerifier,
        "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBddM8pYSeLSDBX2wCNQ9M94MNqcL0opFx+1nhvpDE+BDO6AfsCFzJe13EJqJO0qf3La81bUmAqUhavRlJTmsOs8btcRKonEtPQagHJczqqR9jXiG2XmcyXQ0g3KU0FhUHHB881ERpewRf0SQttv2nte3Ruxy4AnlGRDW4Jbd/480rnCQIv+2eJwnacYm4Kz3SZimafKKw+yU2J3393NsPSow6BRLEgcnbV503swlVpEJrlxUUfx8ylduXqdKPjtuODag3JyEqiR+irSTir1IotIOlAzK36/gRrX1I7nZVVhT7iUfr8HvrcfcBaRvfIUyfflMuqS0TxoNrDImkQWQPhAgMBAAE=");
  }

  @Test
  void should_success_when_validate_id_token_with_public_key() throws IOException {
    // given
    config.setPublicKeySignatureVerifier(
        IOUtils.toString(this.getClass().getResourceAsStream("/singpassPublicKey.pem"), "UTF-8"));

    // when
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // then
    String publicKeySignatureVerifier =
        singpassIdentityProvider.getConfig().getPublicKeySignatureVerifier();
    assertEquals(
        publicKeySignatureVerifier,
        "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBddM8pYSeLSDBX2wCNQ9M9\n"
            + "4MNqcL0opFx+1nhvpDE+BDO6AfsCFzJe13EJqJO0qf3La81bUmAqUhavRlJTmsOs\n"
            + "8btcRKonEtPQagHJczqqR9jXiG2XmcyXQ0g3KU0FhUHHB881ERpewRf0SQttv2nt\n"
            + "e3Ruxy4AnlGRDW4Jbd/480rnCQIv+2eJwnacYm4Kz3SZimafKKw+yU2J3393NsPS\n"
            + "ow6BRLEgcnbV503swlVpEJrlxUUfx8ylduXqdKPjtuODag3JyEqiR+irSTir1Iot\n"
            + "IOlAzK36/gRrX1I7nZVVhT7iUfr8HvrcfcBaRvfIUyfflMuqS0TxoNrDImkQWQPh\n"
            + "AgMBAAE=\n"
            + "-----END PUBLIC KEY-----");
  }

  private void setSingpassConfig() throws IOException {
    IdentityProviderModel model = new IdentityProviderModel();
    config = new SingpassIdentityProviderConfig(model);
    config.setDefaultScope("openid");
    config.setClientId("client_id");
    config.setClientSecret("tIo0D7UpXjpxsVvKo9o9");
    config.setValidateSignature(false);
    config.setPublicKeySignatureVerifier(
        IOUtils.toString(this.getClass().getResourceAsStream("/singpassPublicKey.pem"), "UTF-8"));
    config.setPrivateKey(
        IOUtils.toString(
            this.getClass().getResourceAsStream("/keycloakEncryptedPrivateKey.txt"), "UTF-8"));
    config.getConfig().put("firstNameLinkToType", LinkToType.NRIC.name());
    config.getConfig().put("userNameLinkToType", LinkToType.UUID.name());
    config.getConfig().put("lastNameLinkToType", LinkToType.SUB.name());
    config.getConfig().put("autoRegister", "true");
  }
}
