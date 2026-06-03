package entity;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entity: Đơn hàng nhập - đại diện cho một Yêu cầu nhập hàng mới được tạo.
 *
 * Theo Class Diagram:
 *   - ID() : string
 *   - NgayTao() : date
 *   - TongTien() : double
 *   - TrangThai() : string
 *   + AddYeuCauNhapHang(NewYeuCau : DonHangNhap) : void
 */
public class DonHangNhap {

    private String id;
    private Date ngayTao;
    private double tongTien;
    private String trangThai;

    /** Tên người tạo yêu cầu */
    private String taoBoi;

    /** Map: mã sản phẩm -> số lượng yêu cầu */
    private Map<SanPham, Integer> chiTiet;

    /** Ghi chú khi xác nhận */
    private String ghiChu;

    public DonHangNhap() {
        this.chiTiet = new LinkedHashMap<>();
        this.trangThai = "Vừa tạo";
        this.ngayTao = new Date();
    }

    public DonHangNhap(String id, String taoBoi) {
        this();
        this.id = id;
        this.taoBoi = taoBoi;
    }

    // ---- Getter / Setter ----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTaoBoi() { return taoBoi; }
    public void setTaoBoi(String taoBoi) { this.taoBoi = taoBoi; }

    public Map<SanPham, Integer> getChiTiet() { return chiTiet; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    // ---- Business Methods ----

    /**
     * Thêm hoặc cập nhật số lượng sản phẩm vào chi tiết đơn hàng.
     * Tương ứng: AddYeuCauNhapHang(NewYeuCau : DonHangNhap) : void
     */
    public void addChiTiet(SanPham sanPham, int soLuong) {
        chiTiet.put(sanPham, soLuong);
    }

    /** Tổng số lượng tất cả sản phẩm trong đơn. */
    public int getTongSoLuong() {
        return chiTiet.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** Tóm tắt tên các mặt hàng trong đơn (dùng hiển thị ở bảng danh sách). */
    public String getTomTatMatHang() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (SanPham sp : chiTiet.keySet()) {
            if (count > 0) sb.append(", ");
            sb.append(sp.getTenSanPham());
            count++;
            if (count >= 2 && chiTiet.size() > 2) {
                sb.append(" (+").append(chiTiet.size() - 2).append(" khác)");
                break;
            }
        }
        return sb.toString();
    }

    /** Tính lại tổng tiền từ chi tiết sản phẩm. */
    public void tinhLaiTongTien() {
        double tong = 0;
        for (Map.Entry<SanPham, Integer> entry : chiTiet.entrySet()) {
            tong += entry.getKey().getGiaNhap() * entry.getValue();
        }
        this.tongTien = tong;
    }
}
