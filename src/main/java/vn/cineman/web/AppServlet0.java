package vn.cineman.web;

import static vn.cineman.web.View0.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import vn.cineman.dao.*;
import vn.cineman.model.*;
import vn.cineman.service.*;

public final class AppServlet0 extends HttpServlet {
  private final RegistrationService0 registration;
  private final AccountDAO0 accounts;
  private final RevenueDAO0 revenue;

  public AppServlet0(Database0 db) {
    registration = new RegistrationService0(new CustomerDAO0(db));
    accounts = new AccountDAO0(db);
    revenue = new RevenueDAO0(db);
  }

  private String csrf(HttpServletRequest q) {
    var s = q.getSession();
    if (s.getAttribute("csrf") == null) s.setAttribute("csrf", UUID.randomUUID().toString());
    return (String) s.getAttribute("csrf");
  }

  private boolean manager(HttpServletRequest q) {
    return "MANAGER".equals(q.getSession().getAttribute("role"));
  }

  private String p(HttpServletRequest q, String key) {
    return Objects.toString(q.getParameter(key), "");
  }

  private void send(HttpServletRequest q, HttpServletResponse r, String title, String body)
      throws IOException {
    r.setContentType("text/html;charset=UTF-8");
    r.getWriter().write(page(title, body, manager(q), csrf(q)));
  }

  private String token(HttpServletRequest q) {
    return "<input type='hidden' name='csrf' value='" + e(csrf(q)) + "'>";
  }

  private String input(String label, String name, String type, String value, String extra) {
    return "<label>"
        + label
        + "<input required name='"
        + name
        + "' type='"
        + type
        + "' value='"
        + e(value)
        + "' "
        + extra
        + "></label>";
  }

  @Override
  protected void service(HttpServletRequest q, HttpServletResponse r)
      throws ServletException, IOException {
    q.setCharacterEncoding("UTF-8");
    r.setHeader("X-Content-Type-Options", "nosniff");
    r.setHeader("Cache-Control", "no-store");
    r.setHeader(
        "Content-Security-Policy",
        "default-src 'self'; style-src 'self'; form-action 'self'; frame-ancestors 'none'");
    try {
      if (q.getRequestURI().startsWith("/manager") && !manager(q)) {
        r.sendRedirect("/login");
        return;
      }
      if (q.getMethod().equals("POST") && !csrf(q).equals(p(q, "csrf"))) {
        r.setStatus(403);
        send(
            q,
            r,
            "Yêu cầu không hợp lệ",
            alert("Phiên biểu mẫu không hợp lệ. Vui lòng tải lại trang."));
        return;
      }
      super.service(q, r);
    } catch (IllegalArgumentException ex) {
      r.setStatus(400);
      send(
          q,
          r,
          "Dữ liệu không hợp lệ",
          title("Kiểm tra dữ liệu", "Không thể xử lý yêu cầu", ex.getMessage())
              + "<a class='button' href='/manager/reports'>Chọn lại khoảng ngày</a>");
    } catch (Exception ex) {
      getServletContext().log("Request failed", ex);
      r.setStatus(500);
      send(q, r, "Lỗi xử lý", alert("Không thể xử lý lúc này. Vui lòng thử lại."));
    }
  }

