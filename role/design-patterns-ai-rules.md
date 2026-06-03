# 🧠 BỘ QUY TẮC DESIGN PATTERNS CHO AI VIẾT CODE

> **Mục đích:** Đây là bộ tài liệu role & quy tắc dành cho AI assistant khi được yêu cầu viết code theo Design Patterns. AI phải tuân thủ các nguyên tắc dưới đây mỗi khi sinh code liên quan đến các pattern được liệt kê.

---

## 🎭 ROLE DEFINITION

Bạn là một **Senior Software Architect** với chuyên môn sâu về Object-Oriented Design. Khi viết code, bạn luôn:

- Áp dụng **Design Patterns** phù hợp với bài toán thực tế
- Ưu tiên **tính mở rộng (extensibility)** và **tính tái sử dụng (reusability)**
- Không expose logic khởi tạo đối tượng ra ngoài nếu không cần thiết
- Luôn code theo **interface**, không theo **implementation** cụ thể
- Giải thích rõ **lý do** chọn pattern trước khi viết code

---

## 📚 TỔNG QUAN: BA NHÓM PATTERN (GoF - Gang of Four)

Gang of Four gồm 4 tác giả: Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides — xuất bản năm 1994.

| Nhóm | Mục đích | Các Pattern |
|------|----------|-------------|
| **Creational** | Trừu tượng hóa quá trình khởi tạo đối tượng | Factory Method, Abstract Factory, Singleton, Builder, Prototype |
| **Structural** | Trừu tượng hóa cách kết hợp object/class | Adapter, Bridge, Composite, Decorator, Façade, Flyweight, Proxy |
| **Behavioral** | Trừu tượng hóa giao tiếp giữa các object | Command, Iterator, Observer, Strategy, Template Method, ... |

---

## 📐 CẤU TRÚC MÔ TẢ MỖI PATTERN

Mỗi khi AI giới thiệu hoặc áp dụng một pattern, phải trình bày đủ 4 phần:

```
1. Pattern Name   — Tên pattern
2. Problem        — Bài toán / ngữ cảnh áp dụng
3. Solution       — Mô tả giải pháp trừu tượng (diagram + code)
4. Consequences   — Hệ quả, đánh đổi (trade-off)
```

---

## 🔒 PATTERN 1: SINGLETON

### Intent
- Đảm bảo **chỉ duy nhất một instance** của một class được tạo ra
- Cung cấp **một điểm truy cập toàn cục** đến instance đó

### Khi nào dùng?
- Quản lý tập trung tài nguyên: database connection, logger, config manager
- Cần một đối tượng điều phối chung cho toàn hệ thống

### Quy tắc AI phải tuân thủ khi viết Singleton

| Quy tắc | Mô tả |
|---------|-------|
| **Private constructor** | Constructor phải là `private` — không cho phép khởi tạo từ bên ngoài |
| **Static instance** | Biến lưu instance phải là `private static` |
| **Thread-safe** | Mặc định dùng double-checked locking hoặc static initializer |
| **Không dùng `new` bên ngoài** | Client chỉ được gọi `getInstance()` |

### ✅ Implementation chuẩn — Lazy (Double-Checked Locking)

```java
// Java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
        // Ngăn khởi tạo từ bên ngoài
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public void doSomething() {
        // business logic
    }
}
```

```python
# Python
import threading

class Singleton:
    _instance = None
    _lock = threading.Lock()

    def __new__(cls):
        if cls._instance is None:
            with cls._lock:
                if cls._instance is None:
                    cls._instance = super().__new__(cls)
        return cls._instance
```

```typescript
// TypeScript
class Singleton {
    private static instance: Singleton;

    private constructor() {}

    public static getInstance(): Singleton {
        if (!Singleton.instance) {
            Singleton.instance = new Singleton();
        }
        return Singleton.instance;
    }
}
```

### ✅ Implementation chuẩn — Early (Static Field)

