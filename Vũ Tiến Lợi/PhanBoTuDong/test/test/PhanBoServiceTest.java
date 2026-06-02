package test;

import dao.KetQuaPhanBoDAO;
import dao.ThongTinKhoDAO;
import dao.ThongTinSiteDAO;
import dao.YeuCauNhapHangDAO;
import entity.KetQuaPhanBo;
import entity.MatHang;
import entity.ThongTinKho;
import entity.ThongTinSite;
import entity.YeuCauNhapHang;
import service.PhanBoService;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Kiểm thử tự động cho PhanBoService (thuật toán phân bổ Greedy).
 *
 * Bao gồm:
 *   - Kiểm thử hộp đen (Black-box): Dựa trên đặc tả SRS, kiểm tra input/output
 *   - Kiểm thử hộp trắng (White-box, C1): Bao phủ tất cả các nhánh quyết định
 *
 * Module được chọn kiểm thử: PhanBoService.tinhToanPhanBoTuDong()
 */
public class PhanBoServiceTest {

    private PhanBoService service;

    @Before
    public void setUp() {
        service = new PhanBoService();
    }

    // ========================================================================
    // KIỂM THỬ HỘP ĐEN (Black-box Testing)
    // Dựa trên Luồng sự kiện chính & các luồng thay thế trong SRS
    // ========================================================================

    /**
     * TC01 - Luồng chính: Phân bổ thành công hoàn toàn bằng đường Tàu.
     * Input: YC-2025-001 (3 mặt hàng, deadline dài ~40-50 ngày)
     * Expected: Tất cả mặt hàng được phân bổ đủ SL, không có cảnh báo thiếu hàng.
     */
    @Test
    public void testTC01_PhanBoThanhCongBangTau() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        assertNotNull("Kết quả phân bổ không được null", ketQua);
        assertFalse("Kết quả phân bổ không được rỗng", ketQua.isEmpty());

