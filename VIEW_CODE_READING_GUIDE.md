# Huong Dan Doc Code Cac File `view`

Tai lieu nay dung de doc va giai thich cac man hinh Swing trong thu muc `src/view`.
Muc tieu la hieu luong nghiep vu thanh toan, khong can doc chi tiet layout do NetBeans sinh ra.

## 1. Nguyen Tac Doc Moi Form

Khi mo mot file `*Frm.java`, nen doc theo thu tu sau:

1. Doc `package` va `import`
   - Biet form nay phu thuoc vao DAO nao, model nao.
   - Vi du: `LoginFrm` import `UserDAO` va `User`, nen form nay lien quan dang nhap.

2. Doc cac field o dau class
   - Field cho biet form dang giu du lieu nao.
   - Vi du: `private User user;`, `private Contract contract;`, `private Bill bill;`.

3. Doc constructor
   - Constructor cho biet form nhan du lieu tu man hinh truoc nhu the nao.
   - Constructor co tham so thuong la constructor dung trong luong chay that.

4. Tam thoi bo qua `initComponents()`
   - Day la code NetBeans GUI Builder tu sinh de tao label, button, table, layout.
   - Chi doc khi can biet ten nut, ten bang, hoac label hien thi noi dung gi.

5. Doc cac ham `ActionPerformed`
   - Day la noi xu ly hanh dong cua nguoi dung: bam nut, nhan Enter, chon dong.
   - Cac ham nay thuong chi goi tiep ham nghiep vu rieng.

6. Doc cac ham nghiep vu rieng
   - Vi du: `doLogin()`, `searchContract()`, `reloadData()`, `savePaymentAmount()`, `saveBill()`.
   - Day la phan quan trong nhat de giai thich chuong trinh.

7. Doc `Variables declaration` cuoi file neu can
   - Phan nay liet ke cac component UI nhu `JButton`, `JLabel`, `JTable`.
   - Khong can giai thich chi tiet neu muc tieu la nghiep vu.

## 2. Luong Chay Chinh

Luong chinh cua chuong trinh:

```text
Main
  -> LoginFrm
  -> SellerHomeFrm
  -> SearchContractFrm
  -> ContractFrm
  -> PaymentPeriodFrm
  -> ConfirmFrm
```

Y nghia tung buoc:

1. `Main.java`
   - Thiet lap Look and Feel.
   - Goi `SwingUtilities.invokeLater(...)` de mo `LoginFrm`.

2. `LoginFrm`
   - Nguoi dung nhap username va password.
   - `doLogin()` tao object `User`, kiem tra rong, goi `new UserDAO().checkLogin(user)`.
   - Dang nhap dung thi mo `SellerHomeFrm(user)` va dong man hinh login.
   - Dang nhap sai thi hien `JOptionPane` bao loi.

3. `SellerHomeFrm`
   - Nhan object `User` tu `LoginFrm`.
   - `loadUserData()` hien thi thong tin nhan vien.
   - Bam nut thanh toan thi mo `SearchContractFrm(user)`.
   - Bam dang xuat thi mo lai `LoginFrm` va dong form hien tai.

4. `SearchContractFrm`
   - Nhan object `User` tu `SellerHomeFrm`.
   - Nguoi dung nhap ma hop dong.
   - `searchContract()` goi `new ContractDAO().searchContract(contractId)`.
   - Khong tim thay thi hien thong bao loi.
   - Tim thay thi tao `Bill`, gan `bill.setUser(user)`, mo `ContractFrm(contract, bill)`.

5. `ContractFrm`
   - Nhan `Contract` va `Bill`.
   - `reloadData()` hien thi thong tin hop dong, khach hang, doi tac, mat hang va cac dot thanh toan.
   - Chon mot dong trong bang dot thanh toan thi `openSelectedPeriod()` mo `PaymentPeriodFrm(this, bill, period)`.
   - Bam tiep thi `confirmPayment()` kiem tra `bill.getPaymentDetails()`; neu da co it nhat mot khoan thanh toan thi mo `ConfirmFrm(this, contract, bill)`.

