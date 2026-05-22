DROP DATABASE IF EXISTS db_installment_payment;
CREATE DATABASE db_installment_payment CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE db_installment_payment;

-- Tắt kiểm tra khóa ngoại tạm thời để có thể Drop bảng cũ (nếu chạy lại script)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS tblPaymentDetail;
DROP TABLE IF EXISTS tblBill;
DROP TABLE IF EXISTS tblPaymentPeriod;
DROP TABLE IF EXISTS tblBoughtItem;
DROP TABLE IF EXISTS tblContract;
DROP TABLE IF EXISTS tblItem;
DROP TABLE IF EXISTS tblPartner;
DROP TABLE IF EXISTS tblClient;
DROP TABLE IF EXISTS tblUser;
-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE tblUser (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    position VARCHAR(100) NOT NULL,
    tel VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE tblClient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idCard VARCHAR(12) NOT NULL UNIQUE,
    fullName VARCHAR(100) NOT NULL,
    tel VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE tblPartner (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    partnerName VARCHAR(150) NOT NULL,
    phoneNumber VARCHAR(15) NOT NULL,
    address VARCHAR(255) NOT NULL,
    bankName VARCHAR(100),
    accountNumber VARCHAR(50),
    description VARCHAR(255)
);

CREATE TABLE tblItem (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    price DOUBLE NOT NULL,
    unit VARCHAR(30) NOT NULL
);

CREATE TABLE tblContract (
    id VARCHAR(20) PRIMARY KEY,
    signDate DATE NOT NULL,
    loanTerm INT NOT NULL,
    clientId INT NOT NULL,
    userId INT NOT NULL,
    partnerId INT NOT NULL,
    FOREIGN KEY (clientId) REFERENCES tblClient(id),
    FOREIGN KEY (userId) REFERENCES tblUser(id),
    FOREIGN KEY (partnerId) REFERENCES tblPartner(id)
);

CREATE TABLE tblBoughtItem (
    id VARCHAR(20) PRIMARY KEY,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    sellOff DOUBLE NOT NULL DEFAULT 0,
    note VARCHAR(255),
    itemId VARCHAR(20) NOT NULL,
    contractId VARCHAR(20) NOT NULL,
    FOREIGN KEY (itemId) REFERENCES tblItem(id),
    FOREIGN KEY (contractId) REFERENCES tblContract(id) ON DELETE CASCADE
);

CREATE TABLE tblPaymentPeriod (
    id VARCHAR(20) PRIMARY KEY,
    period INT NOT NULL,
    dueDate DATE NOT NULL,
    contractId VARCHAR(20) NOT NULL,
    FOREIGN KEY (contractId) REFERENCES tblContract(id) ON DELETE CASCADE
);

CREATE TABLE tblBill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paymentDate DATE NOT NULL,
    paymentType VARCHAR(50) NOT NULL,
    note VARCHAR(255),
    userId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES tblUser(id)
);

CREATE TABLE tblPaymentDetail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    allocatedAmount DOUBLE NOT NULL,
    note VARCHAR(255),
    paymentPeriodId VARCHAR(20) NOT NULL,
    billId INT NOT NULL,
    FOREIGN KEY (paymentPeriodId) REFERENCES tblPaymentPeriod(id),
    FOREIGN KEY (billId) REFERENCES tblBill(id) ON DELETE CASCADE
);
