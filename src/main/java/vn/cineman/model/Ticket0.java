package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record Ticket0(
    long id, long invoiceId, long showtimeId, long seatId, java.math.BigDecimal unitPrice) {}
