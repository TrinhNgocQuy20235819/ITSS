package entity;

/**
 * Entity: Sản phẩm có trong danh mục (dùng khi Bộ phận bán hàng tạo yêu cầu nhập).
 *
 * Theo Class Diagram:
 *   - MaSanPham : string
 *   - TenSanPham : string
 *   - GiaNhap : double
 *   + LayTenMatHang() : void
 */
public class SanPham {

    private String maSanPham;
    private String tenSanPham;
    private double giaNhap;

    public SanPham(String maSanPham, String tenSanPham, double giaNhap) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaNhap = giaNhap;
    }

    // Getter / Setter
    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public double getGiaNhap() { return giaNhap; }
    public void setGiaNhap(double giaNhap) { this.giaNhap = giaNhap; }

    /**
     * Trả về tên hiển thị cho mặt hàng.
     * Tương ứng: LayTenMatHang() : void trong Class Diagram.
     */
    public String layTenMatHang() {
        return maSanPham + " - " + tenSanPham;
    }

    @Override
    public String toString() {
        return layTenMatHang();
    }
}
