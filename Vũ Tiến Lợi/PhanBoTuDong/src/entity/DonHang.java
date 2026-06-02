package entity;

import java.util.List;

/**
 * Lớp thực thể đại diện cho Đơn hàng (dự thảo) gửi tới Site.
 * Tương ứng với class ĐơnHàng trong Class Diagram.
 * Thuộc tính: mãĐơnHàng, mãSite, mãHàng, sốLượng, đơnVị, phươngTiệnVC, trạngThái
 * Phương thức: tạoDựThảoĐơnHàng(dsKếtQuả)
 */
public class DonHang {
    private String maDonHang;
    private String maSite;
    private String maHang;
    private int soLuong;
    private String donVi;
    private String phuongTienVC;
    private String trangThai; // "DU_THAO", "DA_GUI", "DA_NHAN"

    public DonHang() {}

    public DonHang(String maDonHang, String maSite, String maHang, int soLuong,
                   String donVi, String phuongTienVC, String trangThai) {
        this.maDonHang = maDonHang;
        this.maSite = maSite;
        this.maHang = maHang;
        this.soLuong = soLuong;
        this.donVi = donVi;
        this.phuongTienVC = phuongTienVC;
        this.trangThai = trangThai;
    }

    /**
     * Tạo danh sách dự thảo đơn hàng từ kết quả phân bổ.
     * Tương ứng: tạoDựThảoĐơnHàng(dsKếtQuả) : List trong Class Diagram.
     */
    public static List<DonHang> taoDuThaoDonHang(List<KetQuaPhanBo> dsKetQua) {
        java.util.List<DonHang> dsDonHang = new java.util.ArrayList<>();
        int count = 1;
        for (KetQuaPhanBo kq : dsKetQua) {
            if (!kq.isThieuHang()) {
                DonHang dh = new DonHang();
                dh.maDonHang = "DH-" + String.format("%04d", count++);
                dh.maSite = kq.getMaSite();
                dh.maHang = kq.getMaHang();
                dh.soLuong = kq.getSoLuongPhanBo();
                dh.donVi = "Cái"; // Mặc định, có thể mở rộng
                dh.phuongTienVC = kq.getPhuongTienVC();
                dh.trangThai = "DU_THAO";
                dsDonHang.add(dh);
            }
        }
        return dsDonHang;
    }

    // Getters & Setters
    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public String getMaSite() { return maSite; }
    public void setMaSite(String maSite) { this.maSite = maSite; }

    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    public String getPhuongTienVC() { return phuongTienVC; }
    public void setPhuongTienVC(String phuongTienVC) { this.phuongTienVC = phuongTienVC; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    @Override
    public String toString() {
        return maDonHang + " | Site: " + maSite + " | Hàng: " + maHang + " | SL: " + soLuong + " | VC: " + phuongTienVC;
    }
}