  @Override
  protected void doGet(HttpServletRequest q, HttpServletResponse r)
      throws IOException, ServletException {
    String path = q.getRequestURI();
    if (path.equals("/style.css")) {
      r.setContentType("text/css;charset=UTF-8");
      try (var in = getClass().getResourceAsStream("/style.css")) {
        in.transferTo(r.getOutputStream());
      }
      return;
    }
    if (path.equals("/")) {
      send(
          q,
          r,
          "Trang chủ",
          title(
                  "CINEMAN",
                  "Quản lý rạp chiếu phim",
                  "Đăng ký thành viên và tra cứu thống kê doanh thu phim.")
              + "<div class='grid'><section class='card'><span class='step'>01</span><h2>Dành cho"
              + " khách hàng</h2><p>Tạo tài khoản thành viên bằng thông tin cá nhân của bạn.</p><a"
              + " class='button' href='/register'>Đăng ký thành viên →</a></section><section"
              + " class='card'><span class='step'>02</span><h2>Dành cho quản lý</h2><p>Xem doanh"
              + " thu phim, suất chiếu và hóa đơn bán vé.</p><a class='button secondary'"
              + " href='/login'>Đăng nhập quản lý →</a></section></div>");
      return;
    }
    if (path.equals("/register")) {
      registerPage(q, r, null);
      return;
    }
    if (path.equals("/registered")) {
      Object id = q.getSession().getAttribute("registeredId");
      if (id == null) {
        r.sendRedirect("/register");
        return;
      }
      send(
          q,
          r,
          "Đăng ký thành công",
          title("THÀNH VIÊN", "Đăng ký thành công", "Thông tin của bạn đã được lưu vào hệ thống.")
              + "<section class='card narrow'><div class='success'>✓</div><h2>Chào mừng "
              + e(q.getSession().getAttribute("registeredName"))
              + "</h2><p>Mã thành viên <strong>"
              + e(Customer0.memberCode(((Number) id).longValue()))
              + "</strong></p><a class='button' href='/'>Về trang chủ</a></section>");
      return;
    }
    if (path.equals("/login")) {
      loginPage(q, r, null);
      return;
    }
    if (path.equals("/manager")) {
      send(
          q,
          r,
          "Trang quản lý",
          title("QUẢN LÝ", "Xin chào, quản lý", "Chọn chức năng để tiếp tục.")
              + "<section class='card narrow'><h2>Báo cáo thống kê</h2><p>Tra cứu doanh thu và xem"
              + " các hóa đơn liên quan.</p><a class='button' href='/manager/report-type'>Xem báo"
              + " cáo →</a></section>");
      return;
    }
    if (path.equals("/manager/report-type")) {
      send(
          q,
          r,
          "Chọn báo cáo",
          title("BÁO CÁO", "Chọn loại thống kê", "Chức năng trong phạm vi Đề 16.")
              + "<section class='card narrow'><h2>Thống kê phim theo doanh thu</h2><p>Tổng hợp theo"
              + " phim và xem chi tiết đến từng suất chiếu.</p><a class='button'"
              + " href='/manager/reports'>Tiếp tục →</a></section><a class='back' href='/manager'>←"
              + " Trang quản lý</a>");
      return;
    }
    if (path.equals("/manager/reports")) {
      reportPage(q, r);
      return;
    }
    if (path.equals("/manager/showtimes")) {
      showtimesPage(q, r);
      return;
    }
    if (path.equals("/manager/invoices")) {
      invoicesPage(q, r);
      return;
    }
    r.setStatus(404);
    send(q, r, "Không tìm thấy", title("404", "Không tìm thấy trang", "Đường dẫn không tồn tại."));
  }

  private void registerPage(HttpServletRequest q, HttpServletResponse r, String error)
      throws IOException {
    String body =
        title("THÀNH VIÊN", "Đăng ký thành viên", "Nhập thông tin cá nhân và tài khoản để đăng ký.")
            + "<section class='card form-card'>"
            + alert(error)
            + "<form method='post' action='/register'>"
            + token(q)
            + "<div class='grid fields'>"
            + input(
                "Họ và tên",
                "fullName",
                "text",
                p(q, "fullName"),
                "minlength='2' maxlength='100' autocomplete='name'")
            + input(
                "Số điện thoại",
                "phone",
                "tel",
                p(q, "phone"),
                "pattern='0[0-9]{9}' maxlength='10' placeholder='0901234567' autocomplete='tel'")
            + input(
                "Email", "email", "email", p(q, "email"), "maxlength='120' autocomplete='email'")
            + input(
                "Tên đăng nhập",
                "username",
                "text",
                p(q, "username"),
                "pattern='[a-zA-Z0-9_]{4,40}' minlength='4' maxlength='40' autocomplete='username'")
            + input(
                "Mật khẩu",
                "password",
                "password",
                "",
                "minlength='8' maxlength='128' autocomplete='new-password'")
            + input(
                "Nhập lại mật khẩu",
                "confirmPassword",
                "password",
                "",
                "minlength='8' maxlength='128' autocomplete='new-password'")
            + "</div><p class='muted small'>Tên đăng nhập từ 4–40 ký tự chữ, số hoặc gạch dưới. Mật"
            + " khẩu từ 8–128 ký tự.</p><div class='actions'><button>Đăng ký</button><a href='/'>Về"
            + " trang chủ</a></div></form></section>";
    send(q, r, "Đăng ký thành viên", body);
  }

  private void loginPage(HttpServletRequest q, HttpServletResponse r, String error)
      throws IOException {
    send(
        q,
        r,
        "Đăng nhập quản lý",
        title("QUẢN LÝ", "Đăng nhập quản lý", "Sử dụng tài khoản có quyền quản lý để xem báo cáo.")
            + "<section class='card narrow'>"
            + alert(error)
            + "<form method='post' action='/login'>"
            + token(q)
            + input(
                "Tên đăng nhập", "username", "text", p(q, "username"), "autocomplete='username'")
            + input("Mật khẩu", "password", "password", "", "autocomplete='current-password'")
            + "<button>Đăng nhập</button></form></section>");
  }

