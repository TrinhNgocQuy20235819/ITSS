package service;

import dao.KetQuaPhanBoDAO;
import dao.ThongTinKhoDAO;
import dao.ThongTinSiteDAO;
import dao.YeuCauNhapHangDAO;
import entity.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Lớp xử lý nghiệp vụ chính - Thuật toán phân bổ tự động (Greedy).
 * Tương ứng với class PhânBổService trong Class Diagram (mức thiết kế - Bài 5).
 *
 * Thuộc tính nội bộ (biến trung gian trong quá trình tính):
 *   - locSiteKipDeadlineBangTau : List
 *   - sapXepTheoSLKhoGiamDan : List
 *   - chonSiteItNhatDuSL : List
 *   - tinhSLConThieu : int
 *   - locSiteKipDeadlineBangHangKhong : List
 *   - chonSiteItNhatDuSLConThieu : List
 *   - taoDuThaoDonHang : List
 *
 * Phương thức công khai:
 *   + layDSYCSanSang() : list
 *   + layChiTiet() : void
 *   + tinhToanPhanBoTuDong() : list
 *   + luuVaTaoDon() : List
 */
public class PhanBoService {

    private YeuCauNhapHangDAO yeuCauDAO;
    private ThongTinKhoDAO thongTinKhoDAO;
    private ThongTinSiteDAO thongTinSiteDAO;
    private KetQuaPhanBoDAO ketQuaDAO;

    public PhanBoService() {
        this.yeuCauDAO = new YeuCauNhapHangDAO();
        this.thongTinKhoDAO = new ThongTinKhoDAO();
        this.thongTinSiteDAO = new ThongTinSiteDAO();
        this.ketQuaDAO = new KetQuaPhanBoDAO();
    }

    // Constructor cho phép inject DAO (hỗ trợ Unit Test)
    public PhanBoService(YeuCauNhapHangDAO yeuCauDAO, ThongTinKhoDAO thongTinKhoDAO,
                         ThongTinSiteDAO thongTinSiteDAO, KetQuaPhanBoDAO ketQuaDAO) {
        this.yeuCauDAO = yeuCauDAO;
        this.thongTinKhoDAO = thongTinKhoDAO;
        this.thongTinSiteDAO = thongTinSiteDAO;
        this.ketQuaDAO = ketQuaDAO;
    }

    // ========================================================================
    // PHƯƠNG THỨC CÔNG KHAI (Public Methods - theo Class Diagram)
    // ========================================================================

    /**
     * Lấy danh sách yêu cầu sẵn sàng phân bổ (trạng thái = "CHO_PHAN_BO").
     * Tương ứng: lấyDSYCSẵnSàng() : list
     */
    public List<YeuCauNhapHang> layDSYCSanSang() {
        return yeuCauDAO.findByTrangThai("CHO_PHAN_BO");
    }

    /**
     * Lấy chi tiết một yêu cầu theo ID.
     * Tương ứng: lấyChiTiết() : void
     */
    public YeuCauNhapHang layChiTiet(String yeuCauID) {
        return yeuCauDAO.findById(yeuCauID);
    }

