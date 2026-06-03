# 📐 QUY TẮC THIẾT KẾ PHẦN MỀM — TÀI LIỆU HƯỚNG DẪN AI VIẾT CODE

> **Mục đích:** Đây là bộ quy tắc bắt buộc khi AI sinh code. Mọi đoạn code được tạo ra phải tuân thủ các nguyên tắc dưới đây về Coupling (độ phụ thuộc) và Cohesion (độ gắn kết). Vi phạm bất kỳ quy tắc nào là thiết kế kém.

---

## PHẦN 1 — TRIẾT LÝ THIẾT KẾ

### 1.1 Thiết kế là gì?

Thiết kế là quá trình giải quyết vấn đề nhằm tìm ra cách:
- Hiện thực hóa các **yêu cầu chức năng** của hệ thống
- Tuân thủ các **ràng buộc phi chức năng** (hiệu năng, bảo mật, ngân sách)
- Đảm bảo **chất lượng tổng thể** của sản phẩm

### 1.2 Module là gì?

Một **module** là đơn vị thiết kế độc lập (class, package, service, function group...).

Thiết kế module tốt phải thỏa mãn 5 tiêu chí:

| Tiêu chí | Mô tả |
|---|---|
| **Decomposable** | Có thể phân rã thành các phần nhỏ hơn |
| **Composable** | Các phần nhỏ có thể ghép lại thành hệ thống |
| **Understandable** | Một module có thể đọc hiểu độc lập, không cần đọc module khác |
| **Continuity** | Thay đổi nhỏ trong yêu cầu → chỉ ảnh hưởng ít module |
| **Isolation** | Lỗi trong một module không lan sang module khác |

### 1.3 Mục tiêu chất lượng

```
Thiết kế tốt = HIGH COHESION (trong module) + LOW COUPLING (giữa các module)
```

---

## PHẦN 2 — QUY TẮC COUPLING (ĐỘ PHỤ THUỘC)

> **Định nghĩa:** Coupling là mức độ một module phụ thuộc vào các module khác.  
> **Mục tiêu:** COUPLING CÀNG THẤP CÀNG TỐT.

### 2.0 Thang đo Coupling (từ tệ → tốt)

```
Content → Common → Control → Stamp → Data → Uncoupled
  [tệ nhất]                                  [tốt nhất]
```

---

### 2.1 CONTENT COUPLING — ❌ CẤM TUYỆT ĐỐI

**Định nghĩa:** Module A trực tiếp đọc/ghi dữ liệu nội bộ của module B.

**Dấu hiệu nhận biết:**
- Truy cập trực tiếp vào field `private` của class khác (qua `friend`, reflection...)
- Một class thực hiện phép tính trên data của class khác rồi `set` lại

**❌ Code sai:**
```java
// B truy cập trực tiếp vào field private của A
class B {
    void showA(A x) {
        System.out.println(x.a); // vi phạm: a là private
    }
}

// Getter/setter vẫn vi phạm nếu logic tính toán nằm ngoài class
public int sumValues(Calculator c) {
    int result = c.getFirstNumber() + c.getSecondNumber(); // logic tính toán ở ngoài
    c.setResult(result);
    return c.getResult();
}
```

**✅ Code đúng:**
```java
// Logic tính toán nằm bên trong class sở hữu data
public int sumValues(Calculator c) {
    c.sumAndUpdateResult(); // Calculator tự tính
    return c.getResult();
}
```

**QUY TẮC C1:** Mọi field phải là `private`. Luôn cung cấp getter/setter. Logic tính toán trên data phải nằm trong class sở hữu data đó.

---

### 2.2 COMMON COUPLING — ❌ TRÁNH

**Định nghĩa:** Nhiều module cùng dùng chung một biến toàn cục (global variable / static mutable state).

**Vấn đề:** Khi bug xảy ra với biến toàn cục, không biết module nào đã thay đổi nó.

**❌ Code sai:**
```java
// Biến global, nhiều nơi truy cập và thay đổi
public static int globalCount = 0;

// Module A
globalCount++;

// Module B
while (globalCount > 0) { ... }
```

