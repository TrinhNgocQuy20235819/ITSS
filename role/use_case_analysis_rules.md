# USE CASE ANALYSIS RULES FOR AI CODE GENERATION
> Dựa trên tài liệu IT4490 – Software Design and Construction (Lecture 5)

---

## 1. MỤC ĐÍCH CỦA USE CASE ANALYSIS

Trước khi viết code, AI phải hoàn thành 4 nhiệm vụ:

- [ ] Xác định các **analysis classes** thực hiện flow of events của use case
- [ ] **Phân phối hành vi** của use case vào các classes đó
- [ ] Xác định **trách nhiệm** của từng class
- [ ] Phát triển **Use-Case Realizations** mô hình hóa sự cộng tác giữa các objects

---

## 2. INPUT & OUTPUT CỦA USE CASE ANALYSIS

```
INPUT:
  ├── Use-Case Model            (các use case đã xác định)
  ├── Supplementary Specs       (yêu cầu phi chức năng)
  ├── Glossary                  (thuật ngữ domain)
  ├── Software Architecture Doc (kiến trúc tổng thể)
  └── Project Specific Guidelines

         ▼  Use-Case Analysis  ▼

OUTPUT:
  ├── Analysis Classes          (Boundary / Control / Entity)
  └── Use-Case Realization      (Class + Sequence + Communication Diagrams)
```

---

## 3. BA LOẠI ANALYSIS CLASS — QUY TẮC CỐT LÕI

Toàn bộ hành vi của một use case **phải được phân phối đầy đủ** vào 3 loại class sau:

```
┌─────────────────────────────────────────────────────┐
│                   Use-Case Behavior                 │
│                                                     │
│   <<boundary>>   <<control>>   <<entity>>           │
│   Giao tiếp      Điều phối     Lưu trữ              │
│   bên ngoài      hành vi       thông tin            │
└─────────────────────────────────────────────────────┘
```

---

### 3.1. `<<boundary>>` — Boundary Class

**Vai trò**: Trung gian giữa hệ thống và môi trường bên ngoài (actor).

**Các loại:**
- User Interface classes (form, screen, dialog)
- System Interface classes (giao tiếp hệ thống khác)
- Device Interface classes (giao tiếp thiết bị ngoại vi)

**Quy tắc bắt buộc:**
```
✅ Tạo 1 boundary class cho mỗi cặp (Actor, Use Case)
✅ Tập trung vào THÔNG TIN cần hiển thị cho user
✅ Tập trung vào PROTOCOL cần định nghĩa (với system/device interface)
❌ KHÔNG thiết kế chi tiết UI (button layout, color...)
❌ KHÔNG thiết kế chi tiết cài đặt protocol
```

**Ví dụ** (Course Registration):
```
Actor: Student + UC: Register for Courses → RegisterForCoursesForm
Actor: Course Catalog + UC: Register for Courses → CourseCatalogSystem
```

---

### 3.2. `<<entity>>` — Entity Class

**Vai trò**: Lưu trữ và quản lý thông tin trong hệ thống. Là **key abstraction** của domain.

**Đặc điểm:**
- Độc lập với use case (có thể dùng lại ở nhiều UC)
- Độc lập với môi trường (platform-independent)
- Thường map 1-1 với các khái niệm trong Glossary/Business Domain

**Cách tìm Entity Classes — lọc danh từ:**
```
Bước 1: Gạch chân tất cả cụm danh từ trong flow of events của use case
Bước 2: Loại bỏ các candidate không hợp lệ:
         ❌ Trùng lặp (redundant)
         ❌ Mơ hồ, không rõ nghĩa (vague)
         ❌ Là actor (nằm ngoài hệ thống)
         ❌ Là implementation construct (database table, array...)
         ❌ Là attribute (lưu vào attribute của class khác)
         ❌ Là operation/action
Bước 3: Phần còn lại → candidate Entity Classes
```

