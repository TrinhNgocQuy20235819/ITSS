# 📋 Interface Design Rules — Tài liệu hướng dẫn cho AI viết code

> Tóm tắt từ: IT4490 – Software Design & Construction, Chapter 6: Interface Design  
> Mục đích: Cung cấp quy tắc chuẩn để AI sinh code giao diện đúng chuẩn thiết kế phần mềm

---

## PHẦN 1 — THIẾT KẾ GIAO DIỆN NGƯỜI DÙNG (GUI)

### 1.1 Chuẩn hóa cấu hình màn hình (Screen Standardization)

Khi viết code giao diện, phải tuân thủ các quy tắc chuẩn hóa sau:

#### Display & Layout
- Xác định rõ kích thước màn hình, độ phân giải, số màu hỗ trợ trước khi code.
- Chia màn hình thành các vùng/cửa sổ (Window) rõ ràng.
- Vị trí các nút chuẩn (OK, Cancel, Đăng ký, Tìm kiếm) phải nhất quán trên toàn hệ thống.
- Vị trí hiển thị thông báo (message), tiêu đề màn hình (screen title), và menu phải thống nhất.
- Dùng font chữ, kích cỡ, kiểu chữ nhất quán cho tất cả các màn hình.

#### Control (Điều khiển)
- Style, size, color của các control (button, input, dropdown…) phải đồng nhất.
- Định nghĩa rõ thứ tự di chuyển focus (Tab sequence) cho các form input.
- Quy trình kiểm tra đầu vào (input validation) phải được chuẩn hóa và áp dụng thống nhất.

#### Menu
- Thiết kế menu theo đặc tả giao diện chung (common client area).
- Hỗ trợ nhập trực tiếp từ bàn phím (keyboard shortcut).
- Phân công shortcut key nhất quán, không trùng lặp giữa các màn hình.

#### Messages & Errors
- Hiển thị thông báo "đang xử lý" (busy/loading indicator) khi thực hiện tác vụ lâu.
- Xử lý lỗi theo quy trình chuẩn hóa (standardized error handling).
- Nội dung Help phải nhất quán với tài liệu hướng dẫn, dùng chung thuật ngữ.

---

### 1.2 Tạo màn hình (Screen Images)

- Bắt đầu từ **use case** và **boundary classes** (các lớp tương tác với người dùng).
- Map từng boundary class sang một màn hình cụ thể.
- Dựa vào mô tả input/output trong use case specification để thiết kế màn hình.

#### Công cụ thiết kế màn hình được chấp nhận
| Loại | Công cụ |
|---|---|
| Đơn giản | Notepad, Excel, PowerPoint, Word |
| Miễn phí | InVision, Eclipse, NetBeans |
| Thương mại | Axure RP, Adobe Dreamweaver, Photoshop, Visual Studio |

> **Quy tắc cho AI**: Khi nhận yêu cầu thiết kế màn hình, hãy hỏi xem đã có use case hay boundary class mô tả chưa. Nếu có, sinh code dựa trên đó.

---

### 1.3 Sơ đồ chuyển màn hình (Screen Transition Diagram)

Mọi hệ thống giao diện phải có sơ đồ chuyển đổi màn hình. Có **4 kiểu chuyển màn hình** chuẩn:

#### Kiểu 1 — Simple Screen Transition (Chuyển đơn giản)
- Chuyển sang màn hình độc lập mới.
- Màn hình trước đóng lại hoàn toàn.
- **Ví dụ**: Edit Slide → View Slide Show (From Start)

```
[Screen A] ──────────────→ [Screen B]
```

#### Kiểu 2 — Transition to a Dependent Screen (Chuyển màn hình phụ thuộc)
- Mở màn hình mới kèm dữ liệu từ màn hình trước.
- **Ví dụ**: Edit Slide → Duplicate Slide (với slide đang chọn)

```
[Screen A] ──(data)──────→ [Screen B (dependent)]
```

#### Kiểu 3 — Transition to Independent Child Screen (Pop-up độc lập)
- Mở pop-up/child screen mới.
- Màn hình cha và các màn hình khác **vẫn hoạt động** được trong lúc child đang hiển thị.
- **Ví dụ**: Edit Slide → Replace Text (modeless dialog)

```
[Screen A] ──→ [Child Screen (modeless)]
(A vẫn tương tác được)
```

#### Kiểu 4 — Transition to Dependent Child Screen (Pop-up chặn)
- Mở pop-up/child screen mới.
- Màn hình cha **KHÔNG** thao tác được khi child đang hiển thị (modal dialog).
- **Ví dụ**: Edit Slide → Format Text

