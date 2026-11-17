INSERT INTO account(id,username,password_hash,role,full_name) VALUES(1,'manager','$MANAGER_HASH$','MANAGER','Quản lý CineMan');
INSERT INTO employee(account_id,employee_code,position) SELECT id,'NV001','Quản lý' FROM account WHERE username='manager';
ALTER TABLE account ALTER COLUMN id RESTART WITH 100;
INSERT INTO movie VALUES(1,'Hành trình bình minh',110),(2,'Thành phố mùa hạ',105),(3,'Chuyến tàu cuối',95);
INSERT INTO room VALUES(1,'Phòng 01'),(2,'Phòng 02');
INSERT INTO seat VALUES(1,1,'A1'),(2,1,'A2'),(3,1,'A3'),(4,1,'A4'),(5,1,'A5'),(6,2,'B1'),(7,2,'B2'),(8,2,'B3');
INSERT INTO showtime VALUES(1,1,1,'2026-11-01 09:00:00'),(2,1,1,'2026-11-02 20:00:00'),(3,2,2,'2026-11-03 19:30:00'),(4,1,1,'2026-10-31 23:59:59'),(5,2,2,'2026-12-01 00:00:00'),(6,3,1,'2026-11-10 10:00:00'),(7,1,1,'2026-11-30 23:59:59');
INSERT INTO invoice VALUES(1,NULL,'2026-11-01 08:30:00','PAID'),(2,NULL,'2026-11-02 18:00:00','PAID'),(3,NULL,'2026-11-03 18:00:00','PAID'),(4,NULL,'2026-11-01 08:20:00','CANCELLED'),(5,NULL,'2026-10-31 21:00:00','PAID'),(6,NULL,'2026-11-30 20:00:00','PAID'),(7,NULL,'2026-11-10 09:00:00','CANCELLED'),(8,NULL,'2026-11-30 21:00:00','PAID');
INSERT INTO ticket VALUES(1,1,1,1,80000),(2,1,1,2,80000),(3,2,2,1,80000),(4,2,2,2,80000),(5,2,2,3,80000),(6,3,3,6,90000),(7,3,3,7,90000),(8,3,3,8,90000),(9,4,1,3,80000),(10,5,4,1,80000),(11,6,5,6,90000),(12,7,6,1,70000),(13,8,7,1,80000);