**Ví dụ** (Register For Course):
```
Candidate nouns: Student, Course, CourseOffering, Schedule, Professor...
Sau khi lọc → Entity Classes: Student, CourseOffering, Schedule
```

---

### 3.3. `<<control>>` — Control Class

**Vai trò**: Điều phối hành vi trong hệ thống, xử lý logic nghiệp vụ phức tạp.

**Đặc điểm:**
- Phụ thuộc vào use case (use-case dependent)
- Độc lập với môi trường (environment independent)
- Ví dụ điển hình: transaction manager, resource coordinator, error handler

**Quy tắc:**
```
✅ Thông thường: 1 control class cho 1 use case
✅ Dùng control class khi use case phức tạp, nhiều bước logic
✅ Dùng control class khi cần điều phối nhiều entity/boundary classes
⚪ Có thể bỏ qua control class nếu use case chỉ thao tác đơn giản
   trên stored information (CRUD đơn giản)
```

**Ví dụ** (Register For Course):
```
UC: Register for Courses → RegistrationController
```

---

## 4. QUY TẮC PHÂN PHỐI TRÁCH NHIỆM VÀO CLASSES

Khi phân phối hành vi (allocating responsibilities), áp dụng theo thứ tự:

```
1. Hành vi liên quan đến giao tiếp với actor
   → Giao cho <<boundary>> class

2. Hành vi liên quan đến dữ liệu của một abstraction
   → Giao cho <<entity>> class sở hữu dữ liệu đó

3. Hành vi đặc thù của use case / logic phức tạp
   → Giao cho <<control>> class
```

**Khi dữ liệu nằm ở nhiều class:**

| Tình huống | Giải pháp |
|-----------|----------|
| 1 class có dữ liệu | Đặt responsibility vào class đó |
| Nhiều class có dữ liệu | Chọn 1 class chính, thêm relationship sang class kia |
| Logic quá phức tạp | Tạo class mới (thường là control class), thêm relationships |

---

## 5. INTERACTION DIAGRAMS — QUY TẮC VẼ

Sau khi xác định classes và phân phối responsibilities, phải vẽ **Interaction Diagrams** để mô hình hóa sự cộng tác.

### 5.1. Chọn loại diagram phù hợp:

| Tiêu chí | Sequence Diagram | Communication Diagram |
|----------|-----------------|----------------------|
| **Trọng tâm** | Thứ tự thời gian của messages | Cấu trúc quan hệ giữa objects |
| **Tốt cho** | Visualize overall flow, real-time spec, kịch bản phức tạp | Visualize patterns, tất cả effects trên 1 object, brainstorming |
| **Hiển thị** | Execution occurrence, explicit sequence | Links + relationships giữa objects |
| **Chuyển đổi** | ✅ Hai loại tương đương ngữ nghĩa, có thể chuyển đổi lẫn nhau |

> ✅ **Khuyến nghị**: Dùng **Sequence Diagram** khi cần trình bày flow cho stakeholders; dùng **Communication Diagram** khi brainstorm hoặc phân tích ảnh hưởng lên object.

### 5.2. Quy tắc vẽ Sequence Diagram:

```
Thành phần bắt buộc:
  ├── Objects (lifelines) — các đối tượng tham gia
  ├── Messages — có đánh số thứ bậc (1, 1.1, 1.2, 2...)
  ├── Execution Occurrence — thanh kích hoạt
  ├── Reflexive Message — message tự gọi (nếu có)
  └── Interaction Occurrence (ref) — tham chiếu đến diagram khác

Quy tắc:
  ✅ Vẽ ít nhất cho main flow (happy path)
  ✅ Vẽ thêm diagram riêng cho từng alternative flow phức tạp
  ✅ Đánh số message theo thứ bậc (hierarchical numbering)
  ❌ Không nhồi tất cả flows vào 1 diagram — gây rối
```

### 5.3. Quy tắc vẽ Communication Diagram:

