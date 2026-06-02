package entity;

import java.util.Date;
import java.util.List;

/**
 * Lớp thực thể đại diện cho Yêu cầu nhập hàng.
 * Tương ứng với class YêuCầuNhậpHàng trong Class Diagram.
 * Thuộc tính: yêuCầuID, ngàyTạo, trạngThái
 * Phương thức: lấyDSYêuCầuĐãNhậnTồnKho(), lấyDSMặtHàng(yêuCầuID)
 */
public class YeuCauNhapHang {
    private String yeuCauID;
    private Date ngayTao;
    private String trangThai; // "CHO_PHAN_BO", "DA_PHAN_BO", "DA_GUI_DON"
    private List<MatHang> danhSachMatHang;

    public YeuCauNhapHang() {}

    public YeuCauNhapHang(String yeuCauID, Date ngayTao, String trangThai, List<MatHang> danhSachMatHang) {
        this.yeuCauID = yeuCauID;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
        this.danhSachMatHang = danhSachMatHang;
    }

    /**
     * Lấy danh sách mặt hàng của yêu cầu này.
     * Tương ứng: lấyDSMặtHàng(yêuCầuID) : List trong Class Diagram.
     */
    public List<MatHang> layDSMatHang() {
        return this.danhSachMatHang;
    }

    // Getters & Setters
    public String getYeuCauID() { return yeuCauID; }
    public void setYeuCauID(String yeuCauID) { this.yeuCauID = yeuCauID; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public List<MatHang> getDanhSachMatHang() { return danhSachMatHang; }
    public void setDanhSachMatHang(List<MatHang> danhSachMatHang) { this.danhSachMatHang = danhSachMatHang; }

    public int getSoMatHang() {
        return danhSachMatHang != null ? danhSachMatHang.size() : 0;
    }

    @Override
    public String toString() {
        return yeuCauID + " | Ngày tạo: " + ngayTao + " | Trạng thái: " + trangThai + " | Số MH: " + getSoMatHang();
    }
}
