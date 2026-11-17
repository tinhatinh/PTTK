package vn.cineman.model;

/**
 * Read model for customer statistics in the system context; no screen/query is implemented for it.
 */
public record CustomerRevenue0(
    long customerId,
    String customerName,
    long ticketCount,
    java.math.BigDecimal revenue,
    java.time.LocalDate fromDate,
    java.time.LocalDate toDate) {}