6. `PaymentPeriodFrm`
   - Nhan `ContractFrm`, `Bill`, va `PaymentPeriod`.
   - `loadPaymentPeriod()` hien thi thong tin dot thanh toan va lich su thanh toan.
   - `savePaymentAmount()` doc so tien nguoi dung nhap, kiem tra rong, kiem tra la so, kiem tra lon hon 0, kiem tra khong vuot qua so tien con phai tra.
   - Neu hop le, goi `bill.setPaymentDetail(period, amount, "")` de luu tam vao object `Bill`.
   - Goi `contractFrm.reloadData()` de cap nhat bang dot thanh toan tren `ContractFrm`.

7. `ConfirmFrm`
   - Nhan `ContractFrm`, `Contract`, va `Bill`.
   - `loadConfirmData()` hien thi thong tin xac nhan hoa don.
   - `reloadDetails()` hien thi cac khoan thanh toan dang luu tam trong `Bill`.
   - `saveBill()` lay phuong thuc thanh toan, ngay thanh toan, ghi chu, roi goi `new BillDAO().addBill(bill)`.
   - Luu thanh cong thi thong bao thanh cong, xoa thanh toan tam bang `contract.clearPendingPayments()`, dong cac form lien quan.

8. `ViewUtils`
   - La class helper dung chung trong package `view`.
   - `formatDate()` doi `LocalDate` sang chuoi ngay.
   - `formatMoney()` va `formatRawMoney()` dinh dang tien.
   - `parseMoney()` chuyen chuoi tien nguoi dung nhap thanh `double`.
   - `blank()` doi `null` thanh chuoi rong de tranh hien thi `null`.

## 3. Mau Giai Thich Mot Form

Khi can trinh bay mot form, dung mau sau:

```text
Form nay co nhiem vu gi?
No nhan du lieu gi tu form truoc?
Nguoi dung co the thao tac gi?
Khi thao tac, form goi ham nao?
Ham do goi DAO/model nao?
Thanh cong thi chuyen sang man hinh nao?
That bai thi hien thong bao nao?
```

Vi du voi `LoginFrm`:

```text
LoginFrm la man hinh dang nhap. Khi nguoi dung bam Dang nhap hoac nhan Enter o o mat khau,
ham doLogin() lay username va password tu giao dien, tao object User, kiem tra du lieu rong,
roi goi UserDAO.checkLogin(user). Neu dang nhap dung, chuong trinh mo SellerHomeFrm(user)
va dong man hinh dang nhap. Neu sai, chuong trinh hien thong bao loi bang JOptionPane.
```

## 4. Nhung Ham Nen Nam Chac

Trong `LoginFrm`:

- `doLogin()`: xu ly toan bo nghiep vu dang nhap.
- `txtPasswordActionPerformed(...)`: nhan Enter o o password thi dang nhap.
- `btnLoginActionPerformed(...)`: bam nut dang nhap thi dang nhap.

Trong `SellerHomeFrm`:

- `loadUserData()`: dua thong tin `User` len cac label.
- `btnMakePaymentActionPerformed(...)`: mo man hinh tim hop dong.
- `btnLogoutActionPerformed(...)`: dang xuat.

Trong `SearchContractFrm`:

- `searchContract()`: lay ma hop dong, tim hop dong trong database, tao `Bill` tam.

Trong `ContractFrm`:

- `reloadData()`: nap lai toan bo du lieu hien thi.
- `loadContractHeader()`: hien thi thong tin khach hang, nhan vien lap hop dong, doi tac.
- `reloadItems()`: dua danh sach mat hang vao bang.
- `reloadPeriods()`: dua danh sach dot thanh toan vao bang.
- `openSelectedPeriod()`: mo form nhap tien cho dot thanh toan dang chon.
- `confirmPayment()`: mo form xac nhan neu da co it nhat mot khoan thanh toan.

Trong `PaymentPeriodFrm`:

- `loadPaymentPeriod()`: hien thi thong tin dot thanh toan.
- `reloadHistory()`: lay lich su thanh toan cu tu `BillDAO`.
- `savePaymentAmount()`: validate va luu tam so tien thanh toan vao `Bill`.

Trong `ConfirmFrm`:

