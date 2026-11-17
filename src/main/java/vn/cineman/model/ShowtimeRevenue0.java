package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record ShowtimeRevenue0(
    long showtimeId,
    String movieTitle,
    String roomName,
    java.time.LocalDateTime startsAt,
    long ticketCount,
    java.math.BigDecimal revenue,
    java.time.LocalDate fromDate,
    java.time.LocalDate toDate) {}
