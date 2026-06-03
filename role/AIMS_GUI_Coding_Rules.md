# AIMS – Bộ Quy Tắc Thiết Kế Giao Diện (UI Coding Rules)

> **Mục đích:** Tài liệu này là bộ quy tắc (rules / system prompt) dành cho AI khi viết code giao diện cho hệ thống AIMS. AI phải tuân thủ toàn bộ các quy tắc dưới đây trong mọi màn hình.

---

## 1. DISPLAY – Chuẩn hoá màn hình

| Thuộc tính | Giá trị |
|---|---|
| Số màu hỗ trợ | 16,777,216 màu (True Color) |
| Độ phân giải chuẩn | 1366 × 768 pixels |

---

## 2. LAYOUT – Quy tắc bố cục chung

### 2.1 Vị trí cố định các thành phần

| Thành phần | Vị trí |
|---|---|
| **Button** | Dưới cùng theo chiều dọc, ở giữa theo chiều ngang của khung |
| **Message / Thông báo** | Chính giữa trung tâm khung màn hình |
| **Screen Title** | Góc trên bên trái của màn hình |

### 2.2 Điều hướng màn hình

- **Không** có các khung chồng lên nhau (no overlapping frames).
- Các màn hình được tách biệt hoàn toàn.
- **Ngoại lệ:** Màn hình hướng dẫn sử dụng hoạt động như một **popup** — màn hình phía dưới bị vô hiệu hoá khi popup đang hiển thị.
- Khi app khởi chạy: hiển thị **Splash Screen** trước, sau đó chuyển sang **Home Screen**.

---

## 3. CONTROL – Quy tắc kiểm soát giao diện

### 3.1 Typography

| Thuộc tính | Giá trị |
|---|---|
| Font | Segoe UI |
| Kích thước | Medium – 24px |
| Màu chữ mặc định | `#000000` (Đen) |

### 3.2 Xử lý Input

Kiểm tra input theo thứ tự bắt buộc:

1. **Bước 1:** Kiểm tra trường có bị bỏ trống (empty) không.
2. **Bước 2:** Kiểm tra định dạng (format) của dữ liệu nhập.

### 3.3 Định dạng hiển thị số

- Dùng **dấu phẩy** (`,`) để phân cách hàng nghìn. Ví dụ: `1,000,000`
- Chuỗi hợp lệ chỉ được chứa: chữ cái, chữ số, dấu phẩy (`,`), dấu chấm (`.`), dấu cách (` `), dấu gạch dưới (`_`), và dấu gạch nối (`-`).

### 3.4 Phím tắt & Điều hướng bàn phím

- **Không có phím tắt** (no keyboard shortcuts).
- Có **nút Back** để quay lại màn hình trước đó.
- Có **nút "X"** nằm ở thanh tiêu đề bên phải để đóng màn hình.

---

## 4. ERROR HANDLING – Xử lý lỗi

- Khi có lỗi, hiển thị **một thông điệp rõ ràng** để thông báo cho người dùng biết vấn đề cụ thể đang gặp phải.
- Thông điệp lỗi đặt tại vị trí: **giữa trung tâm màn hình**.

---

## 5. SCREEN FLOW – Thứ tự các màn hình

Luồng điều hướng bắt buộc theo thứ tự:

```
Splash Screen
    ↓
Home Screen ───────────────────────────────→ Result Screen
    ↓
Cart Screen → Delivery Form → Invoice Screen → Payment Form
                                    ↓
                              Error Screen (nếu có lỗi)
```

| STT | Màn hình | Mô tả |
|---|---|---|
| 1 | Splash Screen | Màn hình chớp – hiển thị khi khởi động app |
| 2 | Home Screen | Màn hình chính – danh sách sản phẩm |
| 3 | View Cart Screen | Xem các sản phẩm trong giỏ hàng |
| 4 | Delivery Form | Điền thông tin giao hàng |
| 5 | Invoice Screen | Xem chi tiết đơn hàng |
| 6 | Payment Form | Điền thông tin thanh toán |
| 7 | Result Screen | Kết quả thanh toán |