**✅ Code đúng:**
```java
// Truyền qua tham số hoặc dùng Dependency Injection
public class Counter {
    private int count;
    public void increment() { count++; }
    public int getCount() { return count; }
}
```

**QUY TẮC C2:** Không dùng biến global mutable. Truyền dependency qua constructor hoặc tham số.

---

### 2.3 CONTROL COUPLING — ⚠️ HẠN CHẾ

**Định nghĩa:** Module A truyền một "flag" để điều khiển luồng thực thi bên trong module B.

**Dấu hiệu:** Tham số có tên như `type`, `kind`, `mode`, `flag`, `whatKind`; bên trong có `switch/if` dựa trên tham số đó.

**❌ Code sai:**
```java
public void takeAction(int key) {
    switch (key) {
        case 1: System.out.println("ONE RECEIVED"); break;
        case 2: System.out.println("TWO RECEIVED"); break;
    }
}

// Caller phải biết ý nghĩa của các con số ma thuật
takeAction(1);
```

**✅ Code đúng — dùng polymorphism:**
```java
public interface Printable {
    void print();
}

public class PrinterOne implements Printable {
    @Override
    public void print() { System.out.println("ONE RECEIVED"); }
}

public class PrinterTwo implements Printable {
    @Override
    public void print() { System.out.println("TWO RECEIVED"); }
}

// Caller chọn implementation, không truyền flag
public void takeAction(Printable printable) {
    printable.print();
}
```

**✅ Code đúng — tách thành nhiều method:**
```java
// Thay vì updateCustomer(int whatKind, Customer c)
public void addCustomer(Customer c) { ... }
public void editCustomer(Customer c) { ... }
public void deleteCustomer(int customerId) { ... }

// Thay vì testStack(int kind, Stack s)
public boolean isEmptyStack(Stack s) { return s.num == 0; }
public boolean isFullStack(Stack s) { return s.num == MAX; }
```

**QUY TẮC C3:** Không truyền "magic flag" vào method. Thay bằng: (a) tách thành nhiều method riêng biệt, hoặc (b) dùng interface + polymorphism.

---

### 2.4 STAMP COUPLING — ⚠️ CẦN CHÚ Ý

**Định nghĩa:** Truyền nguyên một object/struct lớn vào method, nhưng method chỉ dùng một vài field của nó.

**❌ Code sai:**
```java
// incomeTaxPayable chỉ dùng p.salary, nhưng nhận cả Person
int incomeTaxPayable(Person p) {
    // chỉ dùng p.salary
}

// sendEmail chỉ cần name và email, nhưng nhận cả Employee
public void sendEmail(Employee e, String text) { ... }
```

**✅ Cách 1 — truyền primitive:**
```java
int incomeTaxPayable(int salary) { ... }

// Caller: incomeTaxPayable(employee.getSalary())
```

**✅ Cách 2 — dùng interface:**
```java
public interface Addressee {
    String getName();
    String getEmail();
}

public class Employee implements Addressee { ... }

public void sendEmail(Addressee addressee, String text) { ... }
// Linh hoạt: dùng được với Employee, Customer, Partner...
```

**QUY TẮC C4:** Method chỉ nhận đúng những gì nó cần. Nếu chỉ cần 1–2 field, hãy truyền trực tiếp các field đó hoặc tạo interface chứa đúng những method cần thiết.

---

### 2.5 DATA COUPLING — ✅ KHUYẾN KHÍCH

**Định nghĩa:** Truyền dữ liệu nguyên thủy (primitive types) hoặc simple objects qua tham số.

**Ví dụ tốt:**
```java
public double calculateArea(double width, double height) { ... }
public String formatName(String firstName, String lastName) { ... }
```

**Lưu ý:** Không truyền quá nhiều tham số — đây là dấu hiệu module bị phân chia kém.

**QUY TẮC C5:** Giữ số lượng tham số tối thiểu. Nếu method cần > 4 tham số, xem xét lại cách phân chia module.

---

### 2.6 Checklist đánh giá Coupling

Trước khi submit code, kiểm tra:

