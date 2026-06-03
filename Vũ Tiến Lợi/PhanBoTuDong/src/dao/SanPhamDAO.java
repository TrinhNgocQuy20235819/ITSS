package dao;

import entity.SanPham;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Danh mục sản phẩm (Mock Data).
 * Cung cấp danh sách sản phẩm cho Bộ phận bán hàng khi tạo yêu cầu nhập hàng.
 */
public class SanPhamDAO {

    private static final List<SanPham> DANH_SACH_SAN_PHAM = new ArrayList<>();

    static {
        // Mock data - khớp hoàn toàn với danh mục bên Phân bổ đơn hàng (mã MH)
        DANH_SACH_SAN_PHAM.add(new SanPham("MH001", "Linh kiện bán dẫn IC-7805",     12_500));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH002", "Tụ điện gốm 100uF",              2_200));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH003", "Điện trở 10K Ohm",                 500));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH004", "Board mạch Arduino Uno R3",    180_000));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH005", "Cảm biến nhiệt độ DS18B20",     35_000));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH006", "Dây cáp USB Type-C",            25_000));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH007", "Chip xử lý ARM Cortex-M4",     450_000));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH008", "LED RGB 5mm",                    1_500));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH009", "Màn hình LCD 16x2",             45_000));
        DANH_SACH_SAN_PHAM.add(new SanPham("MH010", "Module WiFi ESP8266",          120_000));
    }

    /** Lấy toàn bộ danh sách sản phẩm. */
    public List<SanPham> getAllSanPham() {
        return new ArrayList<>(DANH_SACH_SAN_PHAM);
    }

    /** Tìm kiếm sản phẩm theo từ khóa (mã hoặc tên). */
    public List<SanPham> timKiem(String tuKhoa) {
        List<SanPham> ketQua = new ArrayList<>();
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return getAllSanPham();
        }
        String kw = tuKhoa.toLowerCase().trim();
        for (SanPham sp : DANH_SACH_SAN_PHAM) {
            if (sp.getMaSanPham().toLowerCase().contains(kw)
                    || sp.getTenSanPham().toLowerCase().contains(kw)) {
                ketQua.add(sp);
            }
        }
        return ketQua;
    }

    /** Tìm sản phẩm theo mã. */
    public SanPham findByMa(String ma) {
        for (SanPham sp : DANH_SACH_SAN_PHAM) {
            if (sp.getMaSanPham().equals(ma)) return sp;
        }
        return null;
    }
}