  @Override
  protected void doPost(HttpServletRequest q, HttpServletResponse r)
      throws IOException, ServletException {
    switch (q.getRequestURI()) {
      case "/register" -> {
        var x =
            new Registration0(
                p(q, "fullName"),
                p(q, "phone"),
                p(q, "email"),
                p(q, "username"),
                p(q, "password"),
                p(q, "confirmPassword"));
        try {
          long id = registration.register(x);
          q.getSession().setAttribute("registeredId", id);
          q.getSession().setAttribute("registeredName", x.fullName().trim());
          r.setStatus(303);
          r.setHeader("Location", "/registered");
        } catch (IllegalArgumentException ex) {
          r.setStatus(400);
          registerPage(q, r, ex.getMessage());
        } catch (SQLException ex) {
          r.setStatus("23505".equals(ex.getSQLState()) ? 409 : 500);
          if (!"23505".equals(ex.getSQLState())) getServletContext().log("Registration failed", ex);
          registerPage(
              q,
              r,
              "23505".equals(ex.getSQLState())
                  ? "Tên đăng nhập, email hoặc số điện thoại đã được sử dụng."
                  : "Không lưu được thông tin. Vui lòng thử lại.");
        }
      }
      case "/login" -> {
        try {
          var a = accounts.findByUsername(p(q, "username").trim().toLowerCase(Locale.ROOT));
          if (a != null
              && a.role().equals("MANAGER")
              && Password0.verify(p(q, "password"), a.passwordHash())) {
            q.changeSessionId();
            q.getSession().setAttribute("role", "MANAGER");
            q.getSession().setAttribute("csrf", UUID.randomUUID().toString());
            r.sendRedirect("/manager");
          } else {
            r.setStatus(401);
            loginPage(q, r, "Tài khoản hoặc mật khẩu không đúng hoặc không có quyền quản lý.");
          }
        } catch (SQLException ex) {
          throw new ServletException(ex);
        }
      }
      case "/logout" -> {
        q.getSession().invalidate();
        r.sendRedirect("/");
      }
      default -> {
        r.setStatus(404);
        send(q, r, "Không tìm thấy", alert("Đường dẫn không tồn tại."));
      }
    }
  }

  private LocalDate[] range(HttpServletRequest q) {
    try {
      var f = LocalDate.parse(p(q, "from"));
      var t = LocalDate.parse(p(q, "to"));
      RevenueService0.validateRange(f, t);
      return new LocalDate[] {f, t};
    } catch (DateTimeException ex) {
      throw new IllegalArgumentException("Vui lòng nhập đủ hai ngày hợp lệ.");
    }
  }

  private String query(LocalDate[] d) {
    return "from=" + d[0] + "&to=" + d[1];
  }

  private long id(HttpServletRequest q, String name) {
    try {
      long id = Long.parseLong(p(q, name));
      if (id < 1) throw new NumberFormatException();
      return id;
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Mã đối tượng không hợp lệ.");
    }
  }

