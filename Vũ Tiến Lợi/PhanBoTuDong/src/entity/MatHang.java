package entity;

import java.util.Date;

/**
 * Lớp thực thể đại diện cho một mặt hàng trong yêu cầu nhập.
 * Mỗi mặt hàng thuộc về một YêuCầuNhậpHàng.
 */
public class MatHang {
    private String maHang;
    private String tenHang;
    private int soLuongYeuCau;
    private String donVi;
    private Date ngayNhanMongMuon;

    public MatHang() {}

    public MatHang(String maHang, String tenHang, int soLuongYeuCau, String donVi, Date ngayNhanMongMuon) {
        this.maHang = maHang;
        this.tenHang = tenHang;
        this.soLuongYeuCau = soLuongYeuCau;
        this.donVi = donVi;
        this.ngayNhanMongMuon = ngayNhanMongMuon;
    }

    // Getters & Setters
    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public String getTenHang() { return tenHang; }
    public void setTenHang(String tenHang) { this.tenHang = tenHang; }

    public int getSoLuongYeuCau() { return soLuongYeuCau; }
    public void setSoLuongYeuCau(int soLuongYeuCau) { this.soLuongYeuCau = soLuongYeuCau; }

    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    public Date getNgayNhanMongMuon() { return ngayNhanMongMuon; }
    public void setNgayNhanMongMuon(Date ngayNhanMongMuon) { this.ngayNhanMongMuon = ngayNhanMongMuon; }

    @Override
    public String toString() {
        return maHang + " - " + tenHang + " (SL: " + soLuongYeuCau + " " + donVi + ")";
    }
}
