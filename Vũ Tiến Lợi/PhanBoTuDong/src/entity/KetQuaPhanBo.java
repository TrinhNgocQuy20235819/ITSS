package entity;

import java.util.Date;

/**
 * Lớp thực thể đại diện cho Kết quả phân bổ cho một mặt hàng tại một Site.
 * Tương ứng với class KếtQuảPhânBổ trong Class Diagram.
 * Thuộc tính: mãHàng, mãSite, sốLượngPhânBổ, phươngTiệnVC, ngàyGiaoDựKiến
 * Phương thức: tạoKếtQuả(), lưuKếtQuả(), tạoKếtQuảThiếuHàng()
 */
public class KetQuaPhanBo {
    private String maHang;
    private String tenHang;
    private String maSite;
    private String tenSite;
    private int soLuongKho;
    private int soLuongPhanBo;
    private String phuongTienVC; // "Tau" hoặc "HangKhong"
    private Date ngayGiaoDuKien;
    private boolean thieuHang; // true nếu là dòng cảnh báo thiếu hàng

    public KetQuaPhanBo() {
        this.thieuHang = false;
    }

    /**
     * Tạo kết quả phân bổ cho 1 Site.
     * Tương ứng: tạoKếtQuả(site, slPhânBổ, phươngTiện) trong Class Diagram.
     */
    public static KetQuaPhanBo taoKetQua(String maHang, String tenHang,
                                          String maSite, String tenSite,
                                          int soLuongKho, int slPhanBo,
                                          String phuongTien, Date ngayGiaoDuKien) {
        KetQuaPhanBo kq = new KetQuaPhanBo();
        kq.maHang = maHang;
        kq.tenHang = tenHang;
        kq.maSite = maSite;
        kq.tenSite = tenSite;
        kq.soLuongKho = soLuongKho;
        kq.soLuongPhanBo = slPhanBo;
        kq.phuongTienVC = phuongTien;
        kq.ngayGiaoDuKien = ngayGiaoDuKien;
        kq.thieuHang = false;
        return kq;
    }

    /**
     * Tạo kết quả cảnh báo thiếu hàng.
     * Tương ứng: tạoKếtQuảThiếuHàng(mãHàng, slThiếu) trong Class Diagram.
     */
    public static KetQuaPhanBo taoKetQuaThieuHang(String maHang, String tenHang, int slThieu) {
        KetQuaPhanBo kq = new KetQuaPhanBo();
        kq.maHang = maHang;
        kq.tenHang = tenHang;
        kq.maSite = "N/A";
        kq.tenSite = "CẢNH BÁO THIẾU HÀNG";
        kq.soLuongKho = 0;
        kq.soLuongPhanBo = slThieu;
        kq.phuongTienVC = "N/A";
        kq.ngayGiaoDuKien = null;
        kq.thieuHang = true;
        return kq;
    }

    // Getters & Setters
    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public String getMaSite() { return maSite; }
    public void setMaSite(String maSite) { this.maSite = maSite; }

    public String getTenSite() { return tenSite; }
    public void setTenSite(String tenSite) { this.tenSite = tenSite; }

    public int getSoLuongKho() { return soLuongKho; }
    public void setSoLuongKho(int soLuongKho) { this.soLuongKho = soLuongKho; }

    public int getSoLuongPhanBo() { return soLuongPhanBo; }
    public void setSoLuongPhanBo(int soLuongPhanBo) { this.soLuongPhanBo = soLuongPhanBo; }

    public String getPhuongTienVC() { return phuongTienVC; }
    public void setPhuongTienVC(String phuongTienVC) { this.phuongTienVC = phuongTienVC; }

    public Date getNgayGiaoDuKien() { return ngayGiaoDuKien; }
    public void setNgayGiaoDuKien(Date ngayGiaoDuKien) { this.ngayGiaoDuKien = ngayGiaoDuKien; }

    public boolean isThieuHang() { return thieuHang; }
    public void setThieuHang(boolean thieuHang) { this.thieuHang = thieuHang; }
}
