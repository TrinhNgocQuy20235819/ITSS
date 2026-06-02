package dao;

import entity.MatHang;
import entity.YeuCauNhapHang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu cho YêuCầuNhậpHàng (Mock Data).
 * Tương ứng với class YêuCầuNhậpHàngDAO trong Class Diagram.
 * Phương thức: findByTrạngThái() : List, findById() : void
 */
public class YeuCauNhapHangDAO {

    private List<YeuCauNhapHang> mockData;

    public YeuCauNhapHangDAO() {
        mockData = new ArrayList<>();
        initMockData();
    }

    private void initMockData() {
        // ===== Yêu cầu 1: Có 3 mặt hàng =====
        List<MatHang> dsMH1 = new ArrayList<>();
        dsMH1.add(new MatHang("MH001", "Linh kiện bán dẫn IC-7805", 500, "Cái", addDays(new Date(), 45)));
        dsMH1.add(new MatHang("MH002", "Tụ điện gốm 100uF", 1000, "Cái", addDays(new Date(), 40)));
        dsMH1.add(new MatHang("MH003", "Điện trở 10K Ohm", 2000, "Cái", addDays(new Date(), 50)));

        mockData.add(new YeuCauNhapHang("YC-2025-001", addDays(new Date(), -5), "CHO_PHAN_BO", dsMH1));

        // ===== Yêu cầu 2: Có 2 mặt hàng =====
        List<MatHang> dsMH2 = new ArrayList<>();
        dsMH2.add(new MatHang("MH004", "Board mạch Arduino Uno R3", 200, "Cái", addDays(new Date(), 30)));
        dsMH2.add(new MatHang("MH005", "Cảm biến nhiệt độ DS18B20", 300, "Cái", addDays(new Date(), 35)));

        mockData.add(new YeuCauNhapHang("YC-2025-002", addDays(new Date(), -3), "CHO_PHAN_BO", dsMH2));

        // ===== Yêu cầu 3: Đã phân bổ xong (không hiển thị) =====
        List<MatHang> dsMH3 = new ArrayList<>();
        dsMH3.add(new MatHang("MH006", "Dây cáp USB Type-C", 100, "Cái", addDays(new Date(), 20)));

        mockData.add(new YeuCauNhapHang("YC-2025-003", addDays(new Date(), -10), "DA_PHAN_BO", dsMH3));

        // ===== Yêu cầu 4: Deadline rất gấp (test case cảnh báo thiếu hàng) =====
        List<MatHang> dsMH4 = new ArrayList<>();
        dsMH4.add(new MatHang("MH007", "Chip xử lý ARM Cortex-M4", 800, "Cái", addDays(new Date(), 10)));

        mockData.add(new YeuCauNhapHang("YC-2025-004", addDays(new Date(), -1), "CHO_PHAN_BO", dsMH4));
    }

    /**
     * Tìm danh sách yêu cầu theo trạng thái.
     * Tương ứng: findByTrạngThái() : List trong Class Diagram.
     * Chỉ lấy các yêu cầu có trạng thái "CHO_PHAN_BO" (đã nhận tồn kho, sẵn sàng phân bổ).
     */
    public List<YeuCauNhapHang> findByTrangThai(String trangThai) {
        List<YeuCauNhapHang> result = new ArrayList<>();
        for (YeuCauNhapHang yc : mockData) {
            if (yc.getTrangThai().equals(trangThai)) {
                result.add(yc);
            }
        }
        return result;
    }

    /**
     * Tìm yêu cầu theo ID.
     * Tương ứng: findById() trong Class Diagram.
     */
    public YeuCauNhapHang findById(String yeuCauID) {
        for (YeuCauNhapHang yc : mockData) {
            if (yc.getYeuCauID().equals(yeuCauID)) {
                return yc;
            }
        }
        return null;
    }

    /**
     * Cập nhật trạng thái yêu cầu (ví dụ: sau khi phân bổ xong).
     */
    public void updateTrangThai(String yeuCauID, String trangThaiMoi) {
        for (YeuCauNhapHang yc : mockData) {
            if (yc.getYeuCauID().equals(yeuCauID)) {
                yc.setTrangThai(trangThaiMoi);
                break;
            }
        }
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