- [ ] Không có field `public` nào bị truy cập trực tiếp từ class khác
- [ ] Không có biến `static` mutable dùng chung
- [ ] Không có tham số `flag`/`type`/`kind` điều khiển luồng trong method
- [ ] Method không nhận object lớn khi chỉ cần 1–2 field
- [ ] Số tham số mỗi method ≤ 4

---

## PHẦN 3 — QUY TẮC COHESION (ĐỘ GẮN KẾT)

> **Định nghĩa:** Cohesion là mức độ các phần tử trong một module thuộc về nhau và phục vụ cùng một mục đích.  
> **Mục tiêu:** COHESION CÀNG CAO CÀNG TỐT.

### 3.0 Thang đo Cohesion (từ tệ → tốt)

```
Coincidental → Logical → Temporal → Procedural → Communicational → Sequential → Informational → Functional
   [tệ nhất]                                                                                    [tốt nhất]
```

---

### 3.1 COINCIDENTAL COHESION — ❌ TỆ NHẤT

**Định nghĩa:** Các phần tử trong module không có liên hệ gì với nhau, chỉ tình cờ ở cùng một chỗ.

**❌ Code sai:**
```java
class Joe {
    public String win2lin(String path) { ... }    // chuyển đổi đường dẫn
    public int daysSinceEpoch(String date) { ... } // tính số ngày
    public void outputReport(FinanceData d) { ... } // xuất báo cáo
}
// 3 việc hoàn toàn không liên quan nhau
```

**QUY TẮC H1:** Một class không được chứa các method không liên quan đến nhau. Nếu không thể mô tả class bằng một câu ngắn gọn, hãy tách nó ra.

---

### 3.2 LOGICAL COHESION — ❌ KÉM

**Định nghĩa:** Các phần tử "cùng loại" về mặt trực quan nhưng phục vụ các mục đích khác nhau, caller phải truyền flag để chọn.

**❌ Code sai:**
```java
public void readData(String source) {
    if (source.equals("tape")) { ... }
    else if (source.equals("disk")) { ... }
    else if (source.equals("network")) { ... }
}
```

**✅ Code đúng — dùng inheritance:**
```java
public abstract class DataReader<T> {
    public abstract void read(T source);
}

public class TapeReader extends DataReader<Tape> {
    @Override public void read(Tape t) { ... }
}

public class DiskReader extends DataReader<Disk> {
    @Override public void read(Disk d) { ... }
}

public class NetworkReader extends DataReader<Network> {
    @Override public void read(Network n) { ... }
}
```

**QUY TẮC H2:** Khi các hành vi "cùng loại" nhưng khác nhau về thực thi, hãy dùng abstract class hoặc interface + polymorphism thay vì `switch/if` bên trong method.

---

### 3.3 TEMPORAL COHESION — ⚠️ KÉM

**Định nghĩa:** Các phần tử được nhóm lại chỉ vì chúng xảy ra cùng thời điểm (ví dụ: cùng trong hàm `init()`).

**❌ Code sai:**
```java
class Init {
    public void initAll() {
        initReport();    // khởi tạo báo cáo
        initWeather();   // khởi tạo thời tiết
        initCounter();   // khởi tạo bộ đếm
        // ... hoàn toàn không liên quan nhau
    }
}
```

**✅ Code đúng — mỗi component tự khởi tạo:**
```java
// Mỗi class tự khởi tạo trong constructor
class ReportService {
    public ReportService() { /* tự init */ }
}

class WeatherService {
    public WeatherService() { /* tự init */ }
}
```

**QUY TẮC H3:** Không tạo "god initialization method". Mỗi component tự khởi tạo trong constructor của nó.

---

### 3.4 PROCEDURAL COHESION — ⚠️ CHẤP NHẬN ĐƯỢC

**Định nghĩa:** Các phần tử được nhóm vì chúng xảy ra theo một thứ tự nhất định, nhưng không chia sẻ data với nhau.

**❌ Code sai:**
```java
public class Example {
    public void readData() { ... }
    public void sendEmail() { ... } // không liên quan đến readData
}
```

**✅ Code đúng:**
```java
public class DataReader {
    public void readData() { ... }
}

public class EmailSender {
    public void sendEmail() { ... }
}
```