---

## 6. MÔ TẢ TỪNG MÀN HÌNH

---

### 6.1 Splash Screen

- Hiển thị logo **AIMS** ở giữa màn hình.
- Không có button hay input.
- Tự động chuyển sang **Home Screen** sau một khoảng thời gian ngắn.

---

### 6.2 Home Screen

**Chức năng:** Hiển thị danh sách sản phẩm (Books, CDs, DVDs) theo dạng lưới.

| Control | Thao tác | Chức năng |
|---|---|---|
| Search bar | Nhập text + nhấn Search | Tìm kiếm sản phẩm |
| Dropdown bên cạnh Search | Click | Chọn loại tìm kiếm |
| Cart icon (góc trên phải) | Click | Chuyển sang View Cart Screen |
| Add to Cart button | Click | Thêm sản phẩm vào giỏ hàng |
| Spinner (số lượng) | Click ▲▼ | Điều chỉnh số lượng sản phẩm |

**Layout sản phẩm:**
- Mỗi sản phẩm gồm: ảnh bìa, tên (Book/CD/DVD + số thứ tự), Price, Avail (tồn kho), spinner số lượng, nút Add to Cart.
- Hiển thị dạng **grid 4 cột**.

---

### 6.3 View Cart Screen

**Chức năng:** Xem và quản lý các sản phẩm đã thêm vào giỏ hàng.

| Control | Thao tác | Chức năng |
|---|---|---|
| Khu vực danh sách items | Hiển thị ban đầu | Hiển thị media kèm thông tin tương ứng |
| Khu vực Subtotal | Hiển thị ban đầu | Hiển thị tổng tiền tạm tính |
| Delete button | Click | Xoá sản phẩm khỏi giỏ hàng |
| Place Order button | Click | Chuyển sang Delivery Form |

**Định nghĩa trường dữ liệu – View Cart:**

| Tên trường | Số ký tự (bytes) | Kiểu | Thuộc tính | Căn lề |
|---|---|---|---|---|
| Media Title | 50 | Numeral | Màu xanh (Blue) | Trái |
| Price | 20 | Numeral | Màu xanh (Blue) | Phải |
| Subtotal | 20 | Numeral | Màu xanh (Blue) | Trái |

**Hiển thị tài chính:**
- Hiển thị **Subtotal**, **VAT (10%)**, và **Amount** (tổng cuối).
- Đơn vị tiền tệ: **VND**.

---

### 6.4 Delivery Form (Shipping Screen)

**Chức năng:** Thu thập thông tin giao hàng của khách.

| Trường | Loại control | Bắt buộc |
|---|---|---|
| Name | Text field | Có |
| Phone | Text field | Có |
| Province/City | Dropdown (ComboBox) | Có |
| Address | Text field | Có |
| Shipping Instructions | Text area | Không |

| Control | Thao tác | Chức năng |
|---|---|---|
| Confirm Delivery button | Click | Validate input → Chuyển sang Invoice Screen |

**Validate:**
1. Kiểm tra các trường bắt buộc không được để trống.
2. Kiểm tra định dạng Phone (chỉ chứa số).

---

### 6.5 Invoice Screen

**Chức năng:** Hiển thị xác nhận đơn hàng trước khi thanh toán.

**Thông tin hiển thị:**

| Nhóm | Trường |
|---|---|
| Thông tin giao hàng | Name, Phone, City, Address, Shipping Instructions |
| Thông tin tài chính | Subtotal, Shipping Fees, **Total** (in đậm) |

| Control | Thao tác | Chức năng |
|---|---|---|
| Confirm Order button | Click | Chuyển sang Payment Form |

**Lưu ý:** Tổng tiền (**Total**) được hiển thị in đậm, nổi bật. Đơn vị: VND.

---

### 6.6 Payment Form

**Chức năng:** Thu thập thông tin thanh toán.