- `loadConfirmData()`: hien thi thong tin hoa don truoc khi luu.
- `reloadDetails()`: hien thi cac chi tiet thanh toan trong `Bill`.
- `updateDetailNotesFromTable()`: lay ghi chu tu bang va cap nhat vao model.
- `saveBill()`: goi `BillDAO.addBill(bill)` de luu hoa don.

## 5. Cach Lan Theo Du Lieu

Co 3 object quan trong can theo doi:

1. `User`
   - Duoc tao trong `LoginFrm`.
   - Duoc truyen sang `SellerHomeFrm`.
   - Tiep tuc duoc truyen sang `SearchContractFrm`.
   - Duoc gan vao `Bill` bang `bill.setUser(user)`.

2. `Contract`
   - Duoc lay tu database trong `SearchContractFrm` bang `ContractDAO.searchContract(contractId)`.
   - Duoc truyen sang `ContractFrm`.
   - Duoc truyen sang `ConfirmFrm` de hien thi thong tin xac nhan.

3. `Bill`
   - Duoc tao trong `SearchContractFrm`.
   - Duoc truyen qua `ContractFrm`, `PaymentPeriodFrm`, `ConfirmFrm`.
   - Trong `PaymentPeriodFrm`, so tien thanh toan duoc luu tam vao `Bill`.
   - Trong `ConfirmFrm`, `Bill` moi duoc luu xuong database bang `BillDAO.addBill(bill)`.

## 6. Luu Y Ve Swing Va NetBeans

- `extends javax.swing.JFrame`: moi form la mot cua so rieng.
- `setVisible(true)`: hien thi cua so.
- `dispose()`: dong cua so hien tai.
- `JOptionPane.showMessageDialog(...)`: hien thong bao cho nguoi dung.
- `DefaultTableModel`: model du lieu cua `JTable`, dung `addRow(...)` de them dong vao bang.
- `setDefaultEditor(Object.class, null)`: khong cho sua truc tiep noi dung bang.
- `.form`: file cau hinh GUI Builder cua NetBeans, khong phai code nghiep vu.

## 7. Cau Hoi Tu Kiem Tra

Dung cac cau hoi nay de tu kiem tra xem da hieu luong code chua:

1. Dang nhap thieu username hoac password thi ham nao xu ly?
2. Dang nhap dung thi object `User` duoc truyen sang form nao?
3. Khi bam nut thanh toan o `SellerHomeFrm`, form nao duoc mo?
4. `SearchContractFrm` goi DAO nao de tim hop dong?
5. `Bill` duoc tao o dau?
6. `ContractFrm` hien thi danh sach dot thanh toan bang ham nao?
7. Khi chon mot dot thanh toan, form nao duoc mo?
8. So tien thanh toan duoc validate trong ham nao?
9. Truoc khi luu database, so tien thanh toan nam tam trong object nao?
10. `ConfirmFrm` goi DAO nao de luu hoa don?
11. Vi sao sau khi nhap tien, `PaymentPeriodFrm` goi `contractFrm.reloadData()`?
12. File `ViewUtils` giup tranh lap code o nhung cho nao?

## 8. Cach Tra Loi Khi Bi Hoi Ve Luong Thanh Toan

Co the giai thich ngan gon nhu sau:

```text
Nguoi dung dang nhap bang LoginFrm. Neu thanh cong, chuong trinh mo SellerHomeFrm va giu lai
thong tin User. Tu SellerHomeFrm, nguoi dung chon chuc nang thanh toan, chuong trinh mo
SearchContractFrm de nhap ma hop dong. Khi tim thay hop dong, chuong trinh tao mot Bill tam
gan voi User hien tai va mo ContractFrm de hien thi thong tin hop dong. Nguoi dung chon dot
thanh toan, nhap so tien trong PaymentPeriodFrm; so tien nay chua luu database ma chi duoc luu
tam vao Bill. Khi nguoi dung bam tiep, ConfirmFrm hien thi lai thong tin hoa don, cho chon
phuong thuc thanh toan va ghi chu. Cuoi cung ConfirmFrm goi BillDAO.addBill(bill) de luu hoa don
va chi tiet thanh toan xuong database.
```

