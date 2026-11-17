# CineMan - Đề 16

Bài tập lớn Phân tích và thiết kế hệ thống thông tin.
**Phan Thành Danh · B23DCAT040 · Lớp 09**

Ứng dụng Java web gồm hai mô-đun:

- **Đăng ký thành viên:** kiểm tra thông tin, lưu tài khoản và hồ sơ trong một giao dịch, hiển thị mã thành viên.
- **Thống kê phim theo doanh thu:** quản lý chọn khoảng ngày, xem phim → suất chiếu → hóa đơn.

Đăng nhập quản lý hỗ trợ việc truy cập báo cáo. Các chức năng khác trong mô hình tổng quan chưa được cài đặt; vé và hóa đơn được cung cấp bằng dữ liệu mẫu.

## Chạy ứng dụng

Cài **JDK 17 trở lên**, bảo đảm `java` và `javac` có trong PATH. Thư viện cần thiết có sẵn trong `lib/`.

Linux/macOS:

```bash
bash scripts/run.sh
```

Windows PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/run.ps1
```

Mở **http://127.0.0.1:8080/**. Tài khoản quản lý mẫu: `manager` / `Manager@123`. Dừng bằng Ctrl+C. Trên Linux/macOS có thể đổi cổng bằng `PORT=8081 bash scripts/run.sh`.

H2 tự tạo `data/cineman.mv.db` ở lần chạy đầu. Dữ liệu đăng ký được giữ qua các lần khởi động. Muốn khởi tạo lại dữ liệu mẫu, dừng ứng dụng rồi chuyển thư mục `data/` sang nơi dự phòng trước khi chạy lại.

## Cấu trúc

```text
src/main/java/vn/cineman/
  Main0.java          Khởi tạo Tomcat và kết nối các lớp
  web/                Servlet điều phối và xuất HTML
  service/            Kiểm tra dữ liệu và xử lý mật khẩu
  dao/                Giao dịch JDBC và truy vấn thống kê
  model/              Các record thực thể và kết quả
src/main/resources/   Schema, dữ liệu mẫu và CSS
src/test/java/        Kiểm thử Java với H2 trong bộ nhớ
scripts/              Build, chạy và kiểm thử
lib/                  Thư viện chạy và mã SHA-256
models/               Project Visual Paradigm
uml/                  Mã PlantUML theo từng pha
docs/report/          Báo cáo Word và PDF
```

Tomcat 10.1.61 sử dụng Jakarta Servlet 6.0; H2 2.3.232 chạy nhúng. Servlet, service và DAO cùng chạy trong một JVM. Giao diện được xuất bằng Java, không dùng JSP.

## Quy tắc dữ liệu

Tên đăng nhập, email và điện thoại là duy nhất; tên đăng nhập/email được chuyển chữ thường. Mật khẩu dùng PBKDF2-HMAC-SHA256 với salt ngẫu nhiên. Đăng ký lưu tài khoản và hồ sơ cùng giao dịch; lỗi ghi sẽ rollback.

Doanh thu chỉ tính vé thuộc hóa đơn `PAID`, lọc theo **ngày bắt đầu suất chiếu**, lấy cả ngày đầu và ngày cuối. Tiền dùng `BigDecimal` và `DECIMAL(14,0)`. Ở cấp hóa đơn, mỗi dòng chỉ cộng vé của suất đang xem. Phim/suất không có vé đã thanh toán không xuất hiện.

Với dữ liệu mẫu từ 01/11/2026 đến 30/11/2026: Hành trình bình minh đạt 480.000 đồng, 6 vé, 3 suất; Thành phố mùa hạ đạt 270.000 đồng, 3 vé, 1 suất. Tổng cộng 750.000 đồng và 9 vé.

Schema có khóa ngoại, UNIQUE và CHECK. Điều kiện ghế thuộc phòng của suất và hóa đơn có ít nhất một vé được bảo đảm trong dữ liệu mẫu; chưa có chức năng tạo vé/hóa đơn để xử lý các điều kiện này.

## Kiểm thử

```bash
bash scripts/test.sh
```

Bộ kiểm thử gồm 29 điều kiện về đăng ký, giao dịch, truy vấn và tính doanh thu. Mỗi lần chạy dùng CSDL riêng trong bộ nhớ.

## Tài liệu

- [Báo cáo PDF](docs/report/16B23DCAT040.pdf)
- [Báo cáo Word](docs/report/16B23DCAT040.docx)
- [Project Visual Paradigm](models/CineMan_De16_B23DCAT040.vpp) gồm 22 sơ đồ có thể chỉnh sửa.
- [PlantUML](uml/) tương ứng với các sơ đồ trong báo cáo.

Ảnh UML trong báo cáo được xuất từ Visual Paradigm. Ảnh giao diện được chụp từ ứng dụng chạy thực tế.