---

### 3.5 COMMUNICATIONAL COHESION — ✅ CHẤP NHẬN ĐƯỢC

**Định nghĩa:** Các phần tử cùng thao tác trên một tập dữ liệu chung.

**Ví dụ chấp nhận được:**
```java
public class TransactionManager {
    private Transaction trans;

    public void readTransaction() { ... }
    public void sortTransaction() { ... }
    public void calculateMean() { ... }
    public void printTransaction() { ... }
    public void saveTransaction() { ... }
    // Tất cả đều làm việc với Transaction
}
```

---

### 3.6 SEQUENTIAL COHESION — ✅ TỐT

**Định nghĩa:** Output của bước trước là input của bước tiếp theo — như một pipeline.

**Ví dụ tốt:**
```java
public class CarPainter {
    public Car cleanBody(Car car) { ... }
    public Car fillHoles(Car car) { ... }
    public Car sandBody(Car car) { ... }
    public Car applyPrimer(Car car) { ... }
    // Output của mỗi bước là Car được truyền vào bước tiếp theo
}
```

---

### 3.7 INFORMATIONAL COHESION — ✅ RẤT TỐT

**Định nghĩa:** Module thực hiện nhiều hành động, mỗi hành động có entry point riêng, tất cả đều thao tác trên cùng một data structure. Đây là kiểu cohesion đặc trưng của OOP.

**Ví dụ — Repository pattern:**
```java
public class EmployeeRepository {
    public void add(EmpRecord rec) { ... }
    public void update(EmpRecord rec) { ... }
    public void delete(int empId) { ... }
    public EmpRecord findById(int empId) { ... }
    // Tất cả thao tác trên EmpRecord
}
```

---

### 3.8 FUNCTIONAL COHESION — ✅ TỐT NHẤT

**Định nghĩa:** Toàn bộ module chỉ phục vụ MỘT chức năng duy nhất. Mọi phần tử đều cần thiết và đủ cho chức năng đó.

**Ví dụ tốt:**
```java
public class TaxCalculator {
    public double calculateIncomeTax(double salary) { ... }
    // Chỉ làm một việc: tính thuế thu nhập
}

public class PasswordHasher {
    public String hash(String plaintext) { ... }
    public boolean verify(String plaintext, String hash) { ... }
    // Chỉ làm một việc: quản lý hash mật khẩu
}
```

---

### 3.9 Checklist đánh giá Cohesion

Để kiểm tra cohesion, hãy viết một câu mô tả chức năng của module:

| Dấu hiệu trong câu mô tả | Loại Cohesion |
|---|---|
| Câu có nhiều vế không liên quan | Coincidental — tách ngay |
| Có từ "và", "hoặc" nối các chức năng khác nhau | Sequential hoặc Communicational — xem xét tách |
| Có từ "đầu tiên", "tiếp theo", "sau đó" | Sequential hoặc Temporal |
| Có từ "khởi tạo", "init" | Temporal — cần cải thiện |
| Câu ngắn gọn, một ý duy nhất | Functional — tốt! |

**Checklist:**

- [ ] Có thể mô tả class bằng một câu ngắn, rõ ràng
- [ ] Không có method nào "lạc chỗ" trong class
- [ ] Mọi method đều liên quan đến data mà class quản lý
- [ ] Class không làm quá 2–3 việc khác nhau
- [ ] Tên class phản ánh đúng trách nhiệm của nó

---

## PHẦN 4 — NGUYÊN TẮC TỔNG HỢP

### 4.1 Single Responsibility Principle (SRP)

> **"Một class chỉ được có một lý do để thay đổi."**

```java
// ❌ Sai: class làm 2 việc — xử lý data VÀ hiển thị
class Report {
    public void calculateRevenue() { ... }
    public void printReport() { ... }
}

// ✅ Đúng: tách thành 2 class có trách nhiệm riêng
class RevenueCalculator {
    public double calculateRevenue() { ... }
}

class ReportPrinter {
    public void print(double revenue) { ... }
}
```

### 4.2 Cân bằng Coupling — Cohesion