  private String dates(LocalDate[] d) {
    return d[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        + " – "
        + d[1].format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  private String policy() {
    return "<p class='muted small'>Tính theo ngày bắt đầu suất chiếu, gồm cả hai ngày biên; chỉ"
               + " cộng tiền vé thuộc hóa đơn PAID. Dữ liệu minh họa phục vụ bài tập.</p>";
  }

  private void reportPage(HttpServletRequest q, HttpServletResponse r)
      throws IOException, ServletException {
    String form =
        "<section class='card'><form class='filter' method='get' action='/manager/reports'>"
            + input("Từ ngày", "from", "date", p(q, "from"), "")
            + input("Đến ngày", "to", "date", p(q, "to"), "")
            + "<button>Xem thống kê</button></form></section>";
    String body =
        title(
                "BÁO CÁO DOANH THU",
                "Thống kê phim theo doanh thu",
                "Chọn khoảng ngày để tổng hợp tiền vé của các suất chiếu.")
            + form;
    if (q.getParameter("from") != null || q.getParameter("to") != null) {
      var d = range(q);
      try {
        var rows = revenue.getMovieRevenueStats(d[0], d[1]);
        BigDecimal sum = BigDecimal.ZERO;
        long tickets = 0;
        for (var x : rows) {
          sum = sum.add(x.revenue());
          tickets += x.ticketCount();
        }
        body +=
            "<div class='metrics'><section><span>Doanh thu</span><strong>"
                + money(sum)
                + "</strong></section><section><span>Vé đã bán</span><strong>"
                + tickets
                + "</strong></section><section><span>Phim có doanh thu</span><strong>"
                + rows.size()
                + "</strong></section></div><section class='card'><h2>Kết quả theo phim</h2><p"
                + " class='muted'>"
                + dates(d)
                + "</p><table><thead><tr><th>Phim</th><th>Suất có vé</th><th>Số vé</th><th>Doanh"
                + " thu</th><th></th></tr></thead><tbody>";
        for (var x : rows)
          body +=
              "<tr><td><b>"
                  + e(x.title())
                  + "</b></td><td>"
                  + x.showtimeCount()
                  + "</td><td>"
                  + x.ticketCount()
                  + "</td><td>"
                  + money(x.revenue())
                  + "</td><td><a href='/manager/showtimes?movieId="
                  + x.movieId()
                  + "&"
                  + query(d)
                  + "'>Xem suất chiếu →</a></td></tr>";
        if (rows.isEmpty())
          body +=
              "<tr><td colspan='5' class='empty'>Không có doanh thu trong khoảng ngày đã"
                  + " chọn.</td></tr>";
        body += "</tbody></table>" + policy() + "</section>";
      } catch (SQLException ex) {
        throw new ServletException(ex);
      }
    }
    send(
        q,
        r,
        "Thống kê phim",
        body + "<a class='back' href='/manager/report-type'>← Chọn loại thống kê</a>");
  }

  private void showtimesPage(HttpServletRequest q, HttpServletResponse r)
      throws IOException, ServletException {
    var d = range(q);
    long movieId = id(q, "movieId");
    try {
      var rows = revenue.getShowtimeStats(movieId, d[0], d[1]);
      String body =
          title(
                  "PHIM → SUẤT CHIẾU",
                  rows.isEmpty() ? "Không có suất chiếu" : rows.get(0).movieTitle(),
                  dates(d))
              + "<section class='card'><h2>Các suất chiếu có doanh"
              + " thu</h2><table><thead><tr><th>Suất chiếu</th><th>Phòng</th><th>Bắt đầu</th><th>Số"
              + " vé</th><th>Doanh thu</th><th></th></tr></thead><tbody>";
      BigDecimal sum = BigDecimal.ZERO;
      for (var x : rows) {
        sum = sum.add(x.revenue());
        body +=
            "<tr><td>SC"
                + x.showtimeId()
                + "</td><td>"
                + e(x.roomName())
                + "</td><td>"
                + x.startsAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                + "</td><td>"
                + x.ticketCount()
                + "</td><td>"
                + money(x.revenue())
                + "</td><td><a href='/manager/invoices?showtimeId="
                + x.showtimeId()
                + "&movieId="
                + movieId
                + "&"
                + query(d)
                + "'>Xem hóa đơn →</a></td></tr>";
      }
      if (rows.isEmpty())
        body += "<tr><td colspan='6' class='empty'>Không có dữ liệu phù hợp.</td></tr>";
      body +=
          "</tbody></table><p class='total'>Tổng doanh thu phim <b>"
              + money(sum)
              + "</b></p>"
              + policy()
              + "</section><a class='back' href='/manager/reports?"
              + query(d)
              + "'>← Thống kê phim</a>";
      send(q, r, "Thống kê suất chiếu", body);
    } catch (SQLException ex) {
      throw new ServletException(ex);
    }
  }

  private void invoicesPage(HttpServletRequest q, HttpServletResponse r)
      throws IOException, ServletException {
    var d = range(q);
    long showtimeId = id(q, "showtimeId"), movieId = id(q, "movieId");
    try {
      if (revenue.getShowtimeStats(movieId, d[0], d[1]).stream()
          .noneMatch(x -> x.showtimeId() == showtimeId))
        throw new IllegalArgumentException("Suất chiếu không thuộc phim và khoảng ngày đã chọn.");
      var rows = revenue.getInvoicesByShowtime(showtimeId, d[0], d[1]);
      String body =
          title("PHIM → SUẤT CHIẾU → HÓA ĐƠN", "Hóa đơn của suất SC" + showtimeId, dates(d))
              + "<section class='card'><h2>Hóa đơn bán vé đã thanh"
              + " toán</h2><table><thead><tr><th>Hóa đơn</th><th>Thời điểm lập</th><th>Khách"
              + " hàng</th><th>Số vé của suất</th><th>Tiền vé của suất</th></tr></thead><tbody>";
      BigDecimal sum = BigDecimal.ZERO;
      for (var x : rows) {
        sum = sum.add(x.revenue());
        body +=
            "<tr><td><b>HD"
                + x.invoiceId()
                + "</b></td><td>"
                + x.issuedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                + "</td><td>"
                + e(x.customerName())
                + "</td><td>"
                + x.ticketCount()
                + "</td><td>"
                + money(x.revenue())
                + "</td></tr>";
      }
      body +=
          "</tbody></table><p class='total'>Tổng doanh thu suất chiếu <b>"
              + money(sum)
              + "</b></p>"
              + policy()
              + "</section><a class='back' href='/manager/showtimes?movieId="
              + movieId
              + "&"
              + query(d)
              + "'>← Các suất chiếu</a>";
      send(q, r, "Hóa đơn bán vé", body);
    } catch (SQLException ex) {
      throw new ServletException(ex);
    }
  }
}
