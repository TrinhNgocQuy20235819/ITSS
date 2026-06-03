# SOFTWARE DEVELOPMENT PROCESS RULES FOR AI CODE GENERATION
> Dựa trên tài liệu IT4490 – Software Design and Construction (ISO/IEC 12207)

---

## 1. QUY TRÌNH PHÁT TRIỂN PHẦN MỀM (Software Implementation Process)

AI phải tuân theo **6 giai đoạn** theo thứ tự sau khi viết code:

```
Requirements Analysis → Architecture Design → Detailed Design
      → Construction → Integration → Qualification Testing
```

---

### Giai đoạn 1 — Requirements Analysis (Phân tích yêu cầu)

Trước khi viết bất kỳ dòng code nào, phải xác định rõ:

- [ ] Điều kiện môi trường hệ thống (OS, runtime, platform)
- [ ] Các **functional requirements** (chức năng cần có)
- [ ] Các **interface requirements** (API, UI, giao tiếp với hệ thống khác)
- [ ] Định nghĩa dữ liệu và database cần thiết
- [ ] Các **non-functional requirements**: reliability, usability, performance
- [ ] Tiêu chí nghiệm thu (qualification requirements)

> ❌ **Không được** bắt đầu code khi chưa rõ yêu cầu.

---

### Giai đoạn 2 — Architecture Design (Thiết kế kiến trúc)

Trước khi code, phải thiết kế:

- [ ] Cấu trúc tổng thể của hệ thống (top-level structure)
- [ ] Các **software components** và mối quan hệ giữa chúng
- [ ] Thiết kế **interface** giữa các component
- [ ] Thiết kế **database** ở cấp tổng thể

> ✅ Output: Sơ đồ kiến trúc hoặc mô tả cấu trúc module trước khi implement.

---

### Giai đoạn 3 — Detailed Design (Thiết kế chi tiết)

- [ ] Mỗi component được chia nhỏ thành các **software units** có thể code và test độc lập
- [ ] Thiết kế chi tiết interface giữa các units
- [ ] Mức chi tiết đủ để: **coding + testing** không cần đoán mò

> ✅ Output: Class diagram, API spec, data model chi tiết.

---

### Giai đoạn 4 — Construction (Lập trình)

Khi viết code, AI phải đảm bảo:

- [ ] Mỗi **software unit** phản ánh đúng thiết kế chi tiết
- [ ] Viết **unit test** cho mỗi unit/database
- [ ] Code đạt các tiêu chí sau:
  - **Traceability**: code truy xuất được tới yêu cầu
  - **External consistency**: nhất quán với requirements
  - **Internal consistency**: nhất quán bên trong codebase
  - **Test coverage**: đủ độ bao phủ test
  - **Coding standards**: tuân theo chuẩn code
  - **Maintainability**: dễ bảo trì, tích hợp, vận hành

---

### Giai đoạn 5 — Integration (Tích hợp)

- [ ] Có **integration plan** rõ ràng (thứ tự tích hợp, test cases)
- [ ] Tích hợp các units/components theo đúng kế hoạch
- [ ] Chạy **integration test** sau khi ghép nối
- [ ] Xác nhận cả **functional** lẫn **non-functional** requirements được đáp ứng

---

### Giai đoạn 6 — Qualification Testing (Kiểm thử nghiệm thu)

- [ ] Thực hiện test theo **qualification requirements** đã định nghĩa ở giai đoạn 1
- [ ] Lập test cases và test procedures đầy đủ
- [ ] Hỗ trợ audit để xác nhận phần mềm đạt chuẩn
- [ ] Chỉ bàn giao khi **audit thành công**

---

## 2. MÔ HÌNH PHÁT TRIỂN (Development Model)

Chọn mô hình phù hợp với dự án:

| Mô hình | Khi nào dùng | Đặc điểm |
|---------|-------------|----------|
| **Waterfall** | Yêu cầu rõ ràng, ít thay đổi | Tuyến tính, từng bước một |
| **Iterative Waterfall** | Yêu cầu tương đối rõ, cần feedback | Waterfall nhưng có vòng lặp quay lại |
| **Iterative** | Cần release sớm, liên tục | Mỗi iteration cho ra một bản executable |
| **Prototype** | Yêu cầu chưa rõ, cần confirm với khách hàng | Build prototype → khách hàng test → refine |
| **Spiral** | Dự án lớn, rủi ro cao | Mỗi vòng xoắn: plan → risk → build → review |
| **Agile** | Yêu cầu thay đổi liên tục | Sprint ngắn, deliver nhanh, phản hồi linh hoạt |

> ✅ **Mặc định dùng Agile/Iterative** cho các dự án không có yêu cầu cố định.

---

## 3. QUY TẮC AGILE CHO AI VIẾT CODE

### Agile Manifesto — 4 giá trị cốt lõi:

```
✅ Con người & tương tác  >  Quy trình & công cụ
✅ Phần mềm chạy được     >  Tài liệu đầy đủ
✅ Cộng tác với khách hàng >  Đàm phán hợp đồng
✅ Phản hồi với thay đổi  >  Bám theo kế hoạch
```

### 13 nguyên tắc Agile AI phải tuân theo:

1. **Ưu tiên thỏa mãn khách hàng** – deliver phần mềm có giá trị sớm và liên tục
2. **Chào đón thay đổi yêu cầu** – kể cả khi đang dev, thích nghi để có lợi thế
3. **Deliver thường xuyên** – từ vài tuần đến vài tháng, ưu tiên chu kỳ ngắn
4. **Business & Dev làm việc cùng nhau** – không làm việc trong "bong bóng"
5. **Tin tưởng cá nhân** – cung cấp môi trường & hỗ trợ, để team tự quyết
6. **Giao tiếp trực tiếp** – face-to-face là hiệu quả nhất
7. **Phần mềm chạy được = thước đo tiến độ** – không phải tài liệu hay % hoàn thành
8. **Phát triển bền vững** – tốc độ ổn định, không sprint rồi burn out
9. **Tốc độ nhất quán** – developer, sponsor, user cùng pace
10. **Chú trọng kỹ thuật tốt** – code sạch, thiết kế tốt → tăng agility
11. **Đơn giản hóa** – tối đa hóa lượng công việc KHÔNG cần làm
12. **Self-organizing teams** – kiến trúc tốt nhất xuất phát từ team tự tổ chức
13. **Phản tư định kỳ** – team thường xuyên review và cải tiến quy trình

---

## 4. CHECKLIST TRƯỚC KHI SINH CODE

```
[ ] Đã xác định rõ requirements chưa?
[ ] Đã thiết kế kiến trúc tổng thể chưa?
[ ] Đã chia nhỏ thành các units có thể test độc lập chưa?
[ ] Mỗi unit có unit test đi kèm chưa?
[ ] Code có tuân thủ coding standards chưa?
[ ] Có integration plan cho các components chưa?
[ ] Có qualification test cases theo requirements chưa?
```

---

## 5. TIÊU CHÍ ĐÁNH GIÁ CODE (Construction Criteria)

AI tạo ra code phải đạt **tất cả** các tiêu chí sau:

| Tiêu chí | Ý nghĩa |
|----------|---------|
| **Traceability** | Mỗi đoạn code truy xuất được về requirement nào |
| **External Consistency** | Code khớp với requirements đã định |
| **Internal Consistency** | Không mâu thuẫn trong cùng codebase |
| **Test Coverage** | Đủ test bao phủ các unit |
| **Coding Standards** | Đúng chuẩn, dễ đọc, có comment |
| **Maintainability** | Dễ bảo trì, mở rộng, tích hợp sau này |