```
[Screen A] ──◇──→ [Child Screen (modal)]
(A bị khóa cho đến khi Child đóng)
```

> **Quy tắc cho AI**: Khi viết code dialog/modal, hãy xác định rõ kiểu 3 (modeless) hay kiểu 4 (modal) trước khi sinh code. Mặc định nên là modal (kiểu 4) trừ khi được chỉ định khác.

---

### 1.4 Đặc tả màn hình (Screen Specification)

Mỗi màn hình cần có đặc tả đầy đủ gồm 3 thành phần:

#### A. Screen Image (Hình ảnh màn hình)
- Mockup/wireframe của màn hình hiển thị.

#### B. List of Functions (Danh sách chức năng)
- Tên và chức năng của từng thành phần trên màn hình (button, field, panel…).
- Mô tả sự kiện (event) cho từng thành phần.
- Attribute của từng part.
- Đặc tả kiểm tra đầu vào (input check specification).
- Đặc tả đầu ra (output specification).

#### C. Field Attribute Definition (Định nghĩa thuộc tính trường dữ liệu)

Mỗi field trên màn hình phải được định nghĩa đầy đủ theo bảng sau:

| Thuộc tính | Mô tả |
|---|---|
| Item name | Tên field |
| Number of digits (bytes) | Số ký tự/byte tối đa |
| Type | Kiểu dữ liệu (Numeral / Character / Numeral+Special) |
| Field attribute | Màu nền, trạng thái (White, Green/blink khi lỗi) |
| Remarks | Ghi chú đặc biệt (VD: left-justified, format ZZZ,ZZZ.ZZ9) |

**Ví dụ bảng field attribute cho màn hình "Order Entry":**

| Item name | Digits | Type | Field Attribute | Remarks |
|---|---|---|---|---|
| Transaction category | 3 | Numeral | Green (blink) | Error items blink |
| Customer code | 5 | Numeral | Green (blink) | Error items blink |
| Customer name | 30 | Character | White | 15 chars, left-justified |
| Product code | 8 | Numeral | Green (blink) | Error items blink |
| Product name | 22 | Character | White | 11 chars, left-justified |
| Quantity | 6 | Numeral | Green (blink) | Error items blink |
| Unit price | 7 | Numeral | White | — |
| Amount | 9 | Numeral | White | — |
| Quantity in stock | 10 | Numeral, special character | White | Format: ZZZ,ZZZ.ZZ9 |

> **Quy tắc cho AI**: Khi nhận yêu cầu tạo form, hãy sinh ra bảng field attribute trước, sau đó mới sinh HTML/code form tương ứng.

---

## PHẦN 2 — THIẾT KẾ GIAO DIỆN HỆ THỐNG/THIẾT BỊ (System/Device Interface)

### 2.1 Xác định Subsystem

**Subsystem** là đơn vị độc lập, đóng gói hoàn toàn hành vi bên trong, giao tiếp ra ngoài qua interface.

#### Subsystem vs Package — Phân biệt quan trọng

| Tiêu chí | Subsystem | Package |
|---|---|---|
| Cung cấp hành vi | ✅ Có | ❌ Không |
| Đóng gói hoàn toàn | ✅ Có | ❌ Không |
| Dễ thay thế | ✅ Dễ | ❌ Khó |

> **Nguyên tắc vàng**: *Encapsulation is the key!* — Subsystem che giấu hoàn toàn chi tiết bên trong.

#### Các class phân tích có thể trở thành Subsystem
- Các class cung cấp dịch vụ phức tạp (complex services/utilities)
- Boundary classes (giao diện người dùng và giao diện hệ thống ngoài)
- Sản phẩm hoặc hệ thống ngoài tích hợp vào:
  - Communication software
  - Database access layer
  - Common utilities
  - Application-specific components

#### Lợi ích của Subsystem
- Phát triển độc lập miễn là interface không đổi.
- Deploy trên nhiều node phân tán.
- Thay thế/nâng cấp mà không ảnh hưởng phần còn lại.
- Kiểm soát bảo mật trên từng tài nguyên riêng biệt.

---

### 2.2 Xác định Interface của Subsystem

#### Mục đích
Xác định interface của subsystem dựa trên trách nhiệm (responsibility) của nó.

#### Các bước thực hiện
1. Xác định tập hợp interface ứng viên cho tất cả subsystem.
2. Tìm các interface tương tự nhau để hợp nhất.
3. Định nghĩa quan hệ phụ thuộc giữa các interface.
4. Map interface vào từng subsystem tương ứng.
5. Định nghĩa hành vi (behavior) mà interface đặc tả.
6. Package hóa các interface.

