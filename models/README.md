# Mô hình CineMan

Mở `CineMan_De16_B23DCAT040.vpp` bằng Visual Paradigm 18.1. Project gồm 22 sơ đồ native có thể chỉnh sửa:

| Pha | Sơ đồ |
|---|---|
| Đặc tả | Use case tổng quan và hai use case chi tiết |
| Phân tích | Lớp thực thể, hai lớp phân tích, hai trạng thái giao diện, hai giao tiếp, hai tuần tự |
| Thiết kế | Ba lớp thiết kế, hai hoạt động, hai tuần tự, cơ sở dữ liệu, gói, triển khai |

Mã PlantUML tương ứng nằm trong `../uml/`, chia theo ba pha. Ảnh trong báo cáo được xuất trực tiếp từ Visual Paradigm; watermark Community Edition được giữ nguyên.

Các package mô hình tách trách nhiệm phân tích và thiết kế. Chúng không phải package Java; sơ đồ `14-packages` biểu diễn cấu trúc package của source. Ở pha phân tích, kịch bản, giao tiếp và tuần tự dùng cùng 11 bước đăng ký và 23 bước thống kê. Pha thiết kế chuyển trách nhiệm sang Servlet, service, DAO và các record Java.

Mô hình có các chức năng bối cảnh của toàn hệ thống; bản cài đặt giới hạn ở đăng ký thành viên và thống kê phim theo doanh thu. Các lớp thống kê là dữ liệu dẫn xuất, không có bảng lưu riêng.
