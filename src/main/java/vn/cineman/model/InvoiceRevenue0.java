package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record InvoiceRevenue0(
    long invoiceId,
    java.time.LocalDateTime issuedAt,
    String customerName,
    long ticketCount,
    java.math.BigDecimal revenue,
    java.time.LocalDate fromDate,
    java.time.LocalDate toDate) {}
