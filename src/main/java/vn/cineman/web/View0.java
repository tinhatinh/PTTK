package vn.cineman.web;

import java.text.NumberFormat;
import java.util.Locale;

public final class View0 {
  private View0() {}

  public static String e(Object x) {
    return x == null
        ? ""
        : x.toString()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
  }

  public static String money(Number x) {
    return NumberFormat.getIntegerInstance(Locale.forLanguageTag("vi-VN")).format(x) + " ₫";
  }

  public static String page(String title, String body, boolean manager, String csrf) {
    return "<!doctype html><html lang='vi'><head><meta charset='utf-8'><meta name='viewport'"
               + " content='width=device-width,initial-scale=1'><title>"
        + e(title)
        + " · CineMan</title><link rel='stylesheet' href='/style.css'></head><body><header><a"
        + " class='brand' href='/'>◫ <b>CineMan</b></a><span>Hệ thống quản lý rạp chiếu phim</span>"
        + (manager
            ? "<form method='post' action='/logout'><input type='hidden' name='csrf' value='"
                + e(csrf)
                + "'><button class='text'>Đăng xuất</button></form>"
            : "<a href='/login'>Đăng nhập quản lý</a>")
        + "</header><main>"
        + body
        + "</main><footer>CineMan · Đề 16 · Phan Thành Danh · B23DCAT040</footer></body></html>";
  }

  public static String alert(String s) {
    return s == null ? "" : "<div class='alert' role='alert'>" + e(s) + "</div>";
  }

  public static String title(String eyebrow, String title, String text) {
    return "<p class='eyebrow'>"
        + e(eyebrow)
        + "</p><h1>"
        + e(title)
        + "</h1><p class='muted'>"
        + e(text)
        + "</p>";
  }
}
