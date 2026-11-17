# Bàn giao CineMan Đề 16

Phan Thành Danh — B23DCAT040 — lớp 09.

- Báo cáo cuối: `docs/report/16B23DCAT040.docx` và `.pdf`.
- Mã nguồn và lệnh chạy: `README.md`; Java 17 trở lên.
- Project Visual Paradigm sửa được: `models/CineMan_De16_B23DCAT040.vpp`.
- 22 PNG xuất 3× và 22 SVG vector: `models/export`.
- 22 tệp PlantUML và ba tệp tổng hợp theo pha: `uml`.
- Ánh xạ trường Google Form: `docs/FORM_MAPPING.md`.
- Bản khung giao diện có HTML nguồn: `docs/ui-design`.
- Ảnh ứng dụng, editor, Visual Paradigm và log thực thi: `docs/evidence`.

Bố cục theo bốn pha của bài mẫu LibMan, bổ sung đối chiếu bài CineMan B21DCCN748. Pha phân tích có đủ bảy mục: kịch bản; trích lớp toàn hệ thống; lớp mô-đun; trạng thái; kịch bản chi tiết; giao tiếp; tuần tự phân tích. Pha thiết kế có thực thể, CSDL, lớp mô-đun, giao diện, activity, sequence, package và triển khai. Không có chương Kiểm tra và đánh giá kết quả.

Sáu sơ đồ lớp được đối chiếu tên, chữ ký thành viên, cặp quan hệ và hướng với PlantUML; 25 liên kết được kiểm tra bội số và đầu hợp thành. Ba actor chuyên biệt kế thừa Người dùng. Hai sơ đồ giao tiếp có 11/23 IMessage và view thông điệp native, khớp hai sơ đồ tuần tự phân tích. ERD có chín bảng, 35 cột, chín FK. Phạm vi audit: `models/validation.json`.

Source đã chạy PASS 29 điều kiện Java và 13 điều kiện trình duyệt. Log nằm trong project; báo cáo không thêm chương kiểm tra. Giao diện chạy chỉ triển khai đăng ký thành viên và thống kê phim theo doanh thu, có đăng nhập quản lý hỗ trợ.

Các form chưa gửi; sinh viên tự xác nhận lớp và tự chọn điểm. Repository công khai để giảng viên đọc source. Phụ lục prompt biên tập các yêu cầu đã dùng thành cách diễn đạt chuyên nghiệp và mô tả cách áp dụng kết quả.
