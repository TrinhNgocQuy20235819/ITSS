# REQUIREMENT MODELING WITH USE CASE — RULES FOR AI CODE GENERATION
> Dựa trên tài liệu IT4490 – Software Design and Construction (Lecture 3)

---

## 1. MỤC ĐÍCH CỦA REQUIREMENTS (Bắt buộc trước khi code)

Trước khi viết bất kỳ dòng code nào, AI phải đảm bảo requirements đã đáp ứng 6 mục đích sau:

- [ ] Đã thỏa thuận với khách hàng về **phần mềm cần làm gì**
- [ ] Developers đã **hiểu rõ yêu cầu** của phần mềm
- [ ] Đã **giới hạn phạm vi** (scope) của phần mềm
- [ ] Có cơ sở để **lập kế hoạch kỹ thuật** cho từng iteration
- [ ] Có cơ sở để **ước lượng chi phí và thời gian**
- [ ] Đã **xác định giao diện người dùng** (user interface)

---

## 2. BỘ TÀI LIỆU REQUIREMENTS BẮT BUỘC

Mỗi dự án phải có đủ **4 artifact** sau trước khi bắt đầu implement:

```
┌─────────────────────────────────────────────┐
│             Requirements Artifacts          │
│                                             │
│  1. Use-Case Model (Actors + Use Cases)     │
│  2. Use-Case Specifications (per UC)        │
│  3. Glossary                                │
│  4. Supplementary Specification             │
└─────────────────────────────────────────────┘
```

---

## 3. QUY TẮC XÁC ĐỊNH ACTOR

### Định nghĩa
Actor là bất kỳ thực thể nào **tương tác với phần mềm** từ bên ngoài (human, machine, software khác, database, thiết bị ngoại vi).

### Cách tìm Actor — đặt các câu hỏi:
- Ai/cái gì **sử dụng** phần mềm?
- Ai/cái gì **nhận thông tin** từ phần mềm?
- Ai/cái gì **cung cấp thông tin** cho phần mềm?
- Ai/cái gì **vận hành và bảo trì** phần mềm?
- Phần mềm nào khác **sử dụng** phần mềm này?

### Quy tắc đặt tên Actor:
- Tên phải **mô tả rõ vai trò** (role) của actor
- Tên phải **trực quan**, dễ hiểu với cả khách hàng lẫn developer
- Chú ý **danh từ** trong mô tả bài toán → candidate actor

### Quy tắc quan hệ Actor:
| Quan hệ | Ý nghĩa | Khi dùng |
|---------|---------|---------|
| **Association** | Actor giao tiếp với use case | Mặc định |
| **Generalization** | Actor con kế thừa role của actor cha | Khi nhiều role có chung hành vi |

- Mũi tên chỉ **người khởi tạo** tương tác
- Không có mũi tên → cả hai đều có thể khởi tạo
- Mỗi actor **phải liên kết ít nhất 1 use case**

### Checklist Actor:
- [ ] Đã xác định tất cả actors chưa?
- [ ] Mỗi actor có liên kết ít nhất 1 use case không?
- [ ] Mỗi actor có thực sự là một **role** không (không phải người cụ thể)?
- [ ] Có actor nào cần tách/gộp không?
- [ ] Tên actor có đủ rõ nghĩa không?

---

## 4. QUY TẮC XÁC ĐỊNH USE CASE

### Định nghĩa
Use case = một chuỗi hành động mà phần mềm thực hiện, tạo ra **kết quả có giá trị** cho một actor cụ thể.

### Cách tìm Use Case — đặt các câu hỏi:
- Mục tiêu của từng actor khi dùng phần mềm là gì?
- Actor cần **tạo / đọc / sửa / xóa** dữ liệu gì?
- Actor cần **thông báo** cho phần mềm về sự kiện gì?
- Phần mềm cần **thông báo** cho actor về điều gì?

### Quy tắc đặt tên Use Case:
```
✅ Format: [Động từ] + [Danh từ]
   Ví dụ: "Login", "Register for Courses", "Submit Grades", "Query Balance"

❌ Không dùng: tên mơ hồ như "Process", "Handle", "Manage" (thiếu danh từ)
```

