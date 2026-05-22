# Hệ thống quản lý dịch vụ trả góp

Module: **Cho khách hàng thanh toán theo hạn**.

## Công nghệ

- Java 17
- Java Swing
- JDBC
- MySQL
- NetBeans/Ant style project

## Cấu trúc

```text
Project/
  src/
    Main.java
    dao/
    model/
    view/
  resources/database/
    schema.sql
    data.sql
  lib/
  nbproject/
```

## Database

Tạo dữ liệu bằng MySQL:

```sql
SOURCE resources/database/schema.sql;
SOURCE resources/database/data.sql;
```

Thông tin kết nối mặc định nằm trong `src/dao/DAO.java`:

```text
Database: db_installment_payment
Username: root
Password: 123456
```

Có thể override khi chạy mà không cần sửa code:

```powershell
java -Ddb.user=root -Ddb.password=your_password -cp "out/production/Project;lib/*" Main
```

## Chạy ứng dụng

Tài khoản mẫu:

```text
quan / 123456
```

Compile bằng terminal:

```powershell
javac -encoding UTF-8 -cp "lib/*" -d out/production/Project (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out/production/Project;lib/*" Main
```

Luồng chính: đăng nhập -> thanh toán khách hàng -> tìm hợp đồng `HD01` -> chọn đợt thanh toán -> nhập số tiền -> xác nhận -> lưu hóa đơn.

## Test

```powershell
javac -encoding UTF-8 -cp "out/production/Project;lib/*" -d out/test (Get-ChildItem -Recurse test -Filter *.java).FullName
java -cp "out/production/Project;out/test;lib/*" ModelSmokeTest
java -Ddb.user=root -Ddb.password=your_password -cp "out/production/Project;out/test;lib/*" DaoSmokeTest
```
