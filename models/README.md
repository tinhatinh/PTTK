# Mô hình Visual Paradigm — CineMan Đề 16

Mở `CineMan_De16_B23DCAT040.vpp` bằng Visual Paradigm 18.1. Đây là project native, có lớp, thuộc tính, phương thức, đối tượng, thông điệp, trạng thái, bảng và khóa ngoại có thể chỉnh sửa. Không cần cài plugin để mở hoặc chỉnh sửa project.

- `export/`: 20 PNG xuất ở tỷ lệ 3× và 20 SVG vector từ chính Visual Paradigm. Báo cáo dùng các PNG này. Watermark Community được giữ nguyên.
- `specs/`: dữ liệu mô tả đầu vào khi tạo sơ đồ bằng Open API; bố cục cuối được chỉnh trong project. Project `.vpp` là bản mô hình cuối.
- `validation.json`: phạm vi kiểm tra thành viên lớp, số bảng/FK và kích thước ảnh; không thay thế kiểm tra nghiệp vụ.
- `../uml/`: mã PlantUML tương ứng về nội dung để dán vào các Google Form. Bố cục giữa hai công cụ có thể khác nhau.
- `../docs/evidence/15-visual-paradigm.png`: ảnh chụp ứng dụng Visual Paradigm đang mở lớp thực thể.

## Danh mục 20 sơ đồ

| Pha | Tệp (bỏ phần mở rộng) |
|---|---|
| Đặc tả | `00-system-overview` |
| Đặc tả | `01a-registration-use-case` |
| Đặc tả | `01b-revenue-use-case` |
| Phân tích | `02-domain` |
| Phân tích | `03-registration-analysis` |
| Phân tích | `04-revenue-analysis` |
| Phân tích | `04a-registration-states` |
| Phân tích | `04b-revenue-states` |
| Phân tích | `05-registration-communication` |
| Phân tích | `06-revenue-communication` |
| Thiết kế | `07-registration-design` |
| Thiết kế | `07a-entities-design` |
| Thiết kế | `08-revenue-design` |
| Thiết kế | `09-registration-activity` |
| Thiết kế | `10-revenue-activity` |
| Thiết kế | `11-registration-sequence` |
| Thiết kế | `12-revenue-sequence` |
| Thiết kế | `13-database` |
| Thiết kế | `14-packages` |
| Thiết kế | `15-deployment` |

## Quy ước mô hình

Các package `Scope_*` tổ chức các phiên bản lớp theo pha để tên lớp phân tích không bị ghi đè bởi lớp thiết kế. Chúng không phải package Java triển khai. Sơ đồ `14-packages` thể hiện các package Java thực tế.

Communication diagram chứa các `IMessage` native thuộc các `InteractionLifeLineLink`. Để tránh nhãn chồng nhau, thông điệp được trình bày gộp trên mỗi liên kết, có số thứ tự và mũi tên hướng gửi/trả. Các message vẫn có trong mô hình liên kết. Đối tượng biên/thực thể tham chiếu classifier của lớp phân tích.

ERD có tám bảng và tám ánh xạ khóa ngoại tới đúng cột snake_case hiện hữu. `schema.sql` là định nghĩa thực thi, gồm cả UNIQUE nhiều cột và CHECK được ghi chú trong ERD. Không tuyên bố project VP đã tự sinh hoặc chạy toàn bộ DDL. Bất biến ghế đúng phòng và hóa đơn phải có vé chưa được SQL cưỡng chế; xem README và báo cáo.

## Công cụ và nguồn tham khảo kỹ thuật

Visual Paradigm Community Edition 18.1 build 20261101, Open API chính thức. Tạo/sửa mô hình và xuất ảnh bằng công cụ, không dùng ảnh tạo sinh. Cầu nối API cục bộ dựa trên [visual-paradigm-mcp-plugin](https://github.com/Denmurzik/visual-paradigm-mcp-plugin), có bổ sung xuất PNG 3× và ánh xạ FK. Không cần cầu nối này để đọc project bàn giao.

- [Tạo communication diagram qua Open API](https://knowhow.visual-paradigm.com/openapi/create-communication-diagram/)
- [Xuất ảnh từ plugin](https://knowhow.visual-paradigm.com/openapi/export-diagram-img-plugin/)
