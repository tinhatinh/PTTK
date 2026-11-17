# Đối chiếu các trường nộp PlantUML

Thông tin: B23DCAT040 — Phan Thành Danh — mã đề 16. Bìa cũ ghi lớp 09; đối chiếu lại lịch lớp trước khi chọn CQ N9 (10h thứ 7) trên form. Báo cáo không tự xác nhận lịch học.

## Đặc tả
https://forms.gle/WjBeCqMYZnMPHmcV7

| Trường trên form | File chứa nguyên mã cần dán |
|---|---|
| 1 Use case tổng quan hệ thống | `uml/dac-ta/00-system-overview.puml` |
| 2 Use case chi tiết module 1 | `uml/dac-ta/01a-registration-use-case.puml` |
| 3 Use case chi tiết module 2 | `uml/dac-ta/01b-revenue-use-case.puml` |

Form đặc tả đang ghi mô tả “Các lớp PTTK 2025”; link này đúng link được cung cấp trong yêu cầu, chưa tự suy đoán link thay thế.

## Phân tích
https://forms.gle/ciKQDmZntpPKuH4H7

| Trường | File |
|---|---|
| 1 Lớp thực thể | `uml/phan-tich/02-domain.puml` |
| 2 Lớp module 1 | `uml/phan-tich/03-registration-analysis.puml` |
| 3 Lớp module 2 | `uml/phan-tich/04-revenue-analysis.puml` |
| 4 Communication module 1 | `uml/phan-tich/05-registration-communication.puml` |
| 5 Communication module 2 | `uml/phan-tich/06-revenue-communication.puml` |

Mức điểm và lý do tự đánh giá: sinh viên chọn sau khi đọc bài. Không mặc định 10/10.

## Thiết kế
https://forms.gle/rqmj7ZFnfQ5n7FzD7

| Trường | File |
|---|---|
| 1 Lớp thực thể | `uml/thiet-ke/07a-entities-design.puml` |
| 2 Thiết kế CSDL | `uml/thiet-ke/13-database.puml` |
| 3 Lớp module 1 | `uml/thiet-ke/07-registration-design.puml` |
| 4 Lớp module 2 | `uml/thiet-ke/08-revenue-design.puml` |
| 5 Sequence module 1 | `uml/thiet-ke/11-registration-sequence.puml` |
| 6 Sequence module 2 | `uml/thiet-ke/12-revenue-sequence.puml` |

Biểu đồ activity, gói và triển khai nằm trong cùng thư mục và được trình bày trong báo cáo, dù form không có trường riêng.

## Tự đánh giá cuối kỳ
https://forms.gle/yuLGeZZtWheGsnPu5

Mã sinh viên B23DCAT040; họ và đệm Phan Thành; tên Danh. Sinh viên tự chọn điểm cho bốn phần và tự xác nhận mức độ hiểu bài. Dữ kiện hỗ trợ: có đặc tả 2 UC, phân tích thực thể/lớp/giao tiếp, thiết kế lớp/CSDL/activity/sequence/gói/triển khai; Java đã chạy 25 kiểm tra và trình duyệt 13 kiểm tra. Hạn chế ở cuối README là cơ sở để viết phần thiếu/chưa chắc chắn. Không có form nào được tự gửi trong phiên tạo project.

Hai statechart bổ sung nằm tại `uml/phan-tich/04a-registration-states.puml` và `04b-revenue-states.puml`; form phân tích hiện không có trường riêng. Các ảnh sơ đồ trong báo cáo xuất từ Visual Paradigm; xem `models/README.md`.