```
Thành phần bắt buộc:
  ├── Objects — các đối tượng tham gia
  ├── Links — đường nối giữa objects
  └── Messages — gắn trên links, có đánh số

Quy tắc:
  ✅ Thể hiện được tất cả relationships giữa objects
  ✅ Phù hợp khi cần xem tổng thể ảnh hưởng lên một object cụ thể
```

### 5.4. Khi nào cần nhiều Interaction Diagrams?

```
Main Flow       → 1 diagram chính
Alt Flow 1      → 1 diagram riêng (nếu đủ phức tạp)
Alt Flow 2      → 1 diagram riêng
Exceptional Flow → 1 diagram riêng
...

Nguyên tắc: 1 diagram = 1 scenario rõ ràng
```

---

## 6. QUY TRÌNH THỰC HIỆN USE CASE ANALYSIS (Cho mỗi Use Case)

```
Bước 1: Đọc Use-Case Flow of Events
         └── Bổ sung chi tiết nếu còn mơ hồ (supplement the UC spec)

Bước 2: Tìm Boundary Classes
         └── 1 boundary / cặp (Actor, UC)

Bước 3: Tìm Entity Classes
         └── Gạch danh từ → lọc → candidate entities

Bước 4: Tìm Control Classes
         └── 1 control / UC phức tạp

Bước 5: Phân phối responsibilities vào các classes
         └── Dựa theo stereotype: boundary/entity/control

Bước 6: Vẽ Sequence Diagram (main flow)
         └── Thể hiện đúng thứ tự message giữa các objects

Bước 7: Vẽ thêm diagrams cho alternative flows (nếu cần)

Bước 8: Review theo checklist
```

---

## 7. CHECKLIST REVIEW — BẮT BUỘC TRƯỚC KHI CHUYỂN SANG DESIGN

### Kiểm tra Analysis Classes:
- [ ] Các classes có hợp lý không?
- [ ] Tên mỗi class có phản ánh rõ vai trò của nó không?
- [ ] Mỗi class có đại diện cho **một abstraction duy nhất** không?
- [ ] Tất cả responsibilities có **cohesion** (liên quan với nhau) không?
- [ ] Class có cung cấp đủ behavior cần thiết không?
- [ ] Tất cả specific requirements đã được address chưa?

### Kiểm tra Interaction Diagrams:
- [ ] Tất cả main flow và sub-flow đã được xử lý chưa (kể cả exceptional cases)?
- [ ] Tất cả required objects đã được tìm thấy chưa?
- [ ] Tất cả behaviors đã được phân phối **rõ ràng, không mơ hồ** chưa?
- [ ] Behaviors đã được phân phối **đúng class** chưa?
- [ ] Nếu có nhiều interaction diagrams, mối quan hệ giữa chúng có **rõ ràng và nhất quán** không?

---

## 8. TÓM TẮT NHANH — MAPPING USE CASE → CLASSES

```
Từ Use Case Specification:

  ACTOR tương tác với UC
      → <<boundary>> class (1 per actor/UC pair)

  DANH TỪ trong flow of events (sau khi lọc)
      → <<entity>> class

  LOGIC ĐIỀU PHỐI phức tạp của UC
      → <<control>> class (1 per complex UC)

  Toàn bộ BEHAVIOR của UC
      → Sequence Diagram + Communication Diagram
      → = Use-Case Realization
```

---

## 9. VÍ DỤ NHANH — COURSE REGISTRATION

```
Use Case: Register for Courses
Actors: Student, Course Catalog System

Boundary Classes:
  ├── RegisterForCoursesForm    (Student ↔ UC)
  └── CourseCatalogSystem       (Course Catalog ↔ UC)

Entity Classes:
  ├── Student
  ├── CourseOffering
  └── Schedule

Control Class:
  └── RegistrationController

Use-Case Realization:
  ├── Sequence Diagram (main flow: student selects courses → system saves schedule)
  ├── Sequence Diagram (alt flow: course full → notify student)
  └── Class Diagram (Student, CourseOffering, Schedule, RegistrationController)
```
