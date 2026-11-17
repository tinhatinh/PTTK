# CineMan — Đề 16

**Phan Thành Danh — B23DCAT040 — lớp 09.** Bản hoàn thiện cho Đề 16.

Project thực hành học phần Phân tích và thiết kế hệ thống thông tin. Hai mô-đun được cài đặt:

1. Khách hàng đăng ký thành viên: nhập thông tin → lưu CSDL → thông báo thành công.
2. Quản lý xem thống kê phim theo doanh thu: chọn báo cáo → khoảng ngày → phim → suất chiếu → hóa đơn.

Đăng nhập quản lý là chức năng hỗ trợ tiền điều kiện. Các chức năng bối cảnh như bán vé, thanh toán trực tuyến, quản lý phim/phòng, xuất thẻ không được cài đặt. Dữ liệu bán vé là dữ liệu mẫu trong `seed.sql`.

## Chạy trên Linux hoặc macOS

Cần **JDK 17 trở lên** (`java` và `javac` trong PATH). Các JAR phụ thuộc đã có trong `lib/`; không cần cài Maven hay tải thêm thư viện để chạy bản ZIP.

```bash
bash scripts/run.sh
```

Mở http://127.0.0.1:8080/ . Tài khoản quản lý mẫu: `manager` / `Manager@123`. Máy chủ chỉ lắng nghe địa chỉ loopback. Dừng bằng Ctrl+C. Cổng khác: `PORT=8081 bash scripts/run.sh`.

## Chạy trên Windows PowerShell

```powershell
powershell -ExecutionPolicy Bypass -File scripts/run.ps1
```

CSDL H2 được tạo trong `data/cineman.mv.db` ở lần chạy đầu. Đăng ký mới được lưu qua các lần khởi động. Để tạo lại dữ liệu minh họa, dừng ứng dụng và **di chuyển thư mục `data/` sang một thư mục dự phòng** trước khi chạy lại. Không ghi đè dữ liệu cần giữ.

## Quy tắc được lựa chọn cho bản cài đặt

Đề chưa quy định bộ trường đăng ký hoặc cách xác định thời gian doanh thu. Bản này chọn họ tên, điện thoại, email, tên đăng nhập và mật khẩu; điện thoại/email/tên đăng nhập là duy nhất. Username và email chuyển chữ thường. Tên người trùng nhau được phép. Không thêm màn hình xác nhận, in hóa đơn hoặc ưu đãi thành viên.

Doanh thu lọc theo **ngày bắt đầu suất chiếu**, từ 00:00 ngày bắt đầu đến trước 00:00 ngày kế tiếp ngày kết thúc. Chỉ tính vé trong hóa đơn `PAID`. Cột tiền vé ở cấp hóa đơn chỉ cộng vé của suất được chọn; tránh cộng cả hóa đơn khi hóa đơn có nhiều suất. Tiền dùng `DECIMAL(14,0)` và `BigDecimal`. Phim/suất không có vé PAID không xuất hiện trong kết quả.

Khoảng 01/11/2026–30/11/2026: phim Hành trình bình minh 480.000 đồng/6 vé/3 suất; Thành phố mùa hạ 270.000 đồng/3 vé/1 suất; tổng 750.000 đồng/9 vé. Mọi tên phim, hóa đơn, vé và người dùng mẫu đều do project tạo để kiểm thử.

Hồ sơ nhân viên Employee0 và kết quả CustomerRevenue0 mô tả bối cảnh toàn hệ thống; chưa có màn hình quản lý nhân viên hoặc thống kê khách hàng. Schema có bảng employee và dữ liệu mẫu NV001 liên kết với quản lý. Mã thành viên KH<id> được suy ra từ Customer0, không lưu thêm cột trùng khóa. Ba kết quả thống kê đang dùng đều giữ fromDate/toDate.

## Kiến trúc và cây thư mục

- `src/main/java/vn/cineman/web`: Servlet điều phối HTTP và lớp xuất HTML; không dùng JSP.
- `service`: kiểm tra đăng ký/khoảng ngày và băm mật khẩu.
- `dao`: JDBC, giao dịch đăng ký, truy vấn tổng hợp.
- `model`: 9 record thực thể và 5 record dữ liệu truyền/đọc kết quả.
- `src/main/resources`: schema, seed và CSS được ứng dụng dùng thật.
- `src/test/java`: kiểm tra chức năng bằng Java, CSDL riêng trong bộ nhớ.
- `uml`: mã PlantUML tương ứng, chia theo ba giai đoạn; ảnh xuất Visual Paradigm ở `models/export`.
- `docs/evidence`: ảnh chụp ứng dụng/VS Code và log kiểm tra thực tế.

