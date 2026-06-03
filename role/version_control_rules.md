# VERSION CONTROL RULES FOR AI CODE GENERATION
> Dựa trên tài liệu IT4490 – Software Design and Construction

---

## 1. REPOSITORY & SETUP

- Mọi project **phải** được quản lý bằng version control (Git).
- Luôn **clone** từ remote repository trước khi làm việc:
  ```bash
  git clone [remote_url]
  ```
- Khởi tạo local repo mới (nếu chưa có):
  ```bash
  git init
  git remote add origin [remote_url]
  ```

---

## 2. CẤU TRÚC THƯ MỤC

Tổ chức repo theo chuẩn:
```
project-root/
├── Homework01/
│   ├── MemberA-StudentID/
│   ├── MemberB-StudentID/
│   └── All/
├── Homework02/
└── ...
```
- Mỗi thành viên có **thư mục riêng** theo format: `MSSV-HoTen`
- Không để file lộn xộn ở root.

---

## 3. MÔ HÌNH LÀM VIỆC (WORKFLOW MODEL)

### Áp dụng mô hình **Distributed Version Control (Git)**:

| Bước | Hành động | Lệnh |
|------|-----------|-------|
| 1 | Lấy code mới nhất về | `git pull` |
| 2 | Chỉnh sửa code local | *(edit files)* |
| 3 | Kiểm tra thay đổi | `git status` |
| 4 | Thêm file vào staging | `git add .` hoặc `git add [file]` |
| 5 | Commit vào local repo | `git commit -m "[message]"` |
| 6 | Đẩy lên remote | `git push origin [branch]` |

---

## 4. QUY TẮC COMMIT

- **Luôn pull trước khi push** để tránh conflict.
- Commit message phải **có nghĩa**, mô tả rõ thay đổi:
  ```
  ✅ git commit -m "Add Calculator UI with MVC pattern"
  ❌ git commit -m "fix" / "update" / "aaa"
  ```
- Mỗi commit chỉ nên chứa **một nhóm thay đổi liên quan** (Change Set).
- Không commit **code bị lỗi** lên nhánh chính.

---

## 5. XỬ LÝ CONFLICT

- Khi conflict xảy ra:
  1. Chạy `git pull` → Git thông báo file bị conflict
  2. Mở file, tìm markers `<<<<<<`, `======`, `>>>>>>>`
  3. Chỉnh sửa thủ công để giữ đúng logic
  4. Sau khi resolve: `git add [file]` → `git commit`
- **Không được** xóa code của người khác khi resolve conflict mà không hỏi.

---

## 6. BRANCHING

- Luôn tạo **branch riêng** cho mỗi tính năng hoặc task:
  ```bash
  git checkout -b feature/ten-tinh-nang
  ```
- Sau khi hoàn thành, merge vào nhánh chính:
  ```bash
  git checkout main
  git merge feature/ten-tinh-nang
  ```
- Xóa branch sau khi merge xong:
  ```bash
  git branch -d feature/ten-tinh-nang
  ```

---

## 7. TAGGING / LABELING

- Đánh tag cho các mốc release quan trọng:
  ```bash
  git tag -a v1.0 -m "Release version 1.0"
  git push origin v1.0
  ```

---

## 8. QUY TẮC CỘNG TÁC NHÓM

- **Không push thẳng** lên `main`/`master` — dùng Pull Request.
- Mỗi thành viên phải thực hiện đủ các hành động:
  `add` · `remove` · `modify` · `commit` · `push` · `pull` · `merge` · `branch`
- Reviewer phải **review code** trước khi merge vào nhánh chính.
- Thêm tất cả thành viên nhóm vào repository với quyền phù hợp.

---

## 9. FILE CẦN IGNORE

Tạo file `.gitignore` để loại trừ các file không cần thiết:
```
# Build output
/target/
/build/
*.class
*.jar

# IDE files
.idea/
.vscode/
*.iml

# OS files
.DS_Store
Thumbs.db

# Logs
*.log
```

---

## 10. PLATFORM & TOOLS

| Mục đích | Tool |
|----------|------|
| Hosting code | GitHub / Bitbucket |
| GUI Client | GitHub Desktop (người mới) |
| CLI | Git Bash |
| So sánh diff | jDiff, TortoiseMerge |
| CI/CD | Tích hợp qua GitHub Actions |

---

## TÓM TẮT NHANH (CHEAT SHEET)

```bash
git clone [url]          # Lấy repo về lần đầu
git pull                 # Cập nhật code mới nhất
git status               # Xem trạng thái thay đổi
git add .                # Thêm tất cả file vào staging
git commit -m "msg"      # Commit local
git push origin main     # Đẩy lên remote
git checkout -b [branch] # Tạo branch mới
git merge [branch]       # Merge branch vào nhánh hiện tại
git log --oneline        # Xem lịch sử commit
git revert HEAD          # Hoàn tác commit gần nhất
```