    /**
     * THUẬT TOÁN CHÍNH: Tính toán phân bổ tự động cho tất cả mặt hàng trong 1 yêu cầu.
     * Tương ứng: tínhToánPhânBổTựĐộng() : list
     *
     * Luồng xử lý (theo SRS - Luồng sự kiện chính):
     *   1. Lấy danh sách mặt hàng của yêu cầu
     *   2. Với MỖI mặt hàng:
     *      a. Lấy DS Site có tồn kho cho mặt hàng đó
     *      b. Lọc Site kịp deadline bằng Tàu -> Sắp xếp SL kho giảm dần -> Chọn Site ít nhất đủ SL
     *      c. Nếu chưa đủ: Tính SL còn thiếu -> Lọc Site kịp deadline bằng Hàng Không -> Chọn tiếp
     *      d. Nếu vẫn thiếu: Tạo kết quả cảnh báo thiếu hàng
     *   3. Trả về toàn bộ danh sách kết quả phân bổ
     */
    public List<KetQuaPhanBo> tinhToanPhanBoTuDong(String yeuCauID) {
        YeuCauNhapHang yeuCau = yeuCauDAO.findById(yeuCauID);
        if (yeuCau == null) {
            return new ArrayList<>();
        }

        List<KetQuaPhanBo> tatCaKetQua = new ArrayList<>();
        List<MatHang> dsMatHang = yeuCau.layDSMatHang();

        // Xử lý từng mặt hàng độc lập
        for (MatHang mh : dsMatHang) {
            List<KetQuaPhanBo> ketQuaChoMH = tinhPhanBoChoMotMatHang(mh);
            tatCaKetQua.addAll(ketQuaChoMH);
        }

        return tatCaKetQua;
    }

    /**
     * Lưu kết quả phân bổ và tạo dự thảo đơn hàng.
     * Tương ứng: lưuVàTạoĐơn() : List
     */
    public List<DonHang> luuVaTaoDon(List<KetQuaPhanBo> dsKetQua, String yeuCauID) {
        // Lưu kết quả phân bổ vào DAO
        ketQuaDAO.saveAll(dsKetQua);

        // Cập nhật trạng thái yêu cầu
        yeuCauDAO.updateTrangThai(yeuCauID, "DA_PHAN_BO");

        // Tạo dự thảo đơn hàng từ kết quả phân bổ
        List<DonHang> dsDonHang = DonHang.taoDuThaoDonHang(dsKetQua);

        System.out.println("[PhanBoService] Đã lưu kết quả và tạo " + dsDonHang.size() + " dự thảo đơn hàng.");
        return dsDonHang;
    }

    // ========================================================================
    // PHƯƠNG THỨC NỘI BỘ - THUẬT TOÁN GREEDY (Private Methods)
    // ========================================================================

    /**
     * Tính phân bổ cho MỘT mặt hàng cụ thể.
     * Đây là hàm core chạy thuật toán Greedy theo đúng SRS.
     */
    private List<KetQuaPhanBo> tinhPhanBoChoMotMatHang(MatHang matHang) {
        List<KetQuaPhanBo> ketQuaList = new ArrayList<>();
        int soLuongYeuCau = matHang.getSoLuongYeuCau();
        Date ngayMongMuon = matHang.getNgayNhanMongMuon();

        // Bước 1: Lấy DS site có tồn kho cho mặt hàng này
        List<ThongTinKho> dsTonKho = thongTinKhoDAO.findByMaHang(matHang.getMaHang());
        List<String> dsMaSite = new ArrayList<>();
        for (ThongTinKho ttk : dsTonKho) {
            dsMaSite.add(ttk.getMaSite());
        }
        List<ThongTinSite> dsSite = thongTinSiteDAO.findByDSSiteID(dsMaSite);

        // Bước 2: Lọc Site kịp deadline bằng TÀU
        List<SiteVoiTonKho> dsSiteTau = locSiteKipDeadlineBangTau(dsTonKho, dsSite, ngayMongMuon);

        // Bước 3: Sắp xếp theo SL kho giảm dần
        sapXepTheoSLKhoGiamDan(dsSiteTau);

        // Bước 4: Chọn Site ít nhất đủ SL (Greedy)
        int slDaPhanBo = 0;
        slDaPhanBo = chonSiteItNhatDuSL(dsSiteTau, soLuongYeuCau, slDaPhanBo, matHang, "Tàu", ketQuaList);

        // Bước 5: Tính SL còn thiếu
        int slConThieu = tinhSLConThieu(soLuongYeuCau, slDaPhanBo);

        // Bước 6: Nếu chưa đủ -> Lọc Site kịp deadline bằng HÀNG KHÔNG
        if (slConThieu > 0) {
            List<SiteVoiTonKho> dsSiteHK = locSiteKipDeadlineBangHangKhong(dsTonKho, dsSite, ngayMongMuon, dsSiteTau);
            sapXepTheoSLKhoGiamDan(dsSiteHK);

            // Bước 7: Chọn tiếp các Site HK cho SL còn thiếu
            slDaPhanBo = chonSiteItNhatDuSL(dsSiteHK, soLuongYeuCau, slDaPhanBo, matHang, "Hàng không", ketQuaList);
            slConThieu = tinhSLConThieu(soLuongYeuCau, slDaPhanBo);
        }

        // Bước 8: Nếu vẫn thiếu -> Tạo kết quả cảnh báo thiếu hàng
        if (slConThieu > 0) {
            KetQuaPhanBo canhBao = KetQuaPhanBo.taoKetQuaThieuHang(
                    matHang.getMaHang(), matHang.getTenHang(), slConThieu);
            ketQuaList.add(canhBao);
        }

        return ketQuaList;
    }

