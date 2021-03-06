package com.thoughtworks.inspac.keycloak;

import static java.util.Arrays.asList;

import com.thoughtworks.inspac.keycloak.representations.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.IdentityProviderModel;

/** @author yuexie.zhou */
public class SingpassIdentityProviderConfig extends OIDCIdentityProviderConfig {

  private static final String SUPPORT_CP_FIELD_SPLIT_REGEX = "\\.";

  public SingpassIdentityProviderConfig(IdentityProviderModel model) {
    super(model);
  }

  public SingpassIdentityProviderConfig() {
    setDefaultConfig();
  }

  public Entry getSupportFieldForCP() {
    String supportFieldForCP = this.getConfig().get("supportFieldForCP");

    if (Objects.nonNull(supportFieldForCP) && !supportFieldForCP.trim().isEmpty()) {
      String[] fields = supportFieldForCP.trim().split(SUPPORT_CP_FIELD_SPLIT_REGEX);

      return Pair.of(fields[0], fields[1]);
    }

    return null;
  }

  public boolean isAutoRegister() {
    return Boolean.parseBoolean(this.getConfig().get("autoRegister"));
  }

  public String getFirstNameBySub(String subject) {
    String firstNameLinkToType = getConfig().get("firstNameLinkToType");

    return LinkToType.fromString(firstNameLinkToType).parse(subject);
  }

  public String getLastNameBySub(String subject) {
    String lastNameLinkToType = getConfig().get("lastNameLinkToType");

    return LinkToType.fromString(lastNameLinkToType).parse(subject);
  }

  public String getUserNameBySub(String subject) {
    String userNameLinkToType = getConfig().get("userNameLinkToType");

    return LinkToType.fromString(userNameLinkToType).parse(subject);
  }

  public void setPrivateKey(String privateKey) {
    getConfig().put("privateKey", privateKey);
  }

  public String getPrivateKey() {
    return getConfig().get("privateKey");
  }

  public enum LinkToType {
    NRIC("s"),
    UUID("u"),
    CP_INFO("cp"),
    SUB("sub");

    public static final String DEFAULT_VAL = "NONE";
    private static final String TYPE_SPLIT_SIGN = ",";
    private static final String KEY_VALUE_SPLIT_SIGN = "=";
    private String type;

    LinkToType(String type) {
      this.type = type;
    }

    public static LinkToType fromString(String str) {
      return Arrays.stream(LinkToType.values())
          .filter(toType -> toType.name().equals(str))
          .findFirst()
          .orElse(SUB);
    }

    public String parse(String subject) {
      if (this.equals(LinkToType.SUB)) {
        return subject;
      }

      List<String> subPart = asList(subject.split(TYPE_SPLIT_SIGN));

      return subPart.stream()
          .filter(part -> part.contains(this.type + KEY_VALUE_SPLIT_SIGN))
          .flatMap(part -> Arrays.stream(part.split(KEY_VALUE_SPLIT_SIGN)))
          .skip(1)
          .findFirst()
          .orElse(DEFAULT_VAL);
    }
  }

  private void setDefaultConfig() {
    this.setValidateSignature(true);
    this.setUseJwksUrl(false);

    getConfig().put("firstNameLinkToType", LinkToType.NRIC.name());
    getConfig().put("lastNameLinkToType", LinkToType.UUID.name());
    getConfig().put("userNameLinkToType", LinkToType.SUB.name());
  }
}
