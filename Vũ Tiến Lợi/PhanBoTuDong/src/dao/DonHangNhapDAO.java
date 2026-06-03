package dao;

import entity.DonHangNhap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO: Lưu trữ và truy xuất Yêu cầu nhập hàng (in-memory).
 * Tương ứng: AddYeuCauNhapHang(NewYeuCau : DonHangNhap) trong Class Diagram.
 */
public class DonHangNhapDAO {

    private static final List<DonHangNhap> DANH_SACH = new ArrayList<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    /**
     * Sinh mã ID yêu cầu mới tự động.
     * Format: YCN-2025-001, YCN-2025-002, ...
     */
    public String generateId() {
        return String.format("YCN-2025-%03d", COUNTER.getAndIncrement());
    }

    /**
     * Thêm một yêu cầu nhập hàng vào danh sách.
     * Tương ứng: AddYeuCauNhapHang(NewYeuCau) trong Sequence Diagram.
     */
    public void add(DonHangNhap donHang) {
        DANH_SACH.add(donHang);
    }

    /**
     * Lấy toàn bộ danh sách yêu cầu nhập hàng.
     */
    public List<DonHangNhap> getAll() {
        return new ArrayList<>(DANH_SACH);
    }

    /**
     * Lấy danh sách theo trạng thái.
     */
    public List<DonHangNhap> getByTrangThai(String trangThai) {
        List<DonHangNhap> ketQua = new ArrayList<>();
        for (DonHangNhap d : DANH_SACH) {
            if (d.getTrangThai().equals(trangThai)) {
                ketQua.add(d);
            }
        }
        return ketQua;
    }

    /**
     * Tìm đơn theo ID.
     */
    public DonHangNhap findById(String id) {
        for (DonHangNhap d : DANH_SACH) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }

    /**
     * Tổng số yêu cầu trong danh sách.
     */
    public int count() {
        return DANH_SACH.size();
    }
}
