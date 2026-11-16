package vn.cineman.dao;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import vn.cineman.service.Password0;

public final class Database0 {
  private final String url;

  public Database0(String url) {
    this.url = url;
  }

  public Connection open() throws SQLException {
    return DriverManager.getConnection(url, "sa", "");
  }

  public void initialize() throws Exception {
    try (var c = open()) {
      for (String name : new String[] {"schema.sql", "seed.sql"}) {
        if (name.equals("seed.sql")) {
          try (var s = c.createStatement();
              var r = s.executeQuery("SELECT COUNT(*) FROM account")) {
            r.next();
            if (r.getInt(1) > 0) continue;
          }
        }
        String sql;
        try (var in = Database0.class.getResourceAsStream("/" + name)) {
          sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
        if (name.equals("seed.sql"))
          sql = sql.replace("$MANAGER_HASH$", Password0.hash("Manager@123"));
        try (var s = c.createStatement()) {
          for (var part : sql.split(";")) if (!part.isBlank()) s.execute(part);
        }
      }
    }
  }
}
