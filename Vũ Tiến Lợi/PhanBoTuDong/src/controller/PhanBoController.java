package controller;

import entity.DonHang;
import entity.KetQuaPhanBo;
import entity.YeuCauNhapHang;
import service.PhanBoService;

import java.util.List;

/**
 * Lớp điều khiển - Nhận yêu cầu từ View, gọi Service xử lý, trả kết quả về View.
 * Tương ứng với class PhânBổController trong Class Diagram (mức thiết kế - Bài 5).
 *
 * Phương thức:
 *   + yêuCầuDSYêuCầu() : list
 *   + yêuCầuChiTiết() : void
 *   + tínhToánPhânBổ() : list
 *   + xácNhậnPhânBổ() : boolean
 */
public class PhanBoController {

    private PhanBoService phanBoService;

    public PhanBoController() {
        this.phanBoService = new PhanBoService();
    }

    public PhanBoController(PhanBoService phanBoService) {
        this.phanBoService = phanBoService;
    }

    /**
     * Lấy danh sách yêu cầu sẵn sàng phân bổ.
     * Tương ứng: yêuCầuDSYêuCầu() : list
     */
    public List<YeuCauNhapHang> yeuCauDSYeuCau() {
        return phanBoService.layDSYCSanSang();
    }

    /**
     * Lấy chi tiết một yêu cầu.
     * Tương ứng: yêuCầuChiTiết() : void
     */
    public YeuCauNhapHang yeuCauChiTiet(String yeuCauID) {
        return phanBoService.layChiTiet(yeuCauID);
    }

    /**
     * Tính toán phân bổ tự động cho một yêu cầu.
     * Tương ứng: tínhToánPhânBổ() : list
     */
    public List<KetQuaPhanBo> tinhToanPhanBo(String yeuCauID) {
        return phanBoService.tinhToanPhanBoTuDong(yeuCauID);
    }

    /**
     * Xác nhận phân bổ: lưu kết quả và tạo dự thảo đơn hàng.
     * Tương ứng: xácNhậnPhânBổ() : boolean
     */
    public boolean xacNhanPhanBo(List<KetQuaPhanBo> dsKetQua, String yeuCauID) {
        try {
            List<DonHang> dsDonHang = phanBoService.luuVaTaoDon(dsKetQua, yeuCauID);
            return dsDonHang != null && !dsDonHang.isEmpty();
        } catch (Exception e) {
            System.err.println("[PhanBoController] Lỗi xác nhận phân bổ: " + e.getMessage());
            return false;
        }
    }
}
