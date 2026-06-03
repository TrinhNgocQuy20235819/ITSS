package controller;

import dao.DonHangNhapDAO;
import entity.DonHangNhap;

import java.util.List;

/**
 * Control: DanhSachYeuCauNhap - Điều phối việc mở form và tải danh sách yêu cầu nhập hàng.
 *
 * Theo Class Diagram (<<control>> DanhSachYeuCauNhap):
 *   + MoFormLapYeuCauNhapHang() : void
 *   + LoadDanhSachYeuCau() : void
 *
 * Theo Sequence Diagram:
 *   - Nhận lệnh từ GiaoDienXemYeuCauNhapHang (boundary)
 *   - Gọi MoFormTaoYeuCauNhapHang trên LapYeuCauNhapHang
 */
public class DanhSachYeuCauNhap {

    private final DonHangNhapDAO donHangNhapDAO;

    public DanhSachYeuCauNhap() {
        this.donHangNhapDAO = new DonHangNhapDAO();
    }

    public DanhSachYeuCauNhap(DonHangNhapDAO donHangNhapDAO) {
        this.donHangNhapDAO = donHangNhapDAO;
    }

    /**
     * Lấy toàn bộ danh sách yêu cầu nhập hàng để hiển thị.
     * Tương ứng: LoadDanhSachYeuCau() trong Class Diagram.
     */
    public List<DonHangNhap> loadDanhSachYeuCau() {
        return donHangNhapDAO.getAll();
    }

    /**
     * Lấy danh sách theo trạng thái (dùng cho tính năng lọc).
     */
    public List<DonHangNhap> filterByTrangThai(String trangThai) {
        if (trangThai == null || trangThai.equals("Tất cả")) {
            return donHangNhapDAO.getAll();
        }
        return donHangNhapDAO.getByTrangThai(trangThai);
    }
}
