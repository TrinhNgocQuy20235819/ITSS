package controller;

import dao.DonHangNhapDAO;
import dao.NguoiDungDAO;
import dao.SanPhamDAO;
import entity.DonHangNhap;
import entity.NguoiDung;
import entity.SanPham;
import entity.MatHang;
import entity.YeuCauNhapHang;
import dao.YeuCauNhapHangDAO;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Control: LapYeuCauNhapHang - Điều phối logic nghiệp vụ khi tạo yêu cầu nhập hàng.
 *
 * Theo Class Diagram (<<control>> LapYeuCauNhapHang):
 *   + MoFormTaoYeuCauNhapHang() : void
 *   + LayThongTinCoBan() : void
 *   + SubmitFormTaoYeuCauNhapHang() : void
 *   + ValidateThongTinDauVao() : boolean
 *   + AddChiTietYeuCau() : void
 */
public class LapYeuCauNhapHang {

    private final SanPhamDAO sanPhamDAO;
    private final NguoiDungDAO nguoiDungDAO;
    private final DonHangNhapDAO donHangNhapDAO;

    public LapYeuCauNhapHang() {
        this.sanPhamDAO = new SanPhamDAO();
        this.nguoiDungDAO = new NguoiDungDAO();
        this.donHangNhapDAO = new DonHangNhapDAO();
    }

    // Constructor để inject DAO (phục vụ Unit Test)
    public LapYeuCauNhapHang(SanPhamDAO sanPhamDAO,
                              NguoiDungDAO nguoiDungDAO,
                              DonHangNhapDAO donHangNhapDAO) {
        this.sanPhamDAO = sanPhamDAO;
        this.nguoiDungDAO = nguoiDungDAO;
        this.donHangNhapDAO = donHangNhapDAO;
    }

    /**
     * Lấy danh sách sản phẩm và thông tin người dùng để hiển thị lên form.
     * Tương ứng: LayThongTinCoBan() trong Sequence Diagram.
     */
    public List<SanPham> layDanhSachSanPham() {
        return sanPhamDAO.getAllSanPham();
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa.
     */
    public List<SanPham> timKiemSanPham(String tuKhoa) {
        return sanPhamDAO.timKiem(tuKhoa);
    }

    /**
     * Lấy thông tin người dùng đang đăng nhập.
     * Tương ứng: LayThongTinNguoiDung(MaNguoiDung) trong Sequence Diagram.
     */
    public NguoiDung layNguoiDungHienTai() {
        return nguoiDungDAO.getNguoiDungHienTai();
    }

    /**
     * Kiểm tra tính hợp lệ của thông tin đầu vào trước khi tạo yêu cầu.
     * Điều kiện: số lượng từng sản phẩm phải > 0 (theo Activity Diagram).
     * Tương ứng: ValidateThongTinDauVao() : boolean
     *
     * @param chiTiet Map sản phẩm -> số lượng nhập
     * @return null nếu hợp lệ, chuỗi lỗi nếu không hợp lệ
     */
    public String validateThongTinDauVao(Map<SanPham, Integer> chiTiet) {
        if (chiTiet == null || chiTiet.isEmpty()) {
            return "Chưa chọn sản phẩm nào. Vui lòng chọn ít nhất một sản phẩm.";
        }
        for (Map.Entry<SanPham, Integer> entry : chiTiet.entrySet()) {
            if (entry.getValue() == null || entry.getValue() <= 0) {
                return "Số lượng luôn lớn hơn 0. Vui lòng kiểm tra lại số lượng cho: "
                        + entry.getKey().getTenSanPham();
            }
        }
        return null; // hợp lệ
    }

    /**
     * Tạo và lưu một yêu cầu nhập hàng mới vào DAO.
     * Tương ứng: SubmitFormTaoYeuCauNhapHang() và AddYeuCauNhapHang() trong Sequence Diagram.
     *
     * @param chiTiet  Map sản phẩm -> số lượng yêu cầu
     * @param ghiChu   Ghi chú từ người dùng
     * @return DonHangNhap vừa được tạo, hoặc null nếu validate thất bại
     */
    public DonHangNhap submitTaoYeuCau(Map<SanPham, Integer> chiTiet, String ghiChu) {
        // 1. Validate
        String loi = validateThongTinDauVao(chiTiet);
        if (loi != null) {
            return null;
        }

        // 2. Lấy thông tin người dùng
        NguoiDung nguoiDung = layNguoiDungHienTai();
        String taoBoi = (nguoiDung != null) ? nguoiDung.getTenNguoiDung() : "Không xác định";

        // 3. Tạo đối tượng DonHangNhap
        String id = donHangNhapDAO.generateId();
        DonHangNhap donHang = new DonHangNhap(id, taoBoi);
        donHang.setGhiChu(ghiChu);

        // 4. Thêm chi tiết sản phẩm
        for (Map.Entry<SanPham, Integer> entry : chiTiet.entrySet()) {
            donHang.addChiTiet(entry.getKey(), entry.getValue());
        }

        // 5. Tính tổng tiền
        donHang.tinhLaiTongTien();

        // 6. Lưu vào DAO
        donHangNhapDAO.add(donHang);

        // 7. Đồng bộ tự động sang YeuCauNhapHang (Phân bổ)
        dongBoSangYeuCauNhapHang(donHang);

        return donHang;
    }

    /**
     * Đồng bộ đơn hàng nhập sang yêu cầu nhập hàng phục vụ phân bổ tự động.
     */
    private void dongBoSangYeuCauNhapHang(DonHangNhap donHang) {
        List<MatHang> dsMH = new ArrayList<>();
        // Mặc định ngày nhận mong muốn là ngày hiện tại + 30 ngày
        Calendar cal = Calendar.getInstance();
        cal.setTime(donHang.getNgayTao() != null ? donHang.getNgayTao() : new Date());
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date ngayMongMuon = cal.getTime();

        for (Map.Entry<SanPham, Integer> entry : donHang.getChiTiet().entrySet()) {
            SanPham sp = entry.getKey();
            int sl = entry.getValue();
            dsMH.add(new MatHang(sp.getMaSanPham(), sp.getTenSanPham(), sl, "Cái", ngayMongMuon));
        }

        YeuCauNhapHang yc = new YeuCauNhapHang(
                donHang.getId(),
                donHang.getNgayTao(),
                "CHO_PHAN_BO",
                dsMH
        );

        // Lưu vào YeuCauNhapHangDAO
        new YeuCauNhapHangDAO().add(yc);
    }

    /**
     * Lấy danh sách tất cả người dùng.
     */
    public List<NguoiDung> layDanhSachNguoiDung() {
        return nguoiDungDAO.getAll();
    }

    /**
     * Đổi người dùng hiện tại đang hoạt động.
     */
    public void doiNguoiDungHienTai(String maNguoiDung) {
        NguoiDungDAO.setMaNguoiDungHienTai(maNguoiDung);
    }
}
