package vn.cineman.model;

/** Immutable value carrier; record components are private final. */
public record Customer0(
    long id, long accountId, String phone, String email, java.time.LocalDate registeredAt) {
  /**
   * Membership identifier returned after registration; printing a physical card is a separate use
   * case.
   */
  public String memberCode() {
    return memberCode(id);
  }

  public static String memberCode(long customerId) {
    return "KH" + customerId;
  }
}