    /**
     * Lọc các Site giao kịp deadline bằng đường TÀU.
     * Tương ứng thuộc tính nội bộ: locSiteKipDeadlineBangTau : List
     *
     * Điều kiện: Ngày hiện tại + sốNgàyGiaoTàu <= ngàyMongMuốn
     */
    List<SiteVoiTonKho> locSiteKipDeadlineBangTau(List<ThongTinKho> dsTonKho,
                                                          List<ThongTinSite> dsSite,
                                                          Date ngayMongMuon) {
        List<SiteVoiTonKho> result = new ArrayList<>();
        Date homNay = new Date();

        for (ThongTinKho ttk : dsTonKho) {
            ThongTinSite site = timSite(dsSite, ttk.getMaSite());
            if (site != null) {
                Date ngayGiaoDuKien = addDays(homNay, site.getSoNgayGiaoTau());
                if (!ngayGiaoDuKien.after(ngayMongMuon)) {
                    result.add(new SiteVoiTonKho(site, ttk, "Tàu", ngayGiaoDuKien));
                }
            }
        }
        return result;
    }

    /**
     * Lọc các Site giao kịp deadline bằng đường HÀNG KHÔNG.
     * Tương ứng thuộc tính nội bộ: locSiteKipDeadlineBangHangKhong : List
     *
     * Chỉ xét các Site chưa được chọn bằng đường Tàu.
     */
    List<SiteVoiTonKho> locSiteKipDeadlineBangHangKhong(List<ThongTinKho> dsTonKho,
                                                                List<ThongTinSite> dsSite,
                                                                Date ngayMongMuon,
                                                                List<SiteVoiTonKho> daDungTau) {
        List<SiteVoiTonKho> result = new ArrayList<>();
        Date homNay = new Date();

        // Tập hợp các Site đã được phân bổ bằng Tàu
        List<String> daSuDung = new ArrayList<>();
        for (SiteVoiTonKho s : daDungTau) {
            daSuDung.add(s.getSite().getMaSite());
        }

        for (ThongTinKho ttk : dsTonKho) {
            // Bỏ qua Site đã được chọn bằng Tàu
            if (daSuDung.contains(ttk.getMaSite())) {
                continue;
            }
            ThongTinSite site = timSite(dsSite, ttk.getMaSite());
            if (site != null) {
                Date ngayGiaoDuKien = addDays(homNay, site.getSoNgayGiaoHangKhong());
                if (!ngayGiaoDuKien.after(ngayMongMuon)) {
                    result.add(new SiteVoiTonKho(site, ttk, "Hàng không", ngayGiaoDuKien));
                }
            }
        }
        return result;
    }

