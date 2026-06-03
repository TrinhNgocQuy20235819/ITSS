package test;

import controller.LapYeuCauNhapHang;
import dao.DonHangNhapDAO;
import dao.NguoiDungDAO;
import dao.SanPhamDAO;
import entity.DonHangNhap;
import entity.NguoiDung;
import entity.SanPham;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Kiểm thử tự động cho Use Case "Tạo yêu cầu nhập hàng".
 *
 * Module kiểm thử chính: LapYeuCauNhapHang (Controller)
 *
 * Bao gồm:
 *   ── Kiểm thử hộp đen (Black-box): Dựa trên Activity Diagram và SRS
 *   ── Kiểm thử hộp trắng (White-box, C1): Bao phủ các nhánh quyết định
 *      trong validateThongTinDauVao() và submitTaoYeuCau()
 */
public class LapYeuCauNhapHangTest {

    private LapYeuCauNhapHang controller;
    private SanPhamDAO sanPhamDAO;
    private NguoiDungDAO nguoiDungDAO;
    private DonHangNhapDAO donHangNhapDAO;

    // Dữ liệu mẫu dùng chung
    private SanPham sp1;
    private SanPham sp2;
    private SanPham sp3;

    @Before
    public void setUp() {
        sanPhamDAO    = new SanPhamDAO();
        nguoiDungDAO  = new NguoiDungDAO();
        donHangNhapDAO = new DonHangNhapDAO();
        controller    = new LapYeuCauNhapHang(sanPhamDAO, nguoiDungDAO, donHangNhapDAO);

        // Lấy sản phẩm thực từ DAO
        List<SanPham> dsSP = sanPhamDAO.getAllSanPham();
        sp1 = dsSP.get(0); // MH001
        sp2 = dsSP.get(1); // MH002
        sp3 = dsSP.get(2); // MH003
    }

    // ====================================================================
    // KIỂM THỬ HỘP ĐEN (Black-box Testing)
    // Dựa trên Activity Diagram và đặc tả Use Case
    // ====================================================================

