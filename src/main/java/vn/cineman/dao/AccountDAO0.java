package vn.cineman.dao;

import java.sql.*;
import vn.cineman.model.Account0;

public final class AccountDAO0 {
  private final Database0 db;

  public AccountDAO0(Database0 db) {
    this.db = db;
  }

  public Account0 findByUsername(String username) throws SQLException {
    try (var c = db.open();
        var s = c.prepareStatement("SELECT * FROM account WHERE username=?")) {
      s.setString(1, username);
      try (var r = s.executeQuery()) {
        return r.next()
            ? new Account0(
                r.getLong("id"),
                r.getString("username"),
                r.getString("password_hash"),
                r.getString("role"),
                r.getString("full_name"))
            : null;
      }
    }
  }
}