    /**
     * Sắp xếp danh sách Site theo SL tồn kho GIẢM DẦN.
     * Tương ứng thuộc tính nội bộ: sapXepTheoSLKhoGiamDan : List
     *
     * Ưu tiên (b): "Tại các site có SL tồn kho lớn hơn"
     */
    void sapXepTheoSLKhoGiamDan(List<SiteVoiTonKho> dsSite) {
        Collections.sort(dsSite, new Comparator<SiteVoiTonKho>() {
            @Override
            public int compare(SiteVoiTonKho a, SiteVoiTonKho b) {
                return Integer.compare(b.getTonKho().getSoLuongTonKho(), a.getTonKho().getSoLuongTonKho());
            }
        });
    }

    /**
     * Chọn số lượng Site ít nhất đủ SL yêu cầu (Thuật toán Greedy).
     * Tương ứng thuộc tính nội bộ: chonSiteItNhatDuSL : List
     *
     * Ưu tiên (c): "Lấy tại số lượng site ít nhất có thể"
     * Cách tiếp cận: Sau khi sắp xếp giảm dần, lấy từ trên xuống cho đến khi đủ SL.
     *
     * @return tổng SL đã phân bổ sau khi chọn
     */
    int chonSiteItNhatDuSL(List<SiteVoiTonKho> dsSite, int soLuongYeuCau,
                                   int slDaPhanBo, MatHang matHang,
                                   String phuongTien, List<KetQuaPhanBo> ketQuaList) {
        for (SiteVoiTonKho svtk : dsSite) {
            if (slDaPhanBo >= soLuongYeuCau) {
                break; // Đã đủ SL, dừng lại
            }

            int slConThieu = soLuongYeuCau - slDaPhanBo;
            int slLayTuSiteNay = Math.min(svtk.getTonKho().getSoLuongTonKho(), slConThieu);

            KetQuaPhanBo kq = KetQuaPhanBo.taoKetQua(
                    matHang.getMaHang(),
                    matHang.getTenHang(),
                    svtk.getSite().getMaSite(),
                    svtk.getSite().getTenSite(),
                    svtk.getTonKho().getSoLuongTonKho(),
                    slLayTuSiteNay,
                    phuongTien,
                    svtk.getNgayGiaoDuKien()
            );
            ketQuaList.add(kq);
            slDaPhanBo += slLayTuSiteNay;
        }
        return slDaPhanBo;
    }

    /**
     * Tính số lượng còn thiếu.
     * Tương ứng thuộc tính nội bộ: tinhSLConThieu : int
     */
    int tinhSLConThieu(int soLuongYeuCau, int slDaPhanBo) {
        return Math.max(0, soLuongYeuCau - slDaPhanBo);
    }

    // ========================================================================
    // LỚP HELPER & PHƯƠNG THỨC TIỆN ÍCH
    // ========================================================================

    /**
     * Lớp nội bộ kết hợp thông tin Site + Tồn kho + Phương tiện vận chuyển.
     * Dùng để thuận tiện cho việc sắp xếp và chọn lọc.
     */
    static class SiteVoiTonKho {
        private ThongTinSite site;
        private ThongTinKho tonKho;
        private String phuongTien;
        private Date ngayGiaoDuKien;

        SiteVoiTonKho(ThongTinSite site, ThongTinKho tonKho, String phuongTien, Date ngayGiaoDuKien) {
            this.site = site;
            this.tonKho = tonKho;
            this.phuongTien = phuongTien;
            this.ngayGiaoDuKien = ngayGiaoDuKien;
        }

        public ThongTinSite getSite() { return site; }
        public ThongTinKho getTonKho() { return tonKho; }
        public String getPhuongTien() { return phuongTien; }
        public Date getNgayGiaoDuKien() { return ngayGiaoDuKien; }
    }

    private ThongTinSite timSite(List<ThongTinSite> dsSite, String maSite) {
        for (ThongTinSite site : dsSite) {
            if (site.getMaSite().equals(maSite)) {
                return site;
            }
        }
        return null;
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
