package org.thoughtworks.provider.singpass;

import static org.junit.Assert.assertNotNull;

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
  }

  @Test
  void should_success_when_validate_id_token() {
    // given

    SingpassIdentityProvider singpassIdentityProvider =
        new SingpassIdentityProvider(session, config);
    String singpassJweResponse =
        "{\"recipients\":[{\"encrypted_key\":\"o6E1g1y4mzOUVh8a5YpzxfNk5FkK0vqrhDgJKPf1cuBsuqAeV6dzcWBqfi5tDhGca0E4BwnsEMNEJNxBu0V86VwNnQkCJjPT5mAlZZ-YazmAdDaBM6YHeecdqFrTFOlacLpxmZQnueqfdrs1CUW9tMjQY0N7K5ZpFm471P2dsNEgN7amkKpvu6DTQOGl0CtZ7lavF_Ku_I2YUIqKDI-vET-pHtQp7-NKHex2QpOfIhpM43Qn4bJgTToVMhP-11Na7BFW1AfHVv1mdEAFBnOR-S4wxGJXV2_cGgm8odzRNQ-dmBAd3CpcFhFJHNaCTpbSIb9HFv7ASsEEBpBl2QEVbw\"}],\"protected\":\"eyJlbmMiOiJBMTI4Q0JDLUhTMjU2IiwiYWxnIjoiUlNBLU9BRVAiLCJraWQiOiJiemEwZFhmNkZqbGExRlFyVkttQVR1WmI5LTRNOTBMeER1ZjN1akxZYnFnIn0\",\"iv\":\"jQxbeWgHwQXlUsm9NtiiNg\",\"ciphertext\":\"G1OauFsuoUoJFX9njr2d2N6ybmEsXmkbz8tqi5aGh7TvUr5i-AkQDS-KPlup5nZCHuzV_0RAg-SVxX_7YPfdV5RMLAcM9GgE9DOavio7MKRhVoDuNmj2PA1V23NZIJHP7uyNxbqhdjH2WXkrdJbYx-RB66rHxajUXpa56pSmMwBHCJsmjkwl6Qk3N2DiWlAGms0hxrdYDTEyKUXOU5LYm0lRS3RQdJeOABy6FSI6cTVEAzQT62nruB8jil0i3lXysM2Zq8Ch4XuabBtCWkkUxYW0d9Dh-pKb6RonjSUeCLnDQTOnbT_E96uUaCZ8tGaZwr4D7glOTHbN1qnVdGJjf5R4LDRYtlylEfIPtcyU-7dXCHdqk3MuI2KiPz5ZacMtxUOrEsajefq1wZpaJPYykan0HAy9irw_d2VXyLi6yv6ri-ZsGlFYr61z8jiDurUaTbrrA396ggB32nQyUZkNy6BWKDnwEjpTa25Sh5XKnl9Ur-pI6TTd7UPTmLBTnq6v1810IT-6dBpJM_m3NyjjHBgT9jDNX1uuvDJw3YFrMCfmG8HkjP4d1e1FhiTdgQEbrV6cULSKbE0FUx1cXwwApGatNxcahfnQ6UvlAEWeKNu8_TlJnMzyUcgA77TNBe2qdT6TdUr5oRaDc55vunzsQkMcJoR7woSToHGspGA5sn8igO4GlV3MvJ65ASNIMbJEkewmYdOdN1hWj1aZsH3ZlrnpC_HM8W4-Qgg6PibbPTmJ5uhX8k818Pior4Kml-6nGSxVvyRKaTIF4GY0ZzJBgPPCjDsU53edksvSj3lNzzI\",\"tag\":\"wlU036mMvPNMTivPJBhstA\"}";

    // when
    JsonWebToken token = singpassIdentityProvider.validateToken(singpassJweResponse, false);

    // then
    assertNotNull(token);
  }
}
