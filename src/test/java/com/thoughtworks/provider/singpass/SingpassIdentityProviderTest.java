package com.thoughtworks.provider.singpass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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

    IdentityProviderModel model = new IdentityProviderModel();
    config = new SingpassIdentityProviderConfig(model);
    config.setDefaultScope("openid");
    config.setClientId("client_id");
    config.setValidateSignature(false);
    config.setPublicKeySignatureVerifier(
        "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAu3YvExh1qjJBpkjALKfC\n"
            + "uB67KqjzCc/EwUrBOkdoEYNLur80A2Y3R4v2q1AW8f8e3bMiPLLSlvxS0a0YyaiT\n"
            + "2+4/ENjwy+dUa1o9XdiWLmHWWMWi7je1IK4zsDxoIt9nSOxaZmhu4aZeP+GhrMiV\n"
            + "LnV0xKDl/4n2QZbD1GkdkG4ZuyqHJsmGcGtXLU1U6r6p6ZDrCTkBXjX96EDobHfX\n"
            + "VFfi/RJ7VOD2aP2p4G7I104tNBYmB9+mkHr4AuOT9PhmOqq4a9Fo1E6VLnlo4FW/\n"
            + "2ZK10gUU0f5wacnNillER0NGNwFWoTj2ASF9LiR6/39b4satRLAy8RxMclva8iK6\n"
            + "yQIDAQAB\n"
            + "-----END PUBLIC KEY-----\n");
    config.setPrivateKey(
        "-----BEGIN PRIVATE KEY-----\n"
            + "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC7di8TGHWqMkGm\n"
            + "SMAsp8K4HrsqqPMJz8TBSsE6R2gRg0u6vzQDZjdHi/arUBbx/x7dsyI8stKW/FLR\n"
            + "rRjJqJPb7j8Q2PDL51RrWj1d2JYuYdZYxaLuN7UgrjOwPGgi32dI7FpmaG7hpl4/\n"
            + "4aGsyJUudXTEoOX/ifZBlsPUaR2Qbhm7KocmyYZwa1ctTVTqvqnpkOsJOQFeNf3o\n"
            + "QOhsd9dUV+L9EntU4PZo/angbsjXTi00FiYH36aQevgC45P0+GY6qrhr0WjUTpUu\n"
            + "eWjgVb/ZkrXSBRTR/nBpyc2KWURHQ0Y3AVahOPYBIX0uJHr/f1vixq1EsDLxHExy\n"
            + "W9ryIrrJAgMBAAECggEAY8jsE+kQMRFhWqcdDGgcQS+yh2m5PP7Ih+9H3cLGxZOz\n"
            + "CuvePvT49e+t1NDj9drMTkydK9wwNsiHOS8/o5BFbGtsTIZ93rv7ds1pHvw8LOJN\n"
            + "W6GQMeebVZME1onBENcEPo/5KsvqQdjyEGUFT1jR+BHznvrakuSYHZ+oC/gMEaVw\n"
            + "dUsM1849EbLDJ5lOPDDqYwsJwIGEryxRLFP+4HhGR9wnrTVee5CCsH0ep8OqypvY\n"
            + "xgg9Ytyt1WripwzhXsVzJxahTbO4XImOgv6Uvo3EYfBXm1gbfugGbSiyYHBBx0Pa\n"
            + "7DtYrpRzjeS33m6Y4SJjKERjCbHXChwUrFcMGS5ogQKBgQDeChbIHYRu4HTQYoV9\n"
            + "pJ70LcHCiE0zaJzuMHes4OFkNAuFXASDGp1HJXd0tol+oeuYO/q1gENvyfBAqDH/\n"
            + "AVAtWaFvQUNzv/Zs++mbrSSwxuZJCaJAbh9GKzCyCbapxBtdzhgCLLY9GeWRiG1c\n"
            + "puYUUvJmkcWQE9sSn2tnE8mv2QKBgQDYIjWgagTLPlT3J65t5BEs9fwjAq8bdIIV\n"
            + "E9o0blIwlKF3FzeUo4Egjc/vfIYOL43AOttuVbVoruvvT13a3+VpCXjxqSYnveuw\n"
            + "FB0xQC1F0c3ay4Oprjh2qk2GfOy0gikaRgmTUP3nVK1LC57GWJMker/UQ2kWCJCs\n"
            + "RyMzpgd8cQKBgQCE5q8KKrjJEOp6jG3wbWeDKhwuzxy+Z6B+5V3MiXH/YzN+KDy/\n"
            + "KF/5ZNCieFvGAy8cGNKQbuxubgWy/bmnM+cErgB1si+oib77LrF+L92lPfg6wVxv\n"
            + "ijqH6nQkLLI73RiwRhqSuqZ93hFN0cX7zh4rDhbvE9OX0HqxI+DKesqeyQKBgEln\n"
            + "hPMQTsSATPcMAQ/Nb4/nk1SIqtQWQ7/I2EkKVtus/xGlTvkqdsaJo19g2V6kA+6P\n"
            + "jsrwTQZaskK6n9OgSxfbYbohipXgyNUqX6fEdhvKX7G5gOP2CbMzr9THRNUhh7gm\n"
            + "pUXlMfaJKbndHnWay46OKex7YItdKVV5a5k1AEHhAoGBAIVSqdn2f4mVgdQxDls6\n"
            + "DPp0XJW2D9nI9w9mm+wZc9TtI7X+7+1LZ609vDHAEfj8flTMl0t/ovaaZWmEHCpg\n"
            + "FKM9rIqep3tZy6qqgo63peDk+RPV3UVgnUwq0+mBwhnHPWQf2nfYnZZHwsldfHWP\n"
            + "y0U6Qc7UNI3EjJEJPTI4Zifg\n"
            + "-----END PRIVATE KEY-----");

    //    when(session.getContext().getRealm()).thenReturn()
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
  void should_success_when_parse_response() {
    // given
    String response =
        "{\"access_token\":\"random string\",\"refresh_token\":\"random string\",\"scope\":\"openid\",\"token_type\":\"bearer\",\"id_token\": \"eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJiemEwZFhmNkZqbGExRlFyVkttQVR1WmI5LTRNOTBMeER1ZjN1akxZYnFnIn0.LXallKksW19_hV9qaSd_WmDup5TGC76WXTgkJZVT3wmj0bQtStpztCD1B3jYWFnoE9OWWHrfCld7UvOFTT5SLe9l4OfC4Fs6bJz7cM8yiFsHykztTtl1I7XY3Qwl5v8MBnK7eayBjRInbUM70bZdKl8rNKSHZwsp4Gld9ckl40B6KV6WuQL2NttpUxaWiCgcizbGFHK7ScfM797cgTdGM-9R8--V3jfgiw7UwAVCfLC392ecpitI_Lsxjkw4RZwuaNrJ51svKnb5sF3E9TR0Ztbaylp54q803bOaR5CmAEoHwj3z6nd8iaLJa0E8nK2JK3lAvulOATTKrB08hbZUng.k4LSvbdrLnUmoJ5ECl0-KA.9t5zyClkPiUWyJ7Rouw1sSD2KdXCRnKIidNnxZwa6upOjVzIKSylMAhSQpnfCQz5bwAnjSSVAu4Pa756WRK8qziJmipzo9t1CYwe7CmqO1FKyFbiJi589Nz1BjP57dzjfpbETgaRs2qp1x5uwPd0Hvo6QwUqUJoiugyXzFUGJBrJXmd7YBpivdTY_gqjpuA5L4dsAXIyWLOp6YaUaNG4Y8Y53rILcPOTHj2Ryc_xvRA2t-QSpoEX507CLN__WmluHL2MvbEFmWn0Lo_LFJqrupKfLT2rI07T8qyBnFQKszJS2xCLXf8Dn7VKxuy-ZUg4vvamWwQgM2rlTFIxoSamWxNUwngH2xzsUbBlg6v2U0cBP_pW1H1nPSwqXHYUpk3TZcCkeZZ8sXhjBKa0zZP5zGKSuE3K3lZxxYP8HRnlmR8WX-okHEXFPzp_jNuizedQrk8PX56XpTrbfEb1skLRvsYs7z0gZonr2mPnTKwkxR9676qfs-QugpPZAxb0WXOMD8rBB1vAkVAuU06u-rg2OxKMH2hCgUwnCYrzsii7MVyLRZ6Wa2omxhYSPors3oTm6gv040JpaM8HcO4AJLRu5opQfsulbXhflcSin-HRVk_dLOLoWMZbL94Aa2OmfMlYh-IhgneNjzzEsjkpuXmwZ-xTBrfiqtgpuUTN0CPhPUOAqipb2oxc9BT0IkSxPkmU-EdavE4mZF3ySsaaeWD3afB6clc9DmuaxeB1O9NtD8HQyPe4Zzm4utiX0OLI_YtFDurSyMB8rYZpdKyjkbYZL-_IcUouAkSiM7EedrzUMLzh0rs3JuApCqfVV2sWpuO1XQplmfT_JTOoLmZYh8eyBqmmgghnRjg9SyRYGbbDEv17C4_1TJr0_NoEmmh4BT_5ERuEZy136m-I8wtVNJh0grY4j6h9w-zbUwpJYINCvD4.P1RwZwpm9df_ZF090vNGQg\"}";
    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);

    // when
    Assertions.assertDoesNotThrow(() -> singpassIdentityProvider.getFederatedIdentity(response));
  }
}