    /**
     * TC-NH-BB01 - Luồng chính: Tạo yêu cầu thành công với số lượng hợp lệ.
     * Input: 2 sản phẩm với số lượng > 0
     * Expected: DonHangNhap được tạo, không null, ID có dạng YCN-2025-XXX
     */
    @Test
    public void testBB01_TaoYeuCauThanhCong() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 100);
        chiTiet.put(sp2, 200);

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "Đơn hàng thử nghiệm");

        assertNotNull("Kết quả không được null khi tạo thành công", result);
        assertNotNull("ID phải được tạo tự động", result.getId());
        assertTrue("ID phải bắt đầu bằng YCN-", result.getId().startsWith("YCN-"));
        assertEquals("Trạng thái mặc định phải là 'Vừa tạo'", "Vừa tạo", result.getTrangThai());
    }

    /**
     * TC-NH-BB02 - Luồng thay thế: Số lượng <= 0 → thông báo lỗi.
     * Theo Activity Diagram: Số lượng <= 0 → Thông báo lỗi "Số lượng luôn lớn hơn 0"
     * Expected: validateThongTinDauVao trả về chuỗi lỗi (not null)
     */
    @Test
    public void testBB02_SoLuongKhongHopLe_ZeroReturnsError() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 0); // số lượng = 0

        String loi = controller.validateThongTinDauVao(chiTiet);

        assertNotNull("Phải trả về thông báo lỗi khi số lượng = 0", loi);
        assertTrue("Thông báo lỗi phải đề cập 'lớn hơn 0'", loi.contains("lớn hơn 0"));
    }

    /**
     * TC-NH-BB03 - Luồng thay thế: Số lượng âm → thông báo lỗi.
     * Expected: validateThongTinDauVao trả về chuỗi lỗi khi số lượng < 0
     */
    @Test
    public void testBB03_SoLuongKhongHopLe_NegativeReturnsError() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 100);
        chiTiet.put(sp2, -5); // số lượng âm

        String loi = controller.validateThongTinDauVao(chiTiet);

        assertNotNull("Phải trả về thông báo lỗi khi số lượng âm", loi);
    }

    /**
     * TC-NH-BB04 - Danh sách sản phẩm chọn rỗng → thông báo lỗi.
     * Expected: validateThongTinDauVao trả về lỗi khi map rỗng
     */
    @Test
    public void testBB04_ChiTietRong() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();

        String loi = controller.validateThongTinDauVao(chiTiet);

        assertNotNull("Phải báo lỗi khi chưa chọn sản phẩm", loi);
        assertTrue("Thông báo phải đề cập 'Chưa chọn sản phẩm'",
                loi.toLowerCase().contains("chưa chọn") || loi.toLowerCase().contains("chua chon"));
    }

    /**
     * TC-NH-BB05 - Tổng tiền phải được tính đúng.
     * Input: sp1 giá 12500 x 10 + sp2 giá 2200 x 20 = 125000 + 44000 = 169000
     * Expected: tongTien = 169000.0
     */
    @Test
    public void testBB05_TongTienTinhDung() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 10); // MH001: 12500 x 10 = 125000
        chiTiet.put(sp2, 20); // MH002: 2200 x 20 = 44000

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "");

        assertNotNull(result);
        double expectedTongTien = sp1.getGiaNhap() * 10 + sp2.getGiaNhap() * 20;
        assertEquals("Tổng tiền phải được tính chính xác",
                expectedTongTien, result.getTongTien(), 0.01);
    }

    /**
     * TC-NH-BB06 - Tổng số lượng phải chính xác.
     * Input: sp1 x 30, sp2 x 70 → TongSoLuong = 100
     */
    @Test
    public void testBB06_TongSoLuongDung() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 30);
        chiTiet.put(sp2, 70);

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "");

        assertNotNull(result);
        assertEquals("Tổng số lượng phải = 100", 100, result.getTongSoLuong());
    }

    /**
     * TC-NH-BB07 - Thông tin người tạo được gán đúng từ người dùng hiện tại.
     * Expected: taoBoi = tên người dùng mock (Nguyễn Hải Đăng)
     */
    @Test
    public void testBB07_TaoBoisDuocGanDung() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 5);

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "");

        assertNotNull(result);
        assertNotNull("Tên người tạo không được null", result.getTaoBoi());
        assertFalse("Tên người tạo không được rỗng", result.getTaoBoi().isBlank());
    }

    /**
     * TC-NH-BB08 - Ghi chú được lưu đúng vào đơn hàng.
     * Expected: ghiChu == "Test ghi chú ABC"
     */
    @Test
    public void testBB08_GhiChuDuocLuu() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 5);
        String ghiChu = "Test ghi chú ABC";

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, ghiChu);

        assertNotNull(result);
        assertEquals("Ghi chú phải được lưu đúng", ghiChu, result.getGhiChu());
    }

    /**
     * TC-NH-BB09 - Sau khi tạo, số lượng đơn trong DAO tăng lên 1.
     */
    @Test
    public void testBB09_DonHangDuocLuuVaoDAO() {
        int truoc = donHangNhapDAO.count();
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp3, 50);

        controller.submitTaoYeuCau(chiTiet, "");

        assertEquals("Số đơn trong DAO phải tăng 1", truoc + 1, donHangNhapDAO.count());
    }

    /**
     * TC-NH-BB10 - Validate null map → trả về lỗi.
     */
    @Test
    public void testBB10_ValidateNullMap() {
        String loi = controller.validateThongTinDauVao(null);
        assertNotNull("Phải trả về lỗi khi map null", loi);
    }

    // ====================================================================
    // KIỂM THỬ HỘP TRẮNG (White-box Testing, C1 - Branch Coverage)
    // Bao phủ các nhánh quyết định trong validateThongTinDauVao() và submitTaoYeuCau()
    // ====================================================================

    /**
     * TC-NH-WB01 - Nhánh: chiTiet == null → return lỗi (nhánh TRUE của if null/empty).
     * Bao phủ: if (chiTiet == null || chiTiet.isEmpty()) → TRUE
     */
    @Test
    public void testWB01_ValidateNull_BranchTrue() {
        String loi = controller.validateThongTinDauVao(null);
        assertNotNull("Phải trả về lỗi khi null (nhánh TRUE)", loi);
    }

    /**
     * TC-NH-WB02 - Nhánh: chiTiet rỗng → return lỗi (nhánh TRUE của if null/empty).
     */
    @Test
    public void testWB02_ValidateEmpty_BranchTrue() {
        String loi = controller.validateThongTinDauVao(new HashMap<>());
        assertNotNull("Phải trả về lỗi khi rỗng (nhánh TRUE)", loi);
    }

    /**
     * TC-NH-WB03 - Nhánh: chiTiet không rỗng, mọi SL > 0 → return null (hợp lệ).
     * Bao phủ: if (chiTiet == null || chiTiet.isEmpty()) → FALSE
     *          + if (entry.getValue() <= 0) → FALSE (không vào nhánh lỗi SL)
     */
    @Test
    public void testWB03_ValidateHopLe_BothBranchFalse() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 10);
        chiTiet.put(sp2, 5);

        String loi = controller.validateThongTinDauVao(chiTiet);
        assertNull("Phải trả về null (hợp lệ) khi tất cả SL > 0", loi);
    }

    /**
     * TC-NH-WB04 - Nhánh: Có ít nhất 1 sản phẩm SL = 0 → return lỗi.
     * Bao phủ: if (entry.getValue() <= 0) → TRUE
     */
    @Test
    public void testWB04_ValidateSoLuongZero_BranchTrue() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 10);
        chiTiet.put(sp2, 0); // vi phạm

        String loi = controller.validateThongTinDauVao(chiTiet);
        assertNotNull("Phải trả về lỗi khi có SL = 0 (nhánh TRUE)", loi);
        assertTrue("Thông báo phải đề cập tên sản phẩm vi phạm",
                loi.contains(sp2.getTenSanPham()));
    }

    /**
     * TC-NH-WB05 - Nhánh: submitTaoYeuCau khi validate thất bại → return null.
     * Bao phủ: if (loi != null) → TRUE → return null
     */
    @Test
    public void testWB05_SubmitKhiValidateThieu_ReturnsNull() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, -1); // không hợp lệ

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "");
        assertNull("Submit phải trả về null khi validate thất bại (nhánh TRUE)", result);
    }

    /**
     * TC-NH-WB06 - Nhánh: submitTaoYeuCau khi validate thành công → return DonHangNhap.
     * Bao phủ: if (loi != null) → FALSE → tiếp tục tạo đơn hàng
     */
    @Test
    public void testWB06_SubmitKhiValidatePass_ReturnsDonHang() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 1); // hợp lệ

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "Ghi chú WB06");
        assertNotNull("Submit phải trả về DonHangNhap khi hợp lệ (nhánh FALSE)", result);
    }

    /**
     * TC-NH-WB07 - Kiểm tra ID tự tăng dần: Tạo 2 yêu cầu liên tiếp, ID sau > ID trước.
     */
    @Test
    public void testWB07_IDTuTang() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 5);

        DonHangNhap d1 = controller.submitTaoYeuCau(new LinkedHashMap<>(chiTiet), "");
        DonHangNhap d2 = controller.submitTaoYeuCau(new LinkedHashMap<>(chiTiet), "");

        assertNotNull(d1);
        assertNotNull(d2);
        assertNotEquals("ID của hai đơn không được trùng nhau", d1.getId(), d2.getId());
    }

    /**
     * TC-NH-WB08 - Kiểm tra danh sách sản phẩm DAO mock: phải không rỗng và >= 5 sản phẩm.
     */
    @Test
    public void testWB08_DanhSachSanPhamMock() {
        List<SanPham> dsSP = controller.layDanhSachSanPham();
        assertNotNull("Danh sách sản phẩm không được null", dsSP);
        assertTrue("Mock data phải có ít nhất 5 sản phẩm", dsSP.size() >= 5);
    }

    /**
     * TC-NH-WB09 - Tìm kiếm sản phẩm theo từ khóa đúng (MH001).
     * Expected: Phải tìm thấy ít nhất 1 kết quả, và mã/tên chứa từ khóa.
     */
     @Test
     public void testWB09_TimKiemSanPham_Hop_Le() {
         List<SanPham> ketQua = controller.timKiemSanPham("MH001");
         assertNotNull(ketQua);
         assertFalse("Phải tìm thấy kết quả cho 'MH001'", ketQua.isEmpty());
         assertEquals("Phải tìm đúng mã MH001", "MH001", ketQua.get(0).getMaSanPham());
    }

    /**
     * TC-NH-WB10 - Tìm kiếm sản phẩm với từ khóa không tồn tại → danh sách rỗng.
     */
    @Test
    public void testWB10_TimKiemSanPham_KhongTonTai() {
        List<SanPham> ketQua = controller.timKiemSanPham("XXXXNOTEXIST9999");
        assertNotNull(ketQua);
        assertTrue("Phải trả về rỗng khi không tìm thấy", ketQua.isEmpty());
    }

    /**
     * TC-NH-WB11 - Thay đổi người dùng hiện tại hoạt động và kiểm tra tạo đơn hàng ghi nhận đúng người tạo.
     */
    @Test
    public void testThayDoiNguoiDungHienTai_TaoDonHangGhiNhanDung() {
        // Đổi người dùng sang ND-002 (Vũ Tiến Lợi)
        controller.doiNguoiDungHienTai("ND-002");
        
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 5);
        
        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "");
        assertNotNull(result);
        assertEquals("Người tạo đơn hàng phải ghi nhận đúng nhân viên mới", "Vũ Tiến Lợi", result.getTaoBoi());
        
        // Reset về mặc định để tránh ảnh hưởng test case khác
        controller.doiNguoiDungHienTai("ND-001");
    }

    /**
     * TC-NH-WB12 - Kiểm tra việc đồng bộ tự động sang YeuCauNhapHangDAO khi tạo đơn thành công.
     */
    @Test
    public void testSubmitTaoYeuCau_DongBoSangYeuCauNhapHangDAO() {
        Map<SanPham, Integer> chiTiet = new LinkedHashMap<>();
        chiTiet.put(sp1, 10);
        chiTiet.put(sp2, 20);

        DonHangNhap result = controller.submitTaoYeuCau(chiTiet, "Đồng bộ test");
        assertNotNull(result);

        // Lấy từ YeuCauNhapHangDAO xem có tồn tại yêu cầu tương ứng
        dao.YeuCauNhapHangDAO ycDAO = new dao.YeuCauNhapHangDAO();
        entity.YeuCauNhapHang yc = ycDAO.findById(result.getId());

        assertNotNull("Yêu cầu nhập hàng tương ứng phải tồn tại trong YeuCauNhapHangDAO", yc);
        assertEquals("ID yêu cầu phải khớp với ID đơn hàng nhập", result.getId(), yc.getYeuCauID());
        assertEquals("Số mặt hàng đồng bộ phải khớp", 2, yc.getSoMatHang());
        assertEquals("Trạng thái mặc định phải là CHO_PHAN_BO", "CHO_PHAN_BO", yc.getTrangThai());
    }
}
