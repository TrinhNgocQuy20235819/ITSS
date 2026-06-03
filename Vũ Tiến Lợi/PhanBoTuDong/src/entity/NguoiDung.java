package entity;

/**
 * Entity: Người dùng hệ thống (Bộ phận bán hàng tạo yêu cầu nhập).
 *
 * Theo Class Diagram:
 *   - MaNguoiDung : string
 *   - TenNguoiDung : string
 *   - Email : string
 *   - ChucVu : string
 *   + LayThongTinNguoiDung(MaNguoiDung) : string
 */
public class NguoiDung {

    private String maNguoiDung;
    private String tenNguoiDung;
    private String email;
    private String chucVu;

    public NguoiDung(String maNguoiDung, String tenNguoiDung, String email, String chucVu) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
        this.chucVu = chucVu;
    }

    // Getter / Setter
    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenNguoiDung() { return tenNguoiDung; }
    public void setTenNguoiDung(String tenNguoiDung) { this.tenNguoiDung = tenNguoiDung; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    /**
     * Trả về thông tin hiển thị người dùng.
     * Tương ứng: LayThongTinNguoiDung(MaNguoiDung) : string trong Class Diagram.
     */
    public String layThongTinNguoiDung(String maNguoiDung) {
        if (this.maNguoiDung.equals(maNguoiDung)) {
            return tenNguoiDung + " (" + chucVu + ")";
        }
        return "Không tìm thấy";
    }

    @Override
    public String toString() {
        return tenNguoiDung + " - " + chucVu;
    }
}
