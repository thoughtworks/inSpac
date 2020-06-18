package org.thoughtworks.provider.singpass.utils;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class PrivateKeyUtils {
  private static final String PKCS1_TYPE = "RSA PRIVATE KEY";
  private static final String PKCS8_TYPE = "PRIVATE KEY";

  public static PrivateKey parsePrivateKey(String pemPrivateKey) throws IOException {
    PemObject pemObject = new PemReader(new StringReader(pemPrivateKey)).readPemObject();

    AsymmetricKeyParameter keyParameter;
    if (PKCS8_TYPE.equals(pemObject.getType())) {
      keyParameter = pkcs8PrivateKey(pemObject);
    } else if (PKCS1_TYPE.equals(pemObject.getType())) {
      keyParameter = pkcs1PrivateKey(pemObject);
    } else {
      throw new IOException();
    }

    JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
    return jcaPEMKeyConverter.getPrivateKey(
        PrivateKeyInfoFactory.createPrivateKeyInfo(keyParameter));
  }

  private static RSAPrivateCrtKeyParameters pkcs1PrivateKey(PemObject pemObject) {
    RSAPrivateKey rsa = RSAPrivateKey.getInstance(pemObject.getContent());

    return new RSAPrivateCrtKeyParameters(
        rsa.getModulus(),
        rsa.getPublicExponent(),
        rsa.getPrivateExponent(),
        rsa.getPrime1(),
        rsa.getPrime2(),
        rsa.getExponent1(),
        rsa.getExponent2(),
        rsa.getCoefficient());
  }

  private static AsymmetricKeyParameter pkcs8PrivateKey(PemObject pemObject) throws IOException {
    return PrivateKeyFactory.createKey(pemObject.getContent());
  }
}
