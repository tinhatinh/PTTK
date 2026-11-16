package vn.cineman.service;

import java.time.*;

public final class RevenueService0 {
  private RevenueService0() {}

  public static void validateRange(LocalDate from, LocalDate to) {
    if (from == null || to == null || from.isAfter(to) || to.getYear() > 9998 || from.getYear() < 1)
      throw new IllegalArgumentException(
          "Khoảng ngày không hợp lệ. Ngày bắt đầu phải không sau ngày kết thúc.");
  }
}
