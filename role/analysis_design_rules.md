# ANALYSIS & DESIGN RULES FOR AI CODE GENERATION
> Dựa trên tài liệu IT4490 – Software Design and Construction (Lecture 4)

---

## 1. MỤC ĐÍCH CỦA ANALYSIS & DESIGN

Trước khi viết code, AI phải thực hiện đủ 3 mục tiêu:

- [ ] **Chuyển hóa requirements** thành thiết kế hệ thống cụ thể
- [ ] **Xây dựng kiến trúc** vững chắc, có khả năng mở rộng
- [ ] **Thích nghi thiết kế** với môi trường implementation (performance, platform)

---

## 2. PHÂN BIỆT ANALYSIS VỀ DESIGN — KHÔNG ĐƯỢC LẪN LỘN

| Tiêu chí | Analysis | Design |
|----------|----------|--------|
| **Trọng tâm** | Hiểu **vấn đề** | Hiểu **giải pháp** |
| **Loại yêu cầu** | Functional requirements | Non-functional requirements |
| **Mức độ** | Thiết kế lý tưởng (idealized) | Gần với code thực tế |
| **Quan tâm đến** | Behavior, system structure | Operations, attributes, performance |
| **Vòng đời object** | Không | Có (object lifecycles) |
| **Quy mô model** | Model nhỏ | Model lớn, chi tiết |

> ✅ **Quy tắc**: Làm Analysis trước → hiểu xong bài toán → mới chuyển sang Design → mới code.

---

## 3. INPUT & OUTPUT CỦA ANALYSIS & DESIGN

```
INPUT:
  ├── Use-Case Model          (từ Requirements)
  ├── Glossary                (từ Requirements)
  └── Supplementary Spec      (từ Requirements)

         ▼  Analysis & Design  ▼

OUTPUT:
  ├── Design Model            ← thiết kế class, component, subsystem
  ├── Data Model              ← thiết kế database
  └── Architecture Document   ← quyết định kiến trúc tổng thể
```

---

## 4. KIẾN TRÚC PHẦN MỀM — QUY TẮC BẮT BUỘC

### 4.1. Định nghĩa Architecture
Architecture = tập hợp các **quyết định chiến lược** về tổ chức phần mềm, bao gồm:
- Chọn các **structural elements** và interface giữa chúng
- Định nghĩa **behavior** qua collaboration giữa các elements
- Tổ hợp thành các **subsystems** lớn hơn
- Chọn **architectural style** phù hợp

### 4.2. Nguyên tắc quan trọng nhất:
```
Architecture → constrain → Design → constrain → Implementation → Code

⚠️  Quyết định kiến trúc là QUAN TRỌNG NHẤT.
    Thay đổi architecture sẽ ảnh hưởng toàn bộ hệ thống.
    Phải được quyết định đầu tiên và cẩn thận nhất.
```

### 4.3. Mô hình "4+1 Views" — BẮT BUỘC khi thiết kế hệ thống lớn

| View | Mô tả | Đối tượng quan tâm |
|------|-------|-------------------|
| **Logical View** | Cấu trúc hệ thống, class diagram | Analysts / Designers |
| **Process View** | Performance, scalability, threading | System integrators |
| **Implementation View** | Quản lý source code, modules | Programmers |
| **Deployment View** | Topology mạng, cài đặt, giao tiếp | System engineering |
| **Use-Case View** *(+1)* | Chức năng từ góc nhìn end-user | End-users |

> ✅ Use-Case View là trung tâm — 4 view còn lại phải nhất quán với nó.

---

## 5. QUY TRÌNH 10 BƯỚC ANALYSIS & DESIGN

### Giai đoạn ANALYSIS:

| Bước | Tên | Mô tả | Người thực hiện |
|------|-----|-------|----------------|
| 1 | **Architectural Analysis** | Xác định candidate architecture (chỉ làm 1 lần ở Elaboration) | Architect |
| 2 | **Use Case Analysis** | Phân tích từng use case thành analysis classes | Designer |

### Giai đoạn DESIGN (Refine Architecture):

| Bước | Tên | Mô tả | Người thực hiện |
|------|-----|-------|----------------|
| 3 | **Identify Design Elements** | Xác định coupling, cohesion, reusability | Architect |
| 4 | **Identify Design Mechanisms** | Áp dụng design patterns phù hợp | Architect |
| 5 | **Describe Run-time Architecture** | Mô tả process view (bỏ qua nếu không multi-threading) | Architect |
| 6 | **Describe Distribution** | Mô tả physical architecture | Architect |

### Giai đoạn DESIGN (Components):

