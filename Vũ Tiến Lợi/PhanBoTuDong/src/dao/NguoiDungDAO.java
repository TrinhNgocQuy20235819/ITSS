package dao;

import entity.NguoiDung;

import java.util.ArrayList;
import java.util.List;

/**
 * DAO: Thông tin Người dùng hệ thống (Mock Data).
 * Cung cấp thông tin người dùng đang đăng nhập khi tạo yêu cầu nhập hàng.
 */
public class NguoiDungDAO {

    private static final List<NguoiDung> DANH_SACH_NGUOI_DUNG = new ArrayList<>();

    /** Mock người dùng đang đăng nhập */
    private static String maNguoiDungHienTai = "ND-001";

    public static String getMaNguoiDungHienTai() {
        return maNguoiDungHienTai;
    }

    public static void setMaNguoiDungHienTai(String ma) {
        maNguoiDungHienTai = ma;
    }

    static {
        DANH_SACH_NGUOI_DUNG.add(new NguoiDung("ND-001", "Nguyễn Hải Đăng", "dangnh@itss.vn", "NV Bán hàng"));
        DANH_SACH_NGUOI_DUNG.add(new NguoiDung("ND-002", "Vũ Tiến Lợi",     "loivt@itss.vn",  "NV Đặt hàng quốc tế"));
        DANH_SACH_NGUOI_DUNG.add(new NguoiDung("ND-003", "Trịnh Ngọc Quý",  "quytno@itss.vn", "Trưởng phòng Kinh doanh"));
    }

    /**
     * Lấy thông tin người dùng đang đăng nhập (mock).
     * Tương ứng: LayThongTinNguoiDung(MaNguoiDung) trong Sequence Diagram.
     */
    public NguoiDung getNguoiDungHienTai() {
        return findByMa(maNguoiDungHienTai);
    }

    /** Tìm người dùng theo mã. */
    public NguoiDung findByMa(String ma) {
        for (NguoiDung nd : DANH_SACH_NGUOI_DUNG) {
            if (nd.getMaNguoiDung().equals(ma)) return nd;
        }
        return null;
    }

    public List<NguoiDung> getAll() {
        return new ArrayList<>(DANH_SACH_NGUOI_DUNG);
    }
}
