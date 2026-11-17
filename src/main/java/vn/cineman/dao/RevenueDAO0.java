package vn.cineman.dao;

import java.sql.*;
import java.time.*;
import java.util.*;
import vn.cineman.model.*;

public final class RevenueDAO0 {
  private final Database0 db;

  public RevenueDAO0(Database0 db) {
    this.db = db;
  }

  private static final String BASE =
      " FROM ticket t JOIN invoice i ON i.id=t.invoice_id JOIN showtime s ON s.id=t.showtime_id"
          + " JOIN movie m ON m.id=s.movie_id JOIN room r ON r.id=s.room_id WHERE i.status='PAID'"
          + " AND s.starts_at>=? AND s.starts_at<? ";

  private void bind(PreparedStatement s, LocalDate from, LocalDate to) throws SQLException {
    s.setObject(1, from.atStartOfDay());
    s.setObject(2, to.plusDays(1).atStartOfDay());
  }

  public List<MovieRevenue0> getMovieRevenueStats(LocalDate from, LocalDate to)
      throws SQLException {
    var out = new ArrayList<MovieRevenue0>();
    try (var c = db.open();
        var p =
            c.prepareStatement(
                "SELECT m.id,m.title,COUNT(DISTINCT s.id),COUNT(t.id),SUM(t.unit_price)"
                    + BASE
                    + " GROUP BY m.id,m.title ORDER BY SUM(t.unit_price) DESC,m.id")) {
      bind(p, from, to);
      try (var r = p.executeQuery()) {
        while (r.next())
          out.add(
              new MovieRevenue0(
                  r.getLong(1),
                  r.getString(2),
                  r.getLong(3),
                  r.getLong(4),
                  r.getBigDecimal(5),
                  from,
                  to));
      }
    }
    return out;
  }

  public List<ShowtimeRevenue0> getShowtimeStats(long movieId, LocalDate from, LocalDate to)
      throws SQLException {
    var out = new ArrayList<ShowtimeRevenue0>();
    try (var c = db.open();
        var p =
            c.prepareStatement(
                "SELECT s.id,m.title,r.name,s.starts_at,COUNT(t.id),SUM(t.unit_price)"
                    + BASE
                    + " AND m.id=? GROUP BY s.id,m.title,r.name,s.starts_at ORDER BY"
                    + " s.starts_at,s.id")) {
      bind(p, from, to);
      p.setLong(3, movieId);
      try (var r = p.executeQuery()) {
        while (r.next())
          out.add(
              new ShowtimeRevenue0(
                  r.getLong(1),
                  r.getString(2),
                  r.getString(3),
                  r.getTimestamp(4).toLocalDateTime(),
                  r.getLong(5),
                  r.getBigDecimal(6),
                  from,
                  to));
      }
    }
    return out;
  }

  /** Revenue here is only the tickets of the selected showtime, even for a mixed invoice. */
  public List<InvoiceRevenue0> getInvoicesByShowtime(long showtimeId, LocalDate from, LocalDate to)
      throws SQLException {
    var out = new ArrayList<InvoiceRevenue0>();
    String q =
        "SELECT i.id,i.issued_at,COALESCE(a.full_name,'Khách vãng"
            + " lai'),COUNT(t.id),SUM(t.unit_price) FROM ticket t JOIN invoice i ON"
            + " i.id=t.invoice_id JOIN showtime s ON s.id=t.showtime_id LEFT JOIN customer c ON"
            + " c.id=i.customer_id LEFT JOIN account a ON a.id=c.account_id WHERE i.status='PAID'"
            + " AND s.starts_at>=? AND s.starts_at<? AND s.id=? GROUP BY"
            + " i.id,i.issued_at,a.full_name ORDER BY i.issued_at,i.id";
    try (var c = db.open();
        var p = c.prepareStatement(q)) {
      bind(p, from, to);
      p.setLong(3, showtimeId);
      try (var r = p.executeQuery()) {
        while (r.next())
          out.add(
              new InvoiceRevenue0(
                  r.getLong(1),
                  r.getTimestamp(2).toLocalDateTime(),
                  r.getString(3),
                  r.getLong(4),
                  r.getBigDecimal(5),
                  from,
                  to));
      }
    }
    return out;
  }
}