> **Nguyên tắc**: *Stable, well-defined interfaces are key to a stable, resilient architecture.*

#### Quy tắc đặt tên Interface
- **Interface name**: Tên phản ánh vai trò trong hệ thống.
- **Quy ước**: Interface bắt đầu bằng chữ `I` (VD: `ICourseCatalogSystem`, `IBillingSystem`).
- **Interface description**: Diễn đạt trách nhiệm rõ ràng.
- **Operation definition**: Tên operation phản ánh kết quả, mô tả đầy đủ tham số và kiểu trả về.
- **Interface documentation**: Kèm theo sequence diagram, state diagram, test plan.

#### Ví dụ — Mapping Analysis → Design

**Analysis class:**
```
<<boundary>>
BillingSystem
  //submit bill()
```

**Design (Subsystem + Interface):**
```
BillingSystem <<subsystem>>
  └── IBillingSystem <<interface>>
        submitBill(forTuition: Double, forStudent: Student)
```

---

### 2.3 Thiết kế Subsystem

#### Ký hiệu UML chuẩn

```
# Canonical form:
Interface Name
<<interface>>
       ↑ (realization)
Subsystem Name
<<subsystem>>

# Elided form:
Subsystem Name ──○ Interface Name
<<subsystem>>
```

#### Ví dụ — CourseCatalogSystem

```
CourseCatalogSystem <<subsystem>>
  realizes: ICourseCatalogSystem <<interface>>
    + initialize()
    + getCourseOfferings(forSemester: Semester, forStudent: Student): CourseOfferingList
```

---

## CHECKLIST — Dùng cho AI khi viết code giao diện

Trước khi sinh code, AI cần xác nhận đủ các thông tin sau:

### Checklist GUI
- [ ] Đã có use case / boundary class mô tả màn hình chưa?
- [ ] Đã xác định kiểu chuyển màn hình (1/2/3/4)?
- [ ] Các nút chuẩn (OK, Cancel…) đặt đúng vị trí quy ước chưa?
- [ ] Đã định nghĩa field attribute cho tất cả input/output chưa?
- [ ] Input validation đã được chuẩn hóa chưa?
- [ ] Tab sequence (thứ tự focus) đã được định nghĩa chưa?
- [ ] Thông báo lỗi và loading indicator đã có chưa?
- [ ] Shortcut key có bị trùng không?

### Checklist System Interface
- [ ] Đã xác định danh sách subsystem chưa?
- [ ] Mỗi subsystem đã có ít nhất 1 interface (`I` prefix) chưa?
- [ ] Interface có đầy đủ tên operation + tham số + kiểu trả về chưa?
- [ ] Đã phân biệt Provided interface vs Required interface chưa?
- [ ] Subsystem có đóng gói hoàn toàn (không lộ implementation) chưa?

---

## TEMPLATE — Prompt mẫu cho AI viết code

### Prompt tạo Form màn hình

```
Hãy tạo form [tên màn hình] với các quy tắc sau:
- Use case: [mô tả use case]
- Kiểu dialog: [modal / modeless / full screen]
- Tab sequence: [field1 → field2 → field3]
- Field attributes:
  | Field | Type | Max length | Validation | Format |
  |-------|------|-----------|-----------|--------|
  | ...   | ...  | ...       | ...       | ...    |
- Nút bấm: [tên nút] → [chức năng] → [chuyển sang màn hình nào]
- Thông báo lỗi: hiển thị [inline / toast / modal]
- Loading indicator: [spinner / progress bar / skeleton]
```

### Prompt tạo Screen Transition

```
Tạo screen transition diagram với các màn hình sau:
- Màn hình chính: [tên]
- Các chuyển đổi:
  [Screen A] --Kiểu 1--> [Screen B]  (simple, độc lập)
  [Screen A] --Kiểu 2--> [Screen C]  (dependent, có data)
  [Screen A] --Kiểu 3--> [Screen D]  (modeless popup)
  [Screen A] --Kiểu 4--> [Screen E]  (modal popup, chặn A)
```

### Prompt tạo Subsystem Interface

```
Tạo interface cho subsystem [tên] với:
- Interface name: I[TênSubsystem]
- Operations:
  + operationName(param1: Type, param2: Type): ReturnType
  + ...
- Provided by: [SubsystemName] <<subsystem>>
- Required by: [ControllerClassName] <<control>>
```

---

*Tài liệu này được tổng hợp từ slide bài giảng IT4490 – Interface Design, dùng làm system prompt / context cho AI assistant khi phát triển phần mềm.*
