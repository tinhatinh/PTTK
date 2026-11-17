# Mô hình Visual Paradigm — CineMan Đề 16

Mở `CineMan_De16_B23DCAT040.vpp` bằng Visual Paradigm 18.1. Đây là project native, có lớp, thuộc tính, phương thức, đối tượng, thông điệp, trạng thái, bảng và khóa ngoại có thể chỉnh sửa. Không cần cài plugin để mở hoặc chỉnh sửa project.

- `export/`: 22 PNG xuất ở tỷ lệ 3× và 22 SVG vector từ chính Visual Paradigm. Báo cáo dùng các PNG này. Watermark Community được giữ nguyên.
- `specs/`: đặc tả dựng hình hoặc snapshot Open API của sơ đồ; bố cục cuối được chỉnh trong project. Project `.vpp` là bản mô hình cuối.
- `validation.json`: kiểm tra chữ ký thành viên lớp, quan hệ và hướng, bội số/hợp thành, phân cấp actor, đầu gửi/nhận thông điệp, cột/FK và đầu nối; không thay thế kiểm tra nghiệp vụ.
- `../uml/`: mã PlantUML tương ứng về nội dung để dán vào các Google Form. Bố cục giữa hai công cụ có thể khác nhau.
- `../docs/evidence/15-visual-paradigm.png`: ảnh chụp ứng dụng Visual Paradigm đang mở lớp thực thể.

## Danh mục 22 sơ đồ

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
| Phân tích | `06a-registration-analysis-sequence` |
| Phân tích | `06b-revenue-analysis-sequence` |
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

## Nội dung pha phân tích

Chín sơ đồ phân tích được dựng theo tiến trình của bài mẫu cùng Đề 16: kịch bản, trích thực thể qua danh từ và quan hệ, trích biên in/out/sub, trạng thái giao diện, kịch bản chi tiết, giao tiếp, tuần tự phân tích. Hai luồng dùng cùng 11 và 23 thông điệp ở kịch bản/giao tiếp/tuần tự. Pha thiết kế chuyển trách nhiệm sang Servlet, service, DAO và các record Java thực tế.

Mô hình toàn hệ thống có Account0, Customer0, Employee0, Movie0, Room0, Seat0, Showtime0, Invoice0, Ticket0 và bốn kiểu thống kê. Employee0 và CustomerRevenue0 là bối cảnh toàn hệ thống; bản chạy chỉ có hai mô-đun đã công bố.

## Quy ước mô hình

Các package `Model_*` tách lớp theo pha và mô-đun, tránh ghi đè trách nhiệm phân tích bằng phương thức thiết kế. Chúng không là package Java triển khai; `14-packages` thể hiện các package source.

Communication diagram chứa các IMessage native thuộc InteractionLifeLineLink. Mỗi thông điệp có một view CommunicationMessage/CommunicationReverseMessage riêng, số và mũi tên do Visual Paradigm vẽ. View được đặt tự do để giữ bố cục sau khi lưu/mở lại, không dùng ký tự mũi tên thay UML. Các đối tượng biên/thực thể tham chiếu classifier phân tích.

ERD có chín bảng, đúng 35 cột và chín ánh xạ FK tới cột snake_case hiện hữu. `schema.sql` là định nghĩa thực thi, gồm UNIQUE/CHECK; các lớp thống kê không có bảng riêng. Bất biến ghế đúng phòng và hóa đơn phải có vé chưa được SQL cưỡng chế; xem README và báo cáo.

## Công cụ và nguồn tham khảo kỹ thuật

Visual Paradigm Community Edition 18.1 build 20261101, Open API chính thức. Tạo/sửa mô hình và xuất ảnh bằng công cụ, không dùng ảnh tạo sinh. Cầu nối API cục bộ dựa trên [visual-paradigm-mcp-plugin](https://github.com/Denmurzik/visual-paradigm-mcp-plugin), có bổ sung xuất PNG 3× và ánh xạ FK. Không cần cầu nối này để đọc project bàn giao.

- [Tạo communication diagram qua Open API](https://knowhow.visual-paradigm.com/openapi/create-communication-diagram/)
- [Xuất ảnh từ plugin](https://knowhow.visual-paradigm.com/openapi/export-diagram-img-plugin/)

Người dùng là actor tổng quát của ba vai trò. Các tham chiếu được giữ trong trường Java thể hiện bằng association một chiều; phụ thuộc sử dụng thể hiện bằng dependency hướng tới lớp được dùng. Các bố cục đã được kiểm tra sau khi lưu/mở lại project.
