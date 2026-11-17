# Bố cục giao diện từ ứng dụng CineMan

Các ảnh trong `screens/` được chụp từ chính HTML ứng dụng Java đang chạy. CSS chỉ đổi màu chữ, nền và đường viền sang đơn sắc. Script so sánh văn bản, thuộc tính điều khiển và toàn bộ bounding box trước/sau khi áp CSS; chỉ cắt khoảng trắng dưới nội dung trang. Không dựng lại bảng hoặc biểu mẫu bằng HTML riêng.

`capture-manifest.json` ghi route, cột bảng, dòng dữ liệu, thành phần giao diện, hash ảnh và hash source. `capture-checks.txt` ghi 23 kiểm tra thực thi. Báo cáo dùng bảy ảnh bố cục: đăng ký, chọn loại báo cáo, khoảng ngày, phim, suất, hóa đơn và lỗi ngày. Thư mục giữ thêm các trạng thái trang chủ, đăng ký thành công, trùng, đăng nhập, quản lý và kết quả rỗng để đối chiếu.

Để tạo lại, chạy ứng dụng trên một CSDL mẫu mới chưa có thành viên `nguyenan`, rồi chạy:

```bash
PLAYWRIGHT_MODULE=/absolute/path/to/playwright \
CINEMAN_BASE_URL=http://127.0.0.1:8080 \
node scripts/capture-ui-design.cjs
```

Script đăng ký thành viên mẫu, xác thực quản lý và cập nhật cả ảnh màu trong `docs/evidence/`. Có thể đổi thư mục bằng `CINEMAN_CAPTURE_DIR` và `CINEMAN_EVIDENCE_DIR`.

Ngày sai trả trang lỗi HTTP 400 có liên kết nhập lại. Kỳ hợp lệ nhưng không có doanh thu vẫn hiển thị bộ lọc, ba chỉ số bằng 0 và một dòng thông báo trong bảng. Màn chọn loại là thẻ nội dung và liên kết Tiếp tục; không có điều khiển chọn loại.