- Tên phải **duy nhất, trực quan, tự giải thích**
- Phản ánh góc nhìn của **actor** (không phải hệ thống)
- Cả khách hàng lẫn developer đều **hiểu được**
- Chú ý **động từ** trong mô tả bài toán → candidate use case

### Checklist Use Case:
- [ ] Mỗi use case liên kết ít nhất 1 actor?
- [ ] Mỗi use case có độc lập với use case khác không?
- [ ] Có use case nào có flow quá giống nhau không? (có thể gộp/tách)
- [ ] Tên use case có đủ rõ và không bị nhầm lẫn không?

---

## 5. QUY TẮC QUAN HỆ GIỮA CÁC USE CASE

### 3 loại quan hệ — dùng đúng mục đích:

#### `<<include>>` — BẮT BUỘC, luôn xảy ra
```
Dùng khi: UC con luôn được gọi bởi UC cha (bắt buộc, không optional)
Mục đích: Tái sử dụng hành vi chung
Rule: UC cha KHÔNG hoàn chỉnh nếu thiếu UC được include

Ví dụ:
  Place Order ──<<include>>──> Validate User
  Withdraw Funds ──<<include>>──> Update Balance
```

#### `<<extend>>` — TÙY ĐIỀU KIỆN, có thể xảy ra
```
Dùng khi: UC mở rộng THÊM hành vi vào UC gốc (optional, có điều kiện)
Rule: UC gốc vẫn hoàn chỉnh và có nghĩa khi không có phần extend

Ví dụ:
  Place Order <──<<extend>>── Place Rush Order
  (chỉ extend khi khách hàng chọn rush order)
```

#### Generalization — KẾ THỪA hành vi
```
Dùng khi: UC con kế thừa và có thể override UC cha
Rule: Cả UC cha và UC con đều có thể có instance riêng

Ví dụ:
  Validate User ←── Check Password
  Validate User ←── Retinal Scan
```

### Lưu ý quan trọng:
> ❌ **Không dùng quá nhiều quan hệ** — làm diagram rối, khó đọc
> ✅ Chỉ thêm quan hệ khi **thực sự cần thiết**
> ✅ Use case diagram **không** chỉ định thứ tự thực hiện

---

## 6. USE CASE SPECIFICATION — CẤU TRÚC BẮT BUỘC

Mỗi Use Case phải có đầy đủ các mục sau:

```markdown
## UC-[ID]: [Tên Use Case]

**Brief Description**: [1-2 câu mô tả mục đích]

**Actors**: [Danh sách actor liên quan]

**Pre-conditions**: [Điều kiện phải đúng TRƯỚC khi UC bắt đầu]

**Post-conditions**: [Trạng thái hệ thống SAU khi UC kết thúc thành công]

**Flow of Events**:

| # | Doer   | Action                    |
|---|--------|---------------------------|
| 1 | Actor  | [Hành động của actor]     |
| 2 | System | [Phản hồi của hệ thống]   |
| 3 | Actor  | [Tiếp theo...]            |

**Alternative Flows**:
- [ID]a: [Regular variant — biến thể bình thường]
- [ID]b: [Odd case — trường hợp bất thường]
- [ID]c: [Exceptional flow — luồng lỗi/ngoại lệ]

**Special Requirements**: [Yêu cầu phi chức năng riêng của UC này]

**Relationships**: <<include>> / <<extend>> / generalization
```

### Quy tắc viết Flow of Events:
- Phải có **1 main flow** (happy path)
- Phải có **alternative flows**: regular variants, odd cases, exceptional flows
- Mỗi bước chỉ rõ **ai làm gì** (actor hay system)
- Flow phải có **điểm bắt đầu** và **điểm kết thúc** rõ ràng

### Checklist Use Case Specification:
- [ ] Rõ ai muốn thực hiện UC này?
- [ ] Mục đích UC có rõ ràng không?
- [ ] Brief description có phản ánh đúng UC không?
- [ ] UC bắt đầu và kết thúc khi nào?
- [ ] Tương tác giữa actor và hệ thống có rõ không?
- [ ] UC có quá phức tạp không? (nếu có → tách nhỏ)

