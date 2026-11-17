package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record MovieRevenue0(
    long movieId,
    String title,
    long showtimeCount,
    long ticketCount,
    java.math.BigDecimal revenue,
    java.time.LocalDate fromDate,
    java.time.LocalDate toDate) {}
