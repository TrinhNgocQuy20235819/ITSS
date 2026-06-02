package dao;

import entity.KetQuaPhanBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu cho KếtQuảPhânBổ (Mock Data - lưu trong bộ nhớ).
 * Tương ứng với class KếtQuảPhânBổDAO trong Class Diagram.
 * Phương thức: saveAll() : void
 */
public class KetQuaPhanBoDAO {

    private List<KetQuaPhanBo> savedResults;

    public KetQuaPhanBoDAO() {
        savedResults = new ArrayList<>();
    }

    /**
     * Lưu toàn bộ kết quả phân bổ.
     * Tương ứng: saveAll() : void trong Class Diagram.
     */
    public void saveAll(List<KetQuaPhanBo> dsKetQua) {
        savedResults.addAll(dsKetQua);
        System.out.println("[KetQuaPhanBoDAO] Đã lưu " + dsKetQua.size() + " kết quả phân bổ.");
    }

    /**
     * Lấy toàn bộ kết quả đã lưu (dùng để kiểm tra).
     */
    public List<KetQuaPhanBo> findAll() {
        return new ArrayList<>(savedResults);
    }

    /**
     * Xóa toàn bộ kết quả (reset).
     */
    public void clear() {
        savedResults.clear();
    }
}
