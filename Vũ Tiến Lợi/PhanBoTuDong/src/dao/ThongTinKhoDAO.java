package dao;

import entity.ThongTinKho;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu cho ThôngTinKho (Mock Data).
 * Tương ứng với class ThôngTinKhoDAO trong Class Diagram.
 * Phương thức: findByMãHàng() : List
 */
public class ThongTinKhoDAO {

    private List<ThongTinKho> mockData;

    public ThongTinKhoDAO() {
        mockData = new ArrayList<>();
        initMockData();
    }

    private void initMockData() {
        // ===== Tồn kho cho MH001 - Linh kiện IC-7805 (yêu cầu 500) =====
        // 5 Site có tồn kho, tổng = 150+200+100+80+50 = 580 > 500 -> Đủ hàng
        mockData.add(new ThongTinKho("SITE-JP01", "MH001", 150, "Cái"));
        mockData.add(new ThongTinKho("SITE-TW02", "MH001", 200, "Cái"));
        mockData.add(new ThongTinKho("SITE-KR03", "MH001", 100, "Cái"));
        mockData.add(new ThongTinKho("SITE-CN04", "MH001", 80, "Cái"));
        mockData.add(new ThongTinKho("SITE-SG05", "MH001", 50, "Cái"));

        // ===== Tồn kho cho MH002 - Tụ điện 100uF (yêu cầu 1000) =====
        // 3 Site, tổng = 400+350+300 = 1050 > 1000 -> Đủ hàng
        mockData.add(new ThongTinKho("SITE-JP01", "MH002", 400, "Cái"));
        mockData.add(new ThongTinKho("SITE-CN04", "MH002", 350, "Cái"));
        mockData.add(new ThongTinKho("SITE-TW02", "MH002", 300, "Cái"));

        // ===== Tồn kho cho MH003 - Điện trở 10K (yêu cầu 2000) =====
        // 4 Site, tổng = 800+600+500+200 = 2100 > 2000 -> Đủ hàng
        mockData.add(new ThongTinKho("SITE-CN04", "MH003", 800, "Cái"));
        mockData.add(new ThongTinKho("SITE-JP01", "MH003", 600, "Cái"));
        mockData.add(new ThongTinKho("SITE-TW02", "MH003", 500, "Cái"));
        mockData.add(new ThongTinKho("SITE-KR03", "MH003", 200, "Cái"));

        // ===== Tồn kho cho MH004 - Arduino Uno R3 (yêu cầu 200) =====
        // 2 Site, tổng = 120+90 = 210 > 200 -> Đủ hàng (vừa khít)
        mockData.add(new ThongTinKho("SITE-JP01", "MH004", 120, "Cái"));
        mockData.add(new ThongTinKho("SITE-SG05", "MH004", 90, "Cái"));

        // ===== Tồn kho cho MH005 - Cảm biến DS18B20 (yêu cầu 300) =====
        // 3 Site, tổng = 100+80+70 = 250 < 300 -> THIẾU HÀNG (test cảnh báo)
        mockData.add(new ThongTinKho("SITE-TW02", "MH005", 100, "Cái"));
        mockData.add(new ThongTinKho("SITE-KR03", "MH005", 80, "Cái"));
        mockData.add(new ThongTinKho("SITE-CN04", "MH005", 70, "Cái"));

        // ===== Tồn kho cho MH007 - Chip ARM Cortex-M4 (yêu cầu 800, deadline gấp) =====
        // 2 Site, tổng = 500+400 = 900 > 800 -> Đủ hàng nhưng deadline rất gấp
        mockData.add(new ThongTinKho("SITE-JP01", "MH007", 500, "Cái"));
        mockData.add(new ThongTinKho("SITE-KR03", "MH007", 400, "Cái"));
    }

    /**
     * Tìm danh sách thông tin tồn kho theo mã hàng.
     * Tương ứng: findByMãHàng() : List trong Class Diagram.
     * Chỉ trả về các Site có tồn kho > 0.
     */
    public List<ThongTinKho> findByMaHang(String maHang) {
        List<ThongTinKho> result = new ArrayList<>();
        for (ThongTinKho ttk : mockData) {
            if (ttk.getMaHang().equals(maHang) && ttk.getSoLuongTonKho() > 0) {
                result.add(ttk);
            }
        }
        return result;
    }
}