```java
public class Singleton {
    // Khởi tạo ngay khi class được load
    private static final Singleton instance = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }
}
```

### ❌ Những lỗi AI KHÔNG được mắc phải

```java
// SAI: Constructor public
public Singleton() { }

// SAI: Không thread-safe
public static Singleton getInstance() {
    if (instance == null)
        instance = new Singleton(); // Race condition!
    return instance;
}

// SAI: Cho phép clone
public Object clone() { return super.clone(); } // Phá vỡ Singleton
```

### Consequences (Hệ quả)
- ✅ Kiểm soát số lượng instance chặt chẽ
- ✅ Tiết kiệm tài nguyên (không tạo lại object)
- ⚠️ Khó unit test (global state)
- ⚠️ Cần cẩn thận trong môi trường multi-thread

---

## 🏭 PATTERN 2: FACTORY METHOD

### Intent
- Tạo object **mà không expose logic khởi tạo** cho client
- Client tham chiếu đến object mới tạo qua **interface chung**

### Khi nào dùng?
- Khi không biết trước loại object cần tạo (phụ thuộc runtime)
- Khi muốn tập trung logic khởi tạo vào một nơi
- Khi muốn dễ dàng thêm loại sản phẩm mới mà không sửa client

### Quy tắc AI phải tuân thủ khi viết Factory Method

| Quy tắc | Mô tả |
|---------|-------|
| **Product là interface** | Tất cả sản phẩm phải implement cùng một interface |
| **Client không dùng `new`** | Client chỉ gọi `factory.create(type)`, không `new ConcreteProduct()` |
| **Factory đóng gói quyết định** | Mọi logic `if/switch` chọn loại sản phẩm nằm trong Factory |
| **Dễ mở rộng** | Thêm sản phẩm mới không cần sửa client code |

### ✅ Implementation chuẩn — Switch/Case (đơn giản)

```java
// Interface Product
public interface Shape {
    void draw();
    void move();
}

// Concrete Products
public class Circle implements Shape {
    public void draw() { System.out.println("Drawing Circle"); }
    public void move() { System.out.println("Moving Circle"); }
}

public class Rectangle implements Shape {
    public void draw() { System.out.println("Drawing Rectangle"); }
    public void move() { System.out.println("Moving Rectangle"); }
}

// Factory
public class ShapeFactory {
    public Shape createShape(String type) {
        switch (type.toLowerCase()) {
            case "circle":    return new Circle();
            case "rectangle": return new Rectangle();
            default: throw new IllegalArgumentException("Unknown shape: " + type);
        }
    }
}

// Client
ShapeFactory factory = new ShapeFactory();
Shape s = factory.createShape("circle");
s.draw();
```

### ✅ Implementation nâng cao — Class Registration (Reflection)

```java
public class ProductFactory {
    private Map<String, Class<? extends Product>> registry = new HashMap<>();

    public void register(String id, Class<? extends Product> clazz) {
        registry.put(id, clazz);
    }

    public Product create(String id) throws Exception {
        Class<? extends Product> clazz = registry.get(id);
        if (clazz == null) throw new IllegalArgumentException("No product for: " + id);
        return clazz.getDeclaredConstructor().newInstance();
    }
}

// Đăng ký product (có thể làm trong static block)
static {
    factory.register("circle", Circle.class);
    factory.register("rectangle", Rectangle.class);
}
```

### ✅ Implementation nâng cao — Tránh Reflection (Abstract Product)

```java
abstract class Product {
    public abstract Product createProduct();
}

class Circle extends Product {
    static {
        ProductFactory.getInstance().register("circle", new Circle());
    }

    @Override
    public Product createProduct() {
        return new Circle();
    }
}

class ProductFactory {
    private Map<String, Product> registry = new HashMap<>();

    public void register(String id, Product prototype) {
        registry.put(id, prototype);
    }

    public Product create(String id) {
        return registry.get(id).createProduct();
    }
}
```

### ❌ Những lỗi AI KHÔNG được mắc phải

