package vn.cineman;

import java.math.*;
import java.sql.*;
import java.time.*;
import java.util.concurrent.*;
import vn.cineman.dao.*;
import vn.cineman.model.*;
import vn.cineman.service.*;

public final class ProjectTest0 {
  private static int count = 0;

  private static void check(boolean condition, String label) {
    if (!condition) throw new AssertionError(label);
    System.out.println("PASS " + (++count) + " " + label);
  }

  private static int rows(Database0 db, String table) throws SQLException {
    try (var c = db.open();
        var s = c.createStatement();
        var r = s.executeQuery("SELECT COUNT(*) FROM " + table)) {
      r.next();
      return r.getInt(1);
    }
  }

  public static void main(String[] args) throws Exception {
    var db = new Database0("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    db.initialize();
    var reg = new RegistrationService0(new CustomerDAO0(db));
    var x =
        new Registration0(
            "  Nguyễn Văn An  ",
            "0901234567",
            "AN@EXAMPLE.COM",
            "NguyenAn",
            "Abcd@123",
            "Abcd@123");
    long id = reg.register(x);
    check(
        id > 0 && rows(db, "customer") == 1,
        "REG01 valid registration persists account and customer");
    var a = new AccountDAO0(db).findByUsername("nguyenan");
    check(a != null && a.fullName().equals("Nguyễn Văn An"), "REG02 normalize spaces and username");
    check(
        !a.passwordHash().contains("Abcd@123")
            && Password0.verify("Abcd@123", a.passwordHash())
            && !Password0.verify("wrong", a.passwordHash()),
        "REG03 hashed password and verification");
    check(
        reg.validate(
                new Registration0("An", "123", "an@example.com", "valid", "12345678", "12345678"))
            != null,
        "REG04 invalid phone rejected");
    check(
        reg.validate(
                new Registration0("An", "0901234567", "bad email", "valid", "12345678", "12345678"))
            != null,
        "REG05 invalid email rejected");
    check(
        reg.validate(
                new Registration0(
                    "An", "0901234567", "an@example.com", "valid", "12345678", "different"))
            != null,
        "REG06 mismatched password rejected");
    check(
        reg.validate(
                new Registration0("An", "0901234567", "an@example.com", "valid", "short", "short"))
            != null,
        "REG07 short password rejected");
    for (int mode = 0; mode < 3; mode++) {
      int before = rows(db, "account");
      boolean rejected = false;
      var y =
          new Registration0(
              "Nguyễn An",
              mode == 0 ? "0901234567" : "0901234568",
              mode == 1 ? "an@example.com" : "second@example.com",
              mode == 2 ? "nguyenan" : "second" + mode,
              "Abcd@123",
              "Abcd@123");
      try {
        reg.register(y);
      } catch (SQLException e) {
        rejected = "23505".equals(e.getSQLState());
      }
      check(
          rejected && rows(db, "account") == before,
          "REG" + String.format("%02d", 8 + mode) + " duplicate constraint and rollback");
    }
    var pool = Executors.newFixedThreadPool(2);
    Callable<Boolean> task =
        () -> {
          try {
            reg.register(
                new Registration0(
                    "Concurrent User",
                    "0909999999",
                    "race@example.com",
                    "raceuser",
                    "Abcd@123",
                    "Abcd@123"));
            return true;
          } catch (SQLException ex) {
            return false;
          }
        };
    var f1 = pool.submit(task);
    var f2 = pool.submit(task);
    boolean b1 = f1.get(), b2 = f2.get();
    pool.shutdown();
    check(
        b1 != b2 && rows(db, "customer") == 2,
        "REG11 concurrent duplicate creates exactly one customer");
    var dao = new RevenueDAO0(db);
    var from = LocalDate.of(2026, 11, 1);
    var to = LocalDate.of(2026, 11, 30);
    var movies = dao.getMovieRevenueStats(from, to);
    check(
        movies.size() == 2
            && movies.get(0).revenue().compareTo(new BigDecimal("480000")) == 0
            && movies.get(1).revenue().compareTo(new BigDecimal("270000")) == 0,
        "REV01 movie totals and descending order");
    check(
        movies.stream().mapToLong(MovieRevenue0::ticketCount).sum() == 9,
        "REV02 cancelled invoices and outside dates excluded");
    BigDecimal total = BigDecimal.ZERO;
    for (var m : movies) {
      var shows = dao.getShowtimeStats(m.movieId(), from, to);
      BigDecimal film = BigDecimal.ZERO;
      for (var s : shows) {
        var inv = dao.getInvoicesByShowtime(s.showtimeId(), from, to);
        var sum =
            inv.stream().map(InvoiceRevenue0::revenue).reduce(BigDecimal.ZERO, BigDecimal::add);
        check(
            sum.compareTo(s.revenue()) == 0, "REV03 invoice sum equals showtime " + s.showtimeId());
        film = film.add(s.revenue());
      }
      check(film.compareTo(m.revenue()) == 0, "REV04 showtime sum equals movie " + m.movieId());
      total = total.add(film);
    }
    check(total.compareTo(new BigDecimal("750000")) == 0, "REV05 grand total equals 750000 VND");
    check(
        dao.getMovieRevenueStats(LocalDate.of(2026, 11, 30), to).get(0).ticketCount() == 1,
        "REV06 end date includes 23:59:59");
    check(
        dao.getMovieRevenueStats(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 1))
                .get(0)
                .revenue()
                .compareTo(new BigDecimal("90000"))
            == 0,
        "REV07 midnight next month belongs to next range");
    check(
        dao.getMovieRevenueStats(LocalDate.of(2027, 1, 1), LocalDate.of(2027, 1, 31)).isEmpty(),
        "REV08 empty range");
    boolean bad = false;
    try {
      RevenueService0.validateRange(to, from);
    } catch (IllegalArgumentException ex) {
      bad = true;
    }
    check(bad, "REV09 reversed date range rejected");
    try (var c = db.open();
        var s = c.createStatement()) {
      s.executeUpdate("INSERT INTO seat VALUES(90,2,'B4')");
      s.executeUpdate("INSERT INTO invoice VALUES(90,NULL,'2026-11-01 08:00:00','PAID')");
      s.executeUpdate("INSERT INTO ticket VALUES(90,90,1,4,80000),(91,90,3,90,90000)");
    }
    check(
        dao.getInvoicesByShowtime(1, from, to).stream()
                .filter(v -> v.invoiceId() == 90)
                .findFirst()
                .orElseThrow()
                .revenue()
                .compareTo(new BigDecimal("80000"))
            == 0,
        "REV10 mixed invoice counts only selected showtime tickets");
    System.out.println("TOTAL " + count + " passed");
  }
}
