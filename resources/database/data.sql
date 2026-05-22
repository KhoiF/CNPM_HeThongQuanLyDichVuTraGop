USE db_installment_payment;

-- Tắt kiểm tra khóa ngoại tạm thời để có thể Drop bảng cũ (nếu chạy lại script)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE tblPaymentDetail;
TRUNCATE TABLE tblBill;
TRUNCATE TABLE tblPaymentPeriod;
TRUNCATE TABLE tblBoughtItem;
TRUNCATE TABLE tblContract;
TRUNCATE TABLE tblItem;
TRUNCATE TABLE tblPartner;
TRUNCATE TABLE tblClient;
TRUNCATE TABLE tblUser;
-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO tblUser (id, fullName, username, password, position, tel, email, address) VALUES
(1, 'Nguyễn Minh Quân', 'quan', '123456', 'Nhân viên hành chính', '0901111111', 'quan@company.com', 'Hà Nội'),
(2, 'Trần Thanh Tùng', 'tung', '123456', 'Nhân viên hành chính', '0902222222', 'tung@company.com', 'Hải Phòng'),
(3, 'Lê Thị Hoa', 'hoa', '123456', 'Nhân viên hành chính', '0903333333', 'hoa@company.com', 'Đà Nẵng'),
(4, 'Phạm Văn Nam', 'nam', '123456', 'Quản lý', '0904444444', 'nam@company.com', 'TP HCM'),
(5, 'Đỗ Thị Mai', 'mai', '123456', 'Quản lý', '0905555555', 'mai@company.com', 'Cần Thơ');

INSERT INTO tblClient (id, idCard, fullName, tel, address, email) VALUES
(1, '001201000001', 'Nguyễn Văn An', '0981111111', 'Hà Nội', 'an@gmail.com'),
(2, '001201000002', 'Trần Thị Bình', '0982222222', 'Hải Phòng', 'binh@gmail.com'),
(3, '001201000003', 'Lê Văn Cường', '0983333333', 'Đà Nẵng', 'cuong@gmail.com');

INSERT INTO tblPartner (id, email, partnerName, phoneNumber, address, bankName, accountNumber, description) VALUES
(1, 'partner1@gmail.com', 'Công ty Điện máy A', '0911111111', 'Hà Nội', 'Vietcombank', '123456789', 'Đối tác cung cấp điện máy'),
(2, 'partner2@gmail.com', 'Công ty Nội thất B', '0922222222', 'Hải Phòng', 'BIDV', '234567890', 'Đối tác cung cấp nội thất'),
(3, 'partner3@gmail.com', 'Công ty Máy tính C', '0933333333', 'Đà Nẵng', 'Techcombank', '345678901', 'Đối tác cung cấp máy tính'),
(4, 'partner4@gmail.com', 'Công ty Xe máy D', '0944444444', 'TP HCM', 'Agribank', '456789012', 'Đối tác cung cấp xe máy'),
(5, 'partner5@gmail.com', 'Công ty Gia dụng E', '0955555555', 'Cần Thơ', 'MB Bank', '567890123', 'Đối tác cung cấp hàng gia dụng');

INSERT INTO tblItem (id, name, price, unit) VALUES
('MH01', 'Tủ lạnh Samsung', 12000000, 'Cái'),
('MH02', 'Máy giặt LG', 9000000, 'Cái'),
('MH03', 'Laptop Dell', 15000000, 'Cái'),
('MH04', 'Xe máy Honda Wave', 22000000, 'Cái'),
('MH05', 'Điều hòa Panasonic', 9000000, 'Cái');

INSERT INTO tblContract (id, signDate, loanTerm, clientId, userId, partnerId) VALUES
('HD01', '2026-03-15', 6, 1, 1, 1),
('HD02', '2026-04-01', 4, 2, 2, 2),
('HD03', '2026-04-05', 4, 3, 3, 3);

INSERT INTO tblBoughtItem (id, price, quantity, sellOff, note, itemId, contractId) VALUES
('BI01', 12000000, 1, 0, 'Khách mua tủ lạnh', 'MH01', 'HD01'),
('BI02', 9000000, 3, 3000000, 'Giảm giá khuyến mãi', 'MH05', 'HD01'),
('BI03', 9000000, 1, 1000000, 'Giảm giá khuyến mãi', 'MH02', 'HD02'),
('BI04', 15000000, 1, 0, 'Khách mua laptop', 'MH03', 'HD03');

INSERT INTO tblPaymentPeriod (id, period, dueDate, contractId) VALUES
('PP01', 1, '2026-04-05', 'HD01'),
('PP02', 2, '2026-05-05', 'HD01'),
('PP03', 3, '2026-06-05', 'HD01'),
('PP04', 4, '2026-07-05', 'HD01'),
('PP05', 5, '2026-08-05', 'HD01'),
('PP06', 6, '2026-09-05', 'HD01'),
('PP07', 1, '2026-05-05', 'HD02'),
('PP08', 2, '2026-06-05', 'HD02'),
('PP09', 3, '2026-07-05', 'HD02'),
('PP10', 4, '2026-08-05', 'HD02'),
('PP11', 1, '2026-04-15', 'HD03'),
('PP12', 2, '2026-05-15', 'HD03'),
('PP13', 3, '2026-06-15', 'HD03'),
('PP14', 4, '2026-07-15', 'HD03');

INSERT INTO tblBill (id, paymentDate, paymentType, note, userId) VALUES
(1, '2026-04-04', 'Tiền mặt', 'Thanh toán đợt 1 hợp đồng HD01', 1),
(2, '2026-05-01', 'Chuyển khoản', 'Thanh toán một phần đợt 1 HD02', 2),
(3, '2026-05-03', 'Tiền mặt', 'Thanh toán đợt 2 hợp đồng HD01', 3);

INSERT INTO tblPaymentDetail (id, allocatedAmount, note, paymentPeriodId, billId) VALUES
(1, 6000000, 'OK', 'PP01', 1),
(2, 1000000, 'Part', 'PP07', 2),
(3, 6000000, 'OK', 'PP02', 3);
