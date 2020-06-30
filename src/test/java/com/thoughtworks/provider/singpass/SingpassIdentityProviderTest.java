package com.thoughtworks.provider.singpass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.thoughtworks.provider.singpass.SingpassIdentityProviderConfig.LinkToType;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
  void should_verify_signature_success_given_cert_file() {
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    String encryptedIdToken =
        "eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJiemEwZFhmNkZqbGExRlFyVkttQVR1WmI5LTRNOTBMeER1ZjN1akxZYnFnIn0.KLF65DCwjZJZY38ZvhznjV0MiWCADLYbhvw38Ub5_LblvNasv8anUrOgTRK-OscgWUNZRoThYMrI9yIDTBtIeamf-kdNo3HkMF0S15MgJHewXkkXjymcJckdoF0iVy9hklaipEKNihdNcdWXeN2fKxbNzxt-LZ1RHUYG6UPDonW4OPxKcerAGQ-j7W00-ZzagBgTIRVfjsLSueJGoF8fhU_z7X3ByG6ySJiMT_bferRDIMFa-O6qavQhAkoYGXIEu1oC3WNw8r3QzF1pIB8GhrWwyanJClMbrdoBKdMM_4g0F6ievAG-hkOk5TiWod4OksiU0s7kJuw8LKN4t-b1ng.DWHwjnv4fxKX2XW9qs6ExA.az8MS4mn6vbfou-uv_CqIyIbSpbWJ1D5ZIA9Bi0AMw8_aOA-dw81tCGOd1hyRjf6zbJutdeRrNiwpQg-yPVCr9QwYky71l67dhg1uIzlAHvwwWlWADLN7Hk0p7HE5nthLolAX3sfWOud_ASakqah71txhHTLKfEuLs_L2O6UR9LceWT5YUbuawtuuvy4C9dEk6fPvvCJ03v9_GusxWWvkuas8Z2NNSj-pNSA5PiCWLggGyjrntPnO6odqejjLXlclOuwHeNfbbzrjtzuJTKT7ussVxmAiI1FFUKzzTsMxnLOMqHRiUTMP5yH24tnTbBQp1dmgbPBNBx1IomLCf5z8ede_FsTqEWAC5SoSWfcD7Z1Qp1Dxx1MfFUKK2gBFhHQje-R1Vh24Kx7kc95INnp5lHkc4fUtJ_XCHOVQ0VARMHJBBKSstQT5cDxQRaY5TdprZsFqopQXNrDj8rlmq2FQn4Hzxb7WTbL9M60RjDcbQYiZeByo562uuR8-2B-KtIYQgwp8hEcvs5vX4gP7SrKPdymnymtDdveNdesEaKfDXoQ3YNkuGeH9ill7TLP5qC-QgA7FfrJ6-Nrma2bYk3a_llSiZvhXXFsyzkRI1a2FmK8XX8d2fWP-ou1vbQF1tHd-srBglk8hCh4Jvmu5yOjmtZWDrTFx7FJkAWpbZ_fxHvsHtL8gqB3UPsI0cJahtROZii9CeJ4zIjlldeUlke5NsKwm2sgdjhK8uSjoXtRtlFyzpSkntvlWW1AkZHsI5T63LCzQsvr04DQKTmGIXQnjAw5-3fTYSn3KqMFPNtmYa4uzSi-VV_xoNdSEfIPt9P7se8xtF-uKgrtQL1OcFa2d2D25m4hYGcSqEWlNRaP6QnOk8MKuiD5hmRY4nFWA8hpVESnWn1Rw1I8jbocOd5Ing.Vxc3x4yFjoSgKrwfJ73E4A";

    JsonWebToken token = singpassIdentityProvider.validateToken(encryptedIdToken, true);

    assertEquals("s=S8116474F,u=1", token.getSubject());
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
    config.setPublicKeySignatureVerifier(
        "-----BEGIN CERTIFICATE-----"
            + "MIIDXTCCAkWgAwIBAgIJAPOw0d9auxjGMA0GCSqGSIb3DQEBCwUAMEUxCzAJBgNV"
            + "BAYTAkFVMRMwEQYDVQQIDApTb21lLVN0YXRlMSEwHwYDVQQKDBhJbnRlcm5ldCBX"
            + "aWRnaXRzIFB0eSBMdGQwHhcNMjAwNjMwMDE0ODIzWhcNMzAwNjI4MDE0ODIzWjBF"
            + "MQswCQYDVQQGEwJBVTETMBEGA1UECAwKU29tZS1TdGF0ZTEhMB8GA1UECgwYSW50"
            + "ZXJuZXQgV2lkZ2l0cyBQdHkgTHRkMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB"
            + "CgKCAQEAr4flVLO/RsDFmgqOduMebMBGiLcd2+zX6VdkdrnWl4/NMehycSR7aSuM"
            + "qkDji/dqsoY3Q1VHWUYoWGLrAjZ/JP/Ad2vwI1D3SJLG4xZKtGawM1g3DkG9Y2ef"
            + "chk1r7+WrMEn81DNJ1ib+462g1yNllgxeDCRaImsgihjztWFRcPu/dvLFu7nVDL0"
            + "nTsN8Sqldh2ydMlNnmXixL63FUaEYHsYylm/60swGla3ibhOfzCbmjkN44vrktBk"
            + "ZHhsI0G15QF5vn0Nbz7DfHp2IuxZXwTnNgjz1QSpV1+kxHosJFTa2D1TS1Ki9FgM"
            + "Ij0av0TqK2MkPq5pnTm0XvZ/O6Hw4wIDAQABo1AwTjAdBgNVHQ4EFgQU286bExRE"
            + "1REHkJpYZViciymk/+EwHwYDVR0jBBgwFoAU286bExRE1REHkJpYZViciymk/+Ew"
            + "DAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAEvjMnvcWyBrZCbLWozPv"
            + "YWu2fQsUGwGLPwhJm6dJmjvDq0/yYv14svvZsVd8NGQxYaqlVA89S1d/1DNOBhZJ"
            + "rAsqxrKswaR26A2MUB0n9HSRNTa69FkW6nhmuTFeBFF9YE1q2VQsMboCCx0wA+vc"
            + "sEZgjgQLNb6rsyhvWX9sc7PfwjqJeLXb9EQVvy0JJyLv/tRTZqxn+dOQNa00juc6"
            + "I5SQxHG8Jnw5j+4ffwYZCshD5eGjMy5kJOAnNZnLRH4OF3ie0Z2ePvKKs5UwBfR+"
            + "/jRrli4e6hs4j/AlCpMNFiPDwa14FaDyYsOEbSG5XeUzfXwagm/lHxtazubPmO1p"
            + "VA=="
            + "-----END CERTIFICATE-----");
  }
}
