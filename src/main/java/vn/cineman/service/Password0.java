package vn.cineman.service;

import java.security.*;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Password0 {
  private Password0() {}

  public static String hash(String value) {
    byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt)
        + ":"
        + Base64.getEncoder().encodeToString(derive(value, salt));
  }

  private static byte[] derive(String value, byte[] salt) {
    var spec = new PBEKeySpec(value.toCharArray(), salt, 120000, 256);
    try {
      return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException(e);
    } finally {
      spec.clearPassword();
    }
  }

  public static boolean verify(String value, String stored) {
    try {
      var p = stored.split(":");
      return MessageDigest.isEqual(
          derive(value, Base64.getDecoder().decode(p[0])), Base64.getDecoder().decode(p[1]));
    } catch (RuntimeException e) {
      return false;
    }
  }
}