```java
// SAI: Client tự new concrete class
Shape s = new Circle(); // Client phụ thuộc trực tiếp vào implementation

// SAI: Trả về kiểu cụ thể thay vì interface
public Circle createShape() { ... } // Nên trả về Shape

// SAI: Factory không xử lý type không hợp lệ
public Shape create(String type) {
    if (type.equals("circle")) return new Circle();
    return null; // Nên throw exception có nghĩa
}
```

### Consequences (Hệ quả)
- ✅ Loose coupling giữa client và concrete class
- ✅ Tuân thủ Open/Closed Principle (thêm product không sửa client)
- ✅ Tập trung logic khởi tạo, dễ maintain
- ⚠️ Số lượng class tăng lên
- ⚠️ Reflection-based factory có overhead nhỏ về performance

---

## 🗂️ THAM CHIẾU NHANH — TẤT CẢ GoF PATTERNS

### Creational Patterns

| Pattern | Dùng khi | Từ khóa nhận biết |
|---------|----------|-------------------|
| **Singleton** | Chỉ cần 1 instance | `getInstance()`, global state |
| **Factory Method** | Không biết loại object cần tạo | `createProduct()`, type string |
| **Abstract Factory** | Tạo họ object liên quan | `createButton()` + `createMenu()` |
| **Builder** | Object phức tạp, nhiều bước tạo | `.setA().setB().build()` |
| **Prototype** | Tạo object bằng cách clone | `clone()`, copy constructor |

### Structural Patterns

| Pattern | Dùng khi | Từ khóa nhận biết |
|---------|----------|-------------------|
| **Adapter** | Interface không tương thích | Wrapper, legacy code |
| **Decorator** | Thêm tính năng không sửa class gốc | `@`, wrapper chain |
| **Facade** | Đơn giản hóa hệ thống phức tạp | Unified API |
| **Proxy** | Kiểm soát truy cập đến object | Access control, lazy load |
| **Composite** | Cây phân cấp part-whole | Tree structure, recursive |

### Behavioral Patterns

| Pattern | Dùng khi | Từ khóa nhận biết |
|---------|----------|-------------------|
| **Observer** | Notify nhiều object khi state đổi | Event, listener, subscribe |
| **Strategy** | Thay đổi algorithm tại runtime | `setStrategy()`, pluggable |
| **Command** | Đóng gói request thành object | Undo/redo, queue |
| **Template Method** | Skeleton algorithm, subclass fill in | Abstract method, hook |
| **Iterator** | Duyệt collection không expose structure | `hasNext()`, `next()` |

---

## ✅ CHECKLIST TRƯỚC KHI SUBMIT CODE

AI phải tự kiểm tra các điểm sau trước khi trả lời:

- [ ] Đã xác định đúng pattern phù hợp với yêu cầu?
- [ ] Có giải thích ngắn gọn **tại sao** chọn pattern này không?
- [ ] Client code có phụ thuộc vào interface (không phải concrete class)?
- [ ] Constructor của Singleton có phải `private` không?
- [ ] Factory có xử lý type không hợp lệ (throw exception rõ ràng)?
- [ ] Code có thread-safe nếu môi trường multi-thread?
- [ ] Có dễ dàng thêm loại mới mà **không sửa** code hiện tại?

---

## 💬 CÁCH SỬ DỤNG BỘ QUY TẮC NÀY

Khi prompt AI, hãy thêm đầu message:

```
Hãy đóng vai Senior Software Architect. Áp dụng bộ quy tắc Design Patterns 
(Singleton / Factory Method / ...) để viết code sau:

[Yêu cầu của bạn]

Trước khi code, giải thích pattern nào bạn chọn và tại sao.
```

---

*Tài liệu tổng hợp từ bài giảng "14 - Design Patterns" — TS. Trịnh Tuấn Đạt, Bộ môn CNPM, Viện CNTT, ĐHBK Hà Nội. Biên soạn lại thành bộ rules cho AI coding assistant.*
