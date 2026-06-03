# CHEATSHEET — Identify Design Elements
# (In ra dán cạnh màn hình khi làm việc)

## STEREOTYPE → CODE

| Stereotype     | Ví dụ tên class              | Tầng          | Giao tiếp với         |
|----------------|------------------------------|---------------|-----------------------|
| `<<boundary>>` | LoginForm, RegisterForm      | Presentation  | Actor (user/system)   |
| `<<control>>`  | LoginController, RegService  | Application   | Boundary + Entity     |
| `<<entity>>`   | Student, Course, Schedule    | Domain        | Control (chỉ)         |
| `<<interface>>`| IBillingSystem, ICatalog     | Infrastructure| External systems      |

---

## PACKAGE DECISION TREE

```
Hai class có liên quan nhau không?
├── Thay đổi cùng nhau?          → Cùng package ✅
├── Cùng actor?                  → Cùng package ✅
├── Boundary trình bày Entity?   → Cùng package ✅ (nếu interface ít thay đổi)
├── Interface hay thay đổi?      → Boundary vào package RIÊNG ✅
├── Khác actor?                  → Package RIÊNG ✅
└── Một bắt buộc, một tùy chọn? → Package RIÊNG ✅
```

---

## COUPLING RULES

```
✅ ĐÚNG:          ❌ SAI:
A → B → C        A ↔ B  (circular)
                 C → A  (lower → upper)
                 A → C  (skip layer B)
```

---

## VISIBILITY

```
PackageA:
  + ClassA1  (public)   ← Có thể dùng từ PackageB
  + ClassA2  (public)   ← Có thể dùng từ PackageB
  - ClassA3  (private)  ← CHỈ dùng trong PackageA

PackageB:
  + ClassB1  (public)
  - ClassB2  (private)  ← ClassA không thể dùng ClassB2
```

---

## ANALYSIS → DESIGN (MAPPING TYPES)

```
Simple class → 1 Design Class

Complex class → Option 1: Split thành 2+ classes
             → Option 2: Trở thành Package
             → Option 3: Trở thành Subsystem
             → Option 4: Kết hợp các option trên
```

---

## LAYER ARCHITECTURE

```
┌────────────────────────┐
│  <<boundary>> (Forms)  │  ← Nhận input từ User
├────────────────────────┤
│  <<control>> (Services)│  ← Xử lý business logic
├────────────────────────┤
│  <<entity>> (Models)   │  ← Quản lý data
├────────────────────────┤
│  <<interface>> (Infra) │  ← Kết nối external systems
└────────────────────────┘
         ↓ (dependency chỉ đi 1 chiều)
```
