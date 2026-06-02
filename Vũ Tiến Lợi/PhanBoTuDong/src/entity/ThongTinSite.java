package entity;

/**
 * Lớp thực thể đại diện cho Thông tin Site nhập khẩu.
 * Tương ứng với class ThôngTinSite trong Class Diagram.
 * Thuộc tính: mãSite, tênSite, sốNgàyGiaoTàu, sốNgàyGiaoHàngKhông, thôngTinKhác
 */
public class ThongTinSite {
    private String maSite;
    private String tenSite;
    private int soNgayGiaoTau;
    private int soNgayGiaoHangKhong;
    private String thongTinKhac;

    public ThongTinSite() {}

    public ThongTinSite(String maSite, String tenSite, int soNgayGiaoTau, int soNgayGiaoHangKhong, String thongTinKhac) {
        this.maSite = maSite;
        this.tenSite = tenSite;
        this.soNgayGiaoTau = soNgayGiaoTau;
        this.soNgayGiaoHangKhong = soNgayGiaoHangKhong;
        this.thongTinKhac = thongTinKhac;
    }

    // Getters & Setters
    public String getMaSite() { return maSite; }
    public void setMaSite(String maSite) { this.maSite = maSite; }

    public String getTenSite() { return tenSite; }
    public void setTenSite(String tenSite) { this.tenSite = tenSite; }

    public int getSoNgayGiaoTau() { return soNgayGiaoTau; }
    public void setSoNgayGiaoTau(int soNgayGiaoTau) { this.soNgayGiaoTau = soNgayGiaoTau; }

    public int getSoNgayGiaoHangKhong() { return soNgayGiaoHangKhong; }
    public void setSoNgayGiaoHangKhong(int soNgayGiaoHangKhong) { this.soNgayGiaoHangKhong = soNgayGiaoHangKhong; }

    public String getThongTinKhac() { return thongTinKhac; }
    public void setThongTinKhac(String thongTinKhac) { this.thongTinKhac = thongTinKhac; }

    @Override
    public String toString() {
        return maSite + " - " + tenSite + " (Tàu: " + soNgayGiaoTau + " ngày, HK: " + soNgayGiaoHangKhong + " ngày)";
    }
}
