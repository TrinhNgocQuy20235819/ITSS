# Hệ thống Phân bổ Tự động - Use Case: Tính toán phân bổ tự động
## Sinh viên: Vũ Tiến Lợi

---

## Cấu trúc Project

```
PhanBoTuDong/
├── src/
│   ├── Main.java                          # Điểm khởi chạy ứng dụng
│   ├── entity/                            # Tầng Entity (Thực thể)
│   │   ├── MatHang.java                   # Mặt hàng trong yêu cầu nhập
│   │   ├── YeuCauNhapHang.java            # Yêu cầu nhập hàng
│   │   ├── ThongTinSite.java              # Thông tin Site nhập khẩu
│   │   ├── ThongTinKho.java               # Thông tin tồn kho tại Site
│   │   ├── KetQuaPhanBo.java              # Kết quả phân bổ
│   │   └── DonHang.java                   # Đơn hàng (dự thảo)
│   ├── dao/                               # Tầng DAO (Truy xuất dữ liệu - Mock Data)
│   │   ├── YeuCauNhapHangDAO.java         # DAO cho Yêu cầu nhập hàng
│   │   ├── ThongTinKhoDAO.java            # DAO cho Thông tin tồn kho
│   │   ├── ThongTinSiteDAO.java           # DAO cho Thông tin Site
│   │   └── KetQuaPhanBoDAO.java           # DAO cho Kết quả phân bổ
│   ├── service/                           # Tầng Service (Nghiệp vụ - Thuật toán)
│   │   └── PhanBoService.java             # ★ CORE: Thuật toán Greedy phân bổ
│   ├── controller/                        # Tầng Controller (Điều khiển)
│   │   └── PhanBoController.java          # Điều phối View <-> Service
│   └── view/                              # Tầng View (Giao diện Swing)
│       └── PhanBoView.java                # Giao diện 3 màn hình
├── test/
│   └── test/
│       └── PhanBoServiceTest.java         # JUnit Test (Hộp đen + Hộp trắng C1)
└── README.md                              # File hướng dẫn này
```

---

## Hướng dẫn Import vào Eclipse

### Bước 1: Tạo Java Project mới
1. Mở Eclipse → `File` → `New` → `Java Project`
2. Đặt tên project: `PhanBoTuDong`
3. Bỏ chọn "Use default location", trỏ tới thư mục `PhanBoTuDong/`
4. Nhấn `Finish`

### Bước 2: Cấu hình Source Folders
1. Chuột phải project → `Properties` → `Java Build Path`
2. Tab `Source`:
   - Thêm folder `src` làm Source Folder
   - Thêm folder `test` làm Source Folder (cho JUnit)
3. Nhấn `Apply and Close`

### Bước 3: Thêm JUnit Library
1. Chuột phải project → `Properties` → `Java Build Path`
2. Tab `Libraries` → `Add Library...` → `JUnit` → Chọn `JUnit 4` → `Finish`
3. Nhấn `Apply and Close`

### Bước 4: Chạy ứng dụng
1. Mở file `src/Main.java`
2. Chuột phải → `Run As` → `Java Application`
3. Giao diện Swing sẽ hiện ra

### Bước 5: Chạy Unit Test
1. Mở file `test/test/PhanBoServiceTest.java`
2. Chuột phải → `Run As` → `JUnit Test`
3. Xem kết quả trong tab JUnit

---

## Thuật toán Phân bổ Tự động (Greedy)

### Ưu tiên xử lý:
1. **(a) Ưu tiên đường Tàu** → Chi phí thấp, thời gian dài
2. **(b) Ưu tiên Site có SL tồn kho lớn** → Sắp xếp giảm dần
3. **(c) Số lượng Site ít nhất** → Greedy: lấy từ trên xuống đến khi đủ

### Luồng xử lý cho mỗi mặt hàng:
```
Lấy DS Site có tồn kho
    ↓
Lọc Site giao kịp bằng TÀU → Sắp xếp SL kho giảm dần → Chọn Greedy
    ↓
Đủ SL? ──YES──→ Kết thúc (thành công)
    │
   NO
    ↓
Lọc Site giao kịp bằng HÀNG KHÔNG → Sắp xếp SL kho giảm dần → Chọn tiếp
    ↓
Đủ SL? ──YES──→ Kết thúc (phân bổ kết hợp Tàu + HK)
    │
   NO
    ↓
Tạo cảnh báo THIẾU HÀNG (SL thiếu = SL yêu cầu - SL đã phân bổ)
```

---

## Mock Data có sẵn

| Yêu cầu | Mặt hàng | SL YC | Tổng kho | Kết quả mong đợi |
|----------|----------|-------|----------|-------------------|
| YC-2025-001 | MH001 - IC-7805 | 500 | 580 | ✅ Đủ hàng |
| YC-2025-001 | MH002 - Tụ điện | 1000 | 1050 | ✅ Đủ hàng |
| YC-2025-001 | MH003 - Điện trở | 2000 | 2100 | ✅ Đủ hàng |
| YC-2025-002 | MH004 - Arduino | 200 | 210 | ✅ Đủ (vừa khít) |
| YC-2025-002 | MH005 - Cảm biến | 300 | 250 | ⚠️ Thiếu 50 |
| YC-2025-004 | MH007 - Chip ARM | 800 | 900 | ⏰ Deadline gấp |