| Bước | Tên | Mô tả | Người thực hiện |
|------|-----|-------|----------------|
| 7 | **Use Case Design** | Design từng use case thành sequence/communication diagram | Designer |
| 8 | **Subsystem Design** | Thiết kế subsystem | Designer |
| 9 | **Class Design** | Thiết kế chi tiết class | Designer |
| 10 | **Database Design** | Thiết kế database (optional) | Designer |

---

## 6. USE-CASE DRIVEN — QUY TẮC CỐT LÕI

> **Use cases là nền tảng của toàn bộ quá trình phát triển.**

- Mọi Analysis và Design phải **truy xuất được** về một use case cụ thể
- Use cases giúp **đồng bộ** nội dung giữa các model khác nhau
- Không được thiết kế class/component nào **không liên quan** đến bất kỳ use case nào

### Use-Case Realization:
```
Use Case (Use-Case Model)
    │
    ▼ "hiện thực hóa thành"
Use-Case Realization (Design Model)
    ├── Sequence Diagrams     ← mô tả luồng tương tác theo thời gian
    ├── Communication Diagrams ← mô tả quan hệ giữa objects
    └── Class Diagrams         ← mô tả cấu trúc classes
```

---

## 7. QUY TẮC THIẾT KẾ CLASS (Design Elements)

Khi identify design elements (Bước 3), AI phải đảm bảo:

- **Low Coupling**: mỗi class phụ thuộc càng ít class khác càng tốt
- **High Cohesion**: mỗi class chỉ chịu trách nhiệm một nhóm chức năng liên quan
- **Reusability**: class có thể tái sử dụng ở nhiều use case khác nhau
- Analysis và Design **không hoàn toàn top-down cũng không hoàn toàn bottom-up** — định nghĩa ở tầng giữa (middle level) rồi mở rộng hai chiều

---

## 8. PHÂN CÔNG TRÁCH NHIỆM

### Software Architect chịu trách nhiệm:
- [ ] Dẫn dắt và điều phối các hoạt động kỹ thuật
- [ ] Tạo và duy trì **Software Architecture Document**
- [ ] Quản lý: Analysis Model, Design Model, Implementation Model, Deployment Model
- [ ] Quyết định architecture, design patterns, design mechanisms

### Designer chịu trách nhiệm:
- [ ] Nắm vững use-case modeling và software design techniques
- [ ] Thực hiện **Use-Case Realization** cho từng use case
- [ ] Thiết kế packages, classes, subsystems

---

## 9. QUY TẮC ITERATIVE DESIGN

Trong mỗi iteration:

```
Iteration N:
  ├── Chọn 1-2 Use Cases quan trọng nhất
  ├── Làm Use-Case Realization cho các scenarios đó
  └── Kết thúc iteration với Use-Case Realization hoàn chỉnh

Iteration N+1:
  ├── Mở rộng Use-Case Realization từ iteration trước (thêm scenario)
  ├── Thêm Use-Case Realization mới cho use case khác
  └── Refine architecture nếu cần
```

> ✅ Mỗi iteration phải cho ra **một bản design có thể implement được**.
> ❌ Không design toàn bộ hệ thống trước khi bắt đầu code bất cứ thứ gì.

---

## 10. CHECKLIST TRƯỚC KHI CHUYỂN SANG CODE

```
ANALYSIS & DESIGN CHECKLIST
============================
[ ] Đã có đủ Use-Case Model, Glossary, Supplementary Spec chưa?
[ ] Đã định nghĩa candidate architecture chưa?
[ ] Đã phân tích ít nhất Use Cases ưu tiên cao nhất chưa?
[ ] Đã có Architecture Document với 4+1 Views chưa?
[ ] Design Model có đủ Sequence/Communication/Class Diagrams không?
[ ] Mỗi Design Element có truy xuất về Use Case nào không?
[ ] Coupling thấp, Cohesion cao chưa?
[ ] Đã xác định design patterns/mechanisms phù hợp chưa?
[ ] Database Design đã hoàn chỉnh chưa? (nếu cần)
[ ] Architecture decisions đã được review và phê duyệt chưa?
```

---

## TÓM TẮT NHANH

```
Requirements (Use-Case Model)
         │
         ▼
[ANALYSIS] Hiểu bài toán
  → Analysis Classes (idealized, behavior-focused)
         │
         ▼
[DESIGN] Hiểu giải pháp
  → Design Classes (performance, attributes, lifecycles)
  → 4+1 Architecture Views
  → Use-Case Realizations (Sequence + Communication + Class Diagrams)
         │
         ▼
[IMPLEMENTATION] Viết code
  → Code phải nhất quán với Design
  → Design phải nhất quán với Architecture
  → Architecture phải nhất quán với Use Cases
```