---

## 7. GLOSSARY — QUY TẮC THUẬT NGỮ

### Mục đích:
Định nghĩa các thuật ngữ quan trọng để **developer và domain expert hiểu nhau**.

### Quy tắc:
- Mỗi dự án chỉ có **1 Glossary duy nhất**
- Mỗi thuật ngữ phải có định nghĩa **rõ ràng, không mơ hồ**
- Thuật ngữ phải được dùng **nhất quán** trong toàn bộ tài liệu

### Template Glossary:

```markdown
# Glossary — [Tên Project]

## 1. Introduction
[Mô tả ngắn mục đích của Glossary]

## 2. Definitions

| Thuật ngữ | Định nghĩa |
|-----------|-----------|
| [Term 1]  | [Định nghĩa đầy đủ, rõ ràng] |
| [Term 2]  | [Định nghĩa đầy đủ, rõ ràng] |
```

### Checklist Glossary:
- [ ] Mỗi thuật ngữ có định nghĩa rõ và ngắn gọn không?
- [ ] Mỗi thuật ngữ trong Glossary có xuất hiện trong UC descriptions không?
- [ ] Thuật ngữ được dùng **nhất quán** trong toàn bộ tài liệu không?

---

## 8. SUPPLEMENTARY SPECIFICATION — YÊU CẦU PHI CHỨC NĂNG

Các yêu cầu **không được capture** trong use cases phải ghi vào đây.

### Cấu trúc (FURPS):

```markdown
# Supplementary Specification — [Tên Project]

## 1. Functionality (Chức năng dùng chung)
[VD: System Error Logging, Remote Access, Authentication chung...]

## 2. Usability (Khả năng sử dụng)
[VD: Tuân thủ Windows UI guidelines, không cần training, có online help...]

## 3. Reliability (Độ tin cậy)
[VD: MTBF ≥ X giờ, defect rate ≤ Y per KLOC, uptime 99.9%...]

## 4. Performance (Hiệu năng)
[VD: Response time ≤ 2s, hỗ trợ N concurrent users...]

## 5. Supportability (Khả năng bảo trì)
[VD: Code phải có comment, tuân theo coding standard X...]
```

---

## 9. CHECKLIST TỔNG THỂ TRƯỚC KHI CODE

```
REQUIREMENTS CHECKLIST
======================
[ ] Use-Case Model đã đủ và dễ hiểu chưa?
[ ] Tất cả functional requirements đã được capture vào UC chưa?
[ ] Không có UC thừa (superfluous behavior) không?
[ ] Mỗi actor liên kết ít nhất 1 UC?
[ ] Mỗi UC liên kết ít nhất 1 actor?
[ ] Tất cả UC Specifications đã có main flow + alternative flows?
[ ] Pre/Post conditions đã được định nghĩa cho mỗi UC?
[ ] Glossary đã có đủ các thuật ngữ domain-specific?
[ ] Supplementary Specification đã cover FURPS?
[ ] Khách hàng đã review và đồng ý với Use-Case Model chưa?
```

---

## 10. TÓM TẮT NHANH — KHI AI PHÂN TÍCH BÀI TOÁN

```
Bước 1: Đọc mô tả bài toán
        → Danh từ  = Actor candidate
        → Động từ  = Use Case candidate

Bước 2: Lọc Actors
        → Phải là EXTERNAL (không phải bên trong hệ thống)
        → Phải tương tác trực tiếp với phần mềm

Bước 3: Lọc Use Cases
        → Phải tạo ra kết quả CÓ GIÁ TRỊ cho actor
        → Tên: [Verb] + [Noun]

Bước 4: Xác định quan hệ
        → <<include>>: hành vi BẮT BUỘC, dùng lại
        → <<extend>>: hành vi TÙY CHỌN, có điều kiện
        → Generalization: kế thừa role/behavior

Bước 5: Viết UC Specification cho từng UC
        → Main flow + Alternative flows

Bước 6: Viết Glossary + Supplementary Specification

Bước 7: Review toàn bộ với checklist → mới bắt đầu code
```