**Phương thức thanh toán hiện tại:** Credit Card (RadioButton).

| Trường | Loại control | Bắt buộc |
|---|---|---|
| Card Number | Text field | Có |
| Card Holder Name | Text field | Có |
| Expiration Date | Text field | Có |
| Card Security Code | Text field | Có |

| Control | Thao tác | Chức năng |
|---|---|---|
| Confirm Payment button | Click | Validate → Xử lý thanh toán → Chuyển sang Result Screen |

**Validate:**
1. Tất cả trường không được để trống.
2. Card Number: chỉ chứa số, đúng độ dài theo chuẩn.
3. Expiration Date: đúng định dạng MM/YY.
4. Card Security Code: chỉ chứa số, 3-4 ký tự.

---

### 6.7 Result Screen (Payment Result)

**Chức năng:** Hiển thị kết quả thanh toán.

| Control | Thao tác | Chức năng |
|---|---|---|
| Label "PAYMENT RESULT!" | Hiển thị | Tiêu đề kết quả (màu nổi bật) |
| Message | Hiển thị | Thông điệp kết quả chi tiết |
| OK button | Click | Đóng màn hình / Quay về Home Screen |

---

## 7. GLOBAL UI RULES – Quy tắc áp dụng toàn bộ hệ thống

Dưới đây là danh sách quy tắc AI **phải tuân thủ** khi sinh code cho bất kỳ màn hình nào:

```
[RULE-01] Font mặc định: Segoe UI, 24px, màu #000000.
[RULE-02] Button luôn đặt ở dưới cùng, căn giữa theo chiều ngang.
[RULE-03] Screen title luôn đặt góc trên bên trái.
[RULE-04] Thông báo / message luôn hiển thị ở giữa màn hình.
[RULE-05] Không có màn hình nào được chồng lên màn hình khác (trừ popup hướng dẫn).
[RULE-06] Validate input theo thứ tự: (1) empty check → (2) format check.
[RULE-07] Hiển thị số dùng dấu phẩy ngăn hàng nghìn. Ví dụ: 1,500,000.
[RULE-08] Không có phím tắt. Điều hướng bằng button Back hoặc nút X trên thanh tiêu đề.
[RULE-09] Lỗi phải được thông báo bằng message rõ ràng, không âm thầm fail.
[RULE-10] Luồng màn hình phải đúng thứ tự: Splash → Home → Cart → Delivery → Invoice → Payment → Result.
[RULE-11] Màn hình Splash tự chuyển sang Home, không cần thao tác người dùng.
[RULE-12] Độ phân giải thiết kế chuẩn: 1366 × 768 px.
[RULE-13] Trường Price và các giá trị tài chính: màu xanh (Blue), căn phải.
[RULE-14] Trường Media Title và Subtotal: màu xanh (Blue), căn trái.
[RULE-15] VAT mặc định là 10%. Amount = Subtotal × 1.1.
```

---

## 8. SYSTEM PROMPT MẪU – Dùng cho AI khi viết code

Dán đoạn sau vào đầu mỗi prompt khi yêu cầu AI viết code màn hình:

```
You are a frontend developer building the AIMS media store application.
Follow ALL rules below strictly. Do not deviate.

GLOBAL RULES:
- Font: Segoe UI, 24px, color #000000
- Buttons: bottom-center of the frame
- Screen title: top-left corner
- Messages/alerts: center of the screen
- No overlapping screens (except the help popup)
- Input validation order: (1) empty check, then (2) format check
- Numbers formatted with comma separator (e.g., 1,500,000)
- No keyboard shortcuts; use Back button and X button for navigation
- Always show a clear error message on failure

SCREEN FLOW:
Splash → Home → Cart → Delivery Form → Invoice → Payment Form → Result

CURRENT TASK:
[Mô tả màn hình cần viết ở đây]
```

---

*Tài liệu được tổng hợp từ: AIMS GUI Design Specification – v1.0 (30/10/2020)*
*Người phụ trách: Đỗ Minh Hiếu*
