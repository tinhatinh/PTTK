package vn.cineman.dao;

import java.sql.*;
import java.time.LocalDate;
import vn.cineman.model.Registration0;

public final class CustomerDAO0 {
  private final Database0 db;

  public CustomerDAO0(Database0 db) {
    this.db = db;
  }

  /** Both rows are committed together. Unique constraints arbitrate concurrent requests. */
  public long saveRegistration(Registration0 input, String passwordHash) throws SQLException {
    try (var c = db.open()) {
      c.setAutoCommit(false);
      try {
        long accountId;
        try (var s =
            c.prepareStatement(
                "INSERT INTO account(username,password_hash,role,full_name)"
                    + " VALUES(?,?,'CUSTOMER',?)",
                Statement.RETURN_GENERATED_KEYS)) {
          s.setString(1, input.username());
          s.setString(2, passwordHash);
          s.setString(3, input.fullName());
          s.executeUpdate();
          try (var r = s.getGeneratedKeys()) {
            r.next();
            accountId = r.getLong(1);
          }
        }
        long id;
        try (var s =
            c.prepareStatement(
                "INSERT INTO customer(account_id,phone,email,registered_at) VALUES(?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
          s.setLong(1, accountId);
          s.setString(2, input.phone());
          s.setString(3, input.email());
          s.setObject(4, LocalDate.now());
          s.executeUpdate();
          try (var r = s.getGeneratedKeys()) {
            r.next();
            id = r.getLong(1);
          }
        }
        c.commit();
        return id;
      } catch (SQLException e) {
        c.rollback();
        throw e;
      }
    }
  }
}
