package entity;

/**
 * Lớp thực thể đại diện cho Thông tin tồn kho tại một Site.
 * Tương ứng với class ThôngTinKho trong Class Diagram.
 * Thuộc tính: mãSite, mãHàng, sốLượngTồnKho, đơnVị
 */
public class ThongTinKho {
    private String maSite;
    private String maHang;
    private int soLuongTonKho;
    private String donVi;

    public ThongTinKho() {}

    public ThongTinKho(String maSite, String maHang, int soLuongTonKho, String donVi) {
        this.maSite = maSite;
        this.maHang = maHang;
        this.soLuongTonKho = soLuongTonKho;
        this.donVi = donVi;
    }

    // Getters & Setters
    public String getMaSite() { return maSite; }
    public void setMaSite(String maSite) { this.maSite = maSite; }

    public String getMaHang() { return maHang; }
    public void setMaHang(String maHang) { this.maHang = maHang; }

    public int getSoLuongTonKho() { return soLuongTonKho; }
    public void setSoLuongTonKho(int soLuongTonKho) { this.soLuongTonKho = soLuongTonKho; }

    public String getDonVi() { return donVi; }
    public void setDonVi(String donVi) { this.donVi = donVi; }

    @Override
    public String toString() {
        return "Site: " + maSite + " | Hàng: " + maHang + " | Tồn kho: " + soLuongTonKho + " " + donVi;
    }
}
