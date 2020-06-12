package org.keycloak.provider.singpass;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.singpass.SingpassIdentityProvider;
import org.keycloak.provider.singpass.SingpassIdentityProviderConfig;
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
