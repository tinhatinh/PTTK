package vn.cineman.service;

import java.sql.SQLException;
import java.util.*;
import vn.cineman.dao.CustomerDAO0;
import vn.cineman.model.Registration0;

public final class RegistrationService0 {
  private final CustomerDAO0 dao;

  public RegistrationService0(CustomerDAO0 dao) {
    this.dao = dao;
  }

  public Registration0 normalize(Registration0 x) {
    return new Registration0(
        x.fullName().trim(),
        x.phone().trim(),
        x.email().trim().toLowerCase(Locale.ROOT),
        x.username().trim().toLowerCase(Locale.ROOT),
        x.password(),
        x.confirmPassword());
  }

  public String validate(Registration0 x) {
    if (x.fullName().length() < 2 || x.fullName().length() > 100)
      return "Họ tên phải có từ 2 đến 100 ký tự.";
    if (!x.phone().matches("0[0-9]{9}")) return "Số điện thoại gồm 10 chữ số và bắt đầu bằng 0.";
    if (x.email().length() > 120 || !x.email().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
      return "Email không hợp lệ.";
    if (!x.username().matches("[a-z0-9_]{4,40}"))
      return "Tên đăng nhập gồm 4–40 chữ thường, số hoặc dấu gạch dưới.";
    if (x.password().length() < 8 || x.password().length() > 128)
      return "Mật khẩu phải có từ 8 đến 128 ký tự.";
    if (!x.password().equals(x.confirmPassword())) return "Hai lần nhập mật khẩu không khớp.";
    return null;
  }

  public long register(Registration0 x) throws SQLException {
    x = normalize(x);
    String error = validate(x);
    if (error != null) throw new IllegalArgumentException(error);
    return dao.saveRegistration(x, Password0.hash(x.password()));
  }
}