        // Kiểm tra không có cảnh báo thiếu hàng cho MH001 và MH003
        // (MH001: yêu cầu 500, tổng kho = 580; MH003: yêu cầu 2000, tổng kho = 2100)
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH001") || kq.getMaHang().equals("MH003")) {
                if (kq.isThieuHang()) {
                    fail("MH001 và MH003 phải được phân bổ đủ, không được thiếu hàng");
                }
            }
        }
    }

    /**
     * TC02 - Kiểm tra tổng SL phân bổ = SL yêu cầu (cho mặt hàng đủ hàng).
     * Input: MH001 trong YC-2025-001 (yêu cầu 500 cái)
     * Expected: Tổng SL phân bổ cho MH001 = 500
     */
    @Test
    public void testTC02_TongSLPhanBoBangSLYeuCau() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        int tongPhanBoMH001 = 0;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH001") && !kq.isThieuHang()) {
                tongPhanBoMH001 += kq.getSoLuongPhanBo();
            }
        }
        assertEquals("Tổng SL phân bổ cho MH001 phải = 500", 500, tongPhanBoMH001);
    }

    /**
     * TC03 - Luồng thay thế: Cảnh báo thiếu hàng.
     * Input: YC-2025-002 có MH005 (yêu cầu 300, tổng kho chỉ 250)
     * Expected: Có ít nhất 1 kết quả thiếu hàng cho MH005.
     */
    @Test
    public void testTC03_CanhBaoThieuHang() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-002");

        boolean coThieuHangMH005 = false;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH005") && kq.isThieuHang()) {
                coThieuHangMH005 = true;
                break;
            }
        }
        assertTrue("MH005 phải có cảnh báo thiếu hàng (yêu cầu 300 > tổng kho 250)", coThieuHangMH005);
    }

    /**
     * TC04 - Kiểm tra SL thiếu = SL yêu cầu - tổng SL đã phân bổ.
     * Input: MH005 (yêu cầu 300, tổng kho 250)
     * Expected: SL thiếu = 300 - 250 = 50
     */
    @Test
    public void testTC04_SoLuongThieuChinhXac() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-002");

        int slThieu = 0;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH005") && kq.isThieuHang()) {
                slThieu = kq.getSoLuongPhanBo();
            }
        }
        assertEquals("SL thiếu cho MH005 phải = 50 (300 - 250)", 50, slThieu);
    }

    /**
     * TC05 - Ưu tiên đường Tàu trước Hàng không.
     * Input: YC-2025-001 (deadline 40-50 ngày, đủ thời gian đi Tàu)
     * Expected: Các kết quả phân bổ sử dụng PT vận chuyển = "Tàu"
     */
    @Test
    public void testTC05_UuTienDuongTau() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        for (KetQuaPhanBo kq : ketQua) {
            if (!kq.isThieuHang()) {
                // Với deadline dài (40-50 ngày), tất cả site đều giao kịp bằng Tàu
                assertEquals("Với deadline dài, phải ưu tiên đường Tàu",
                        "Tàu", kq.getPhuongTienVC());
            }
        }
    }

    /**
     * TC06 - Yêu cầu không tồn tại.
     * Input: ID không hợp lệ "YC-INVALID"
     * Expected: Trả về danh sách rỗng
     */
    @Test
    public void testTC06_YeuCauKhongTonTai() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-INVALID");

        assertNotNull("Phải trả về list (không null)", ketQua);
        assertTrue("List phải rỗng khi YC không tồn tại", ketQua.isEmpty());
    }

    /**
     * TC07 - Kiểm tra danh sách yêu cầu sẵn sàng.
     * Expected: Chỉ trả về các yêu cầu có trạng thái "CHO_PHAN_BO"
     */
    @Test
    public void testTC07_LayDSYeuCauSanSang() {
        List<YeuCauNhapHang> dsYC = service.layDSYCSanSang();

        assertNotNull("DS yêu cầu không được null", dsYC);
        assertFalse("DS yêu cầu không được rỗng", dsYC.isEmpty());

        for (YeuCauNhapHang yc : dsYC) {
            assertEquals("Chỉ trả về yêu cầu sẵn sàng phân bổ",
                    "CHO_PHAN_BO", yc.getTrangThai());
        }
    }

    /**
     * TC08 - Kiểm tra kết quả sắp xếp theo SL kho giảm dần.
     * Input: MH001 (nhiều site có tồn kho khác nhau)
     * Expected: Site có SL kho lớn nhất được phân bổ trước (xuất hiện trước trong kết quả)
     */
    @Test
    public void testTC08_SapXepSLKhoGiamDan() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        // Lấy các kết quả cho MH001
        List<KetQuaPhanBo> ketQuaMH001 = new ArrayList<>();
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH001") && !kq.isThieuHang()) {
                ketQuaMH001.add(kq);
            }
        }

        // Kiểm tra SL kho giảm dần
        for (int i = 0; i < ketQuaMH001.size() - 1; i++) {
            assertTrue("Site có SL kho lớn hơn phải được chọn trước",
                    ketQuaMH001.get(i).getSoLuongKho() >= ketQuaMH001.get(i + 1).getSoLuongKho());
        }
    }

    // ========================================================================
    // KIỂM THỬ HỘP TRẮNG (White-box Testing, C1 - Branch Coverage)
    // Bao phủ các nhánh quyết định trong tinhPhanBoChoMotMatHang()
    // ========================================================================

    /**
     * TC-WB01 - Nhánh: slConThieu == 0 sau bước Tàu (không cần Hàng không).
     * Bao phủ: if (slConThieu > 0) --> FALSE (không vào nhánh Hàng không)
     */
    @Test
    public void testWB01_DuHangSauBuocTau() {
        // MH001: yêu cầu 500, tổng kho Tàu đủ (>= 500)
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        boolean coHangKhongMH001 = false;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH001") && "Hàng không".equals(kq.getPhuongTienVC())) {
                coHangKhongMH001 = true;
            }
        }
        assertFalse("MH001 đủ hàng bằng Tàu, không cần Hàng không", coHangKhongMH001);
    }

    /**
     * TC-WB02 - Nhánh: slConThieu > 0 sau cả Tàu lẫn Hàng không (thiếu hàng).
     * Bao phủ: if (slConThieu > 0) sau Hàng không --> TRUE (tạo cảnh báo)
     */
    @Test
    public void testWB02_ThieuHangSauCaHaiBuoc() {
        // MH005: yêu cầu 300, tổng kho 250 -> thiếu
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-002");

        boolean coThieu = false;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH005") && kq.isThieuHang()) {
                coThieu = true;
            }
        }
        assertTrue("MH005 phải có kết quả thiếu hàng", coThieu);
    }

    /**
     * TC-WB03 - Nhánh: yeuCau == null (ID không hợp lệ).
     * Bao phủ: if (yeuCau == null) --> TRUE (return rỗng)
     */
    @Test
    public void testWB03_YeuCauNull() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("KHONG_TON_TAI");
        assertTrue("Phải trả về list rỗng khi yêu cầu null", ketQua.isEmpty());
    }

    /**
     * TC-WB04 - Kiểm tra hàm tinhSLConThieu.
     * Bao phủ: Math.max(0, soLuongYeuCau - slDaPhanBo)
     */
    @Test
    public void testWB04_TinhSLConThieu() {
        // Sử dụng reflection hoặc test gián tiếp qua kết quả
        // Test case: SL yêu cầu > SL đã phân bổ
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-002");

        // MH005: yêu cầu 300, tổng phân bổ = 250, thiếu = 50
        int tongPhanBo = 0;
        int slThieu = 0;
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH005")) {
                if (kq.isThieuHang()) {
                    slThieu = kq.getSoLuongPhanBo();
                } else {
                    tongPhanBo += kq.getSoLuongPhanBo();
                }
            }
        }
        assertEquals("Tổng phân bổ + SL thiếu phải = SL yêu cầu (300)",
                300, tongPhanBo + slThieu);
    }

    /**
     * TC-WB05 - Kiểm tra vòng lặp chọn Site dừng đúng khi đủ SL.
     * Bao phủ: if (slDaPhanBo >= soLuongYeuCau) --> break
     */
    @Test
    public void testWB05_DungKhiDuSL() {
        List<KetQuaPhanBo> ketQua = service.tinhToanPhanBoTuDong("YC-2025-001");

        // MH001: yêu cầu 500. Kho: TW02(200), JP01(150), KR03(100), CN04(80), SG05(50)
        // Sắp xếp giảm dần: TW02(200) -> JP01(150) -> KR03(100) -> CN04(80) -> SG05(50)
        // Greedy: TW02(200) + JP01(150) + KR03(100) + CN04(50) = 500 -> Dừng, không lấy SG05
        List<KetQuaPhanBo> ketQuaMH001 = new ArrayList<>();
        for (KetQuaPhanBo kq : ketQua) {
            if (kq.getMaHang().equals("MH001") && !kq.isThieuHang()) {
                ketQuaMH001.add(kq);
            }
        }

        // Tổng phải đúng = 500, không lấy dư
        int tong = 0;
        for (KetQuaPhanBo kq : ketQuaMH001) {
            tong += kq.getSoLuongPhanBo();
        }
        assertEquals("Tổng phân bổ MH001 phải chính xác = 500", 500, tong);
    }
}