Tomcat 10.1.61 triển khai Jakarta Servlet 6.0. Ba tầng logic chạy trong cùng JVM ở bản demo; H2 là CSDL nhúng, không có máy chủ CSDL rời. `Database0` được truyền vào DAO qua constructor; mỗi thao tác mở/đóng kết nối riêng bằng try-with-resources. Không giữ kết nối dùng chung trong servlet.

## Kiểm tra

```bash
bash scripts/test.sh
```

Kết quả đã chạy: **29 kiểm tra Java PASS**. `docs/evidence/java-tests.txt` ghi kết quả. Chương trình không cần JUnit; ném `AssertionError` nếu điều kiện sai và trả mã lỗi.

Kiểm tra trình duyệt và chụp ảnh bằng Playwright (13 kiểm tra): bắt đầu từ CSDL dữ liệu mẫu chưa đăng ký `nguyenan`, chạy app ở cổng 8080; cung cấp module Playwright và Chromium trên máy:

```bash
PLAYWRIGHT_MODULE=/absolute/path/to/playwright CHROMIUM_PATH=/usr/bin/chromium node scripts/browser-check.cjs
```

Script đăng ký một người dùng minh họa và cập nhật ảnh trong `docs/evidence/`. Đây là script kiểm tra, không nằm trong luồng ứng dụng. Ảnh editor `14-project-editor.png` được chụp VS Code for the Web qua code-server mở đúng project.

## PlantUML và biểu mẫu

Mỗi `.puml` là một sơ đồ độc lập, đã kiểm tra cú pháp bằng PlantUML 1.2025.2. Ảnh trong báo cáo xuất trực tiếp từ project Visual Paradigm. Không dùng công cụ tạo ảnh AI. Xem `docs/FORM_MAPPING.md` để biết dán file nào vào từng trường Google Form. Không dán link thay cho code. Tự chấm điểm do sinh viên quyết định; các form chưa được gửi thay sinh viên.

## Giới hạn được công bố

Bản chạy là demo cục bộ của hai mô-đun, chưa triển khai Internet. Chưa đo tải, chưa kiểm tra đa trình duyệt, chưa thử phục hồi khi tiến trình bị tắt giữa giao dịch. Schema có khóa ngoại/duy nhất/miền giá trị; ràng buộc ghế thuộc phòng của suất và hóa đơn phải có ít nhất một vé không được DB cưỡng chế, vì luồng tạo vé/hóa đơn nằm ngoài phạm vi. Bộ seed được đối chiếu theo các quan hệ này. Không tuyên bố đã kiểm thử toàn bộ hệ thống CineMan.

Mật khẩu dùng PBKDF2-HMAC-SHA256, salt ngẫu nhiên, 120.000 vòng. Có CSRF, escape HTML, PreparedStatement và kiểm tra quyền server cho tuyến quản lý. Những cơ chế này hỗ trợ demo; không dùng tài khoản/mật khẩu mẫu cho môi trường công khai.

## Tài liệu và quyền truy cập

Repository: https://github.com/tinhatinh/PTTK . Đây là repository công khai để giảng viên đọc source. Bản báo cáo nằm trong `docs/report/` sau khi hoàn thiện. Bản ZIP chứa source, thư viện chạy, sơ đồ, ảnh và kết quả kiểm tra; không chứa CSDL sử dụng, token hoặc thư mục build.

## Project Visual Paradigm

Mở `models/CineMan_De16_B23DCAT040.vpp` để chỉnh sửa 22 sơ đồ. PNG 3× và SVG vector xuất trực tiếp từ Visual Paradigm ở `models/export`; mã PlantUML nộp form ở `uml`. Xem `models/README.md` để đối chiếu.

## Báo cáo cuối

- [Báo cáo PDF](docs/report/16B23DCAT040.pdf)
- [Báo cáo Word](docs/report/16B23DCAT040.docx)
- [Danh mục bàn giao](docs/HANDOFF.md)