Khi thiết kế, tồn tại một sự đánh đổi:

- **Tăng cohesion** (chia nhỏ module) → có thể làm tăng coupling (nhiều module cần phụ thuộc nhau hơn)
- **Giảm coupling** (gộp module) → có thể làm giảm cohesion (module đảm nhận nhiều trách nhiệm hơn)

**Chiến lược:** Ưu tiên high cohesion trước. Sau đó dùng interface/abstraction để giảm coupling giữa các module đã được tách.

### 4.3 Sử dụng Interface để giảm coupling

```java
// Thay vì phụ thuộc vào implementation cụ thể
class OrderService {
    private MySQLDatabase db; // tight coupling
    public void saveOrder(Order o) { db.save(o); }
}

// Phụ thuộc vào abstraction
class OrderService {
    private Database db; // loose coupling
    public OrderService(Database db) { this.db = db; }
    public void saveOrder(Order o) { db.save(o); }
}

interface Database {
    void save(Object entity);
}

class MySQLDatabase implements Database { ... }
class MongoDatabase implements Database { ... }
```

---

## PHẦN 5 — BỘ QUY TẮC TÓM TẮT CHO AI

Khi AI viết code, phải tuân thủ theo thứ tự ưu tiên sau:

### ✅ LUÔN LÀM

1. Mọi field đều `private`, cung cấp getter/setter khi cần
2. Mỗi class/method có một trách nhiệm duy nhất, rõ ràng
3. Truyền tham số tối thiểu cần thiết (không truyền object thừa)
4. Dùng interface/abstract class khi có nhiều implementation
5. Logic tính toán trên data nằm trong class sở hữu data
6. Tách các chức năng không liên quan vào class riêng

### ❌ KHÔNG BAO GIỜ LÀM

1. Truy cập trực tiếp vào field `private` của class khác
2. Dùng biến global mutable (`public static` có thể bị thay đổi)
3. Truyền tham số `flag`/`type`/`kind` để điều khiển luồng bên trong method
4. Tạo "god class" làm quá nhiều việc không liên quan
5. Tạo "god init method" khởi tạo mọi thứ ở một chỗ
6. `switch/if` dựa trên type khi có thể dùng polymorphism

### ⚠️ HỎI LẠI KHI

1. Method có > 4 tham số
2. Class có > 10 method
3. Không thể mô tả class bằng một câu ngắn
4. Cần thêm method mới không liên quan đến các method hiện có

---

## PHẦN 6 — VÍ DỤ TỔNG HỢP: REFACTOR TỪ XẤU → TỐT

### Trước (Vi phạm nhiều quy tắc):

```java
// ❌ God class, low cohesion, high coupling
class Sensor {
    public int humidity;    // public field — vi phạm C1
    public int temperature; // public field — vi phạm C1

    public Object get(int controlFlag) { // magic flag — vi phạm C3
        switch (controlFlag) {
            case 0: return this.humidity;
            case 1: return this.temperature;
            default: throw new RuntimeException("Unknown flag");
        }
    }
}

// Caller phải biết 0 = humidity, 1 = temperature
Object value = sensor.get(0); // magic number!
```

### Sau (Tuân thủ đầy đủ):

```java
// ✅ High cohesion, low coupling
public class HumiditySensor {
    private int humidity;
    public int readHumidity() { return humidity; }
}

public class TemperatureSensor {
    private int temperature;
    public int readTemperature() { return temperature; }
}

// Hoặc dùng interface nếu cần xử lý đồng nhất:
public interface Sensor {
    int read();
    String getUnit();
}

public class HumiditySensor implements Sensor {
    private int humidity;
    @Override public int read() { return humidity; }
    @Override public String getUnit() { return "%"; }
}

public class TemperatureSensor implements Sensor {
    private int temperature;
    @Override public int read() { return temperature; }
    @Override public String getUnit() { return "°C"; }
}
```

---

*Tài liệu này được chuyển hóa từ bài giảng "12. Design Concepts" — Software Design and Construction.*  
*Áp dụng cho mọi ngôn ngữ OOP: Java, C#, Python, TypeScript, Kotlin...*
