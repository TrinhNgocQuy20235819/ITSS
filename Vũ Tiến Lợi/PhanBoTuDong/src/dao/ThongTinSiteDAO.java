package dao;

import entity.ThongTinSite;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp truy xuất dữ liệu cho ThôngTinSite (Mock Data).
 * Tương ứng với class ThôngTinSiteDAO trong Class Diagram.
 * Phương thức: findByDSSiteID() : List
 */
public class ThongTinSiteDAO {

    private List<ThongTinSite> mockData;

    public ThongTinSiteDAO() {
        mockData = new ArrayList<>();
        initMockData();
    }

    private void initMockData() {
        // Dữ liệu Site với thời gian vận chuyển bằng Tàu và Hàng không
        // soNgayGiaoTau: thời gian vận chuyển bằng tàu (ngày)
        // soNgayGiaoHangKhong: thời gian vận chuyển bằng hàng không (ngày)
        mockData.add(new ThongTinSite("SITE-JP01", "Nhà kho Tokyo, Nhật Bản", 20, 5, "Đối tác chiến lược"));
        mockData.add(new ThongTinSite("SITE-TW02", "Nhà kho Đài Bắc, Đài Loan", 15, 4, "Đối tác lâu năm"));
        mockData.add(new ThongTinSite("SITE-KR03", "Nhà kho Seoul, Hàn Quốc", 18, 5, "Đối tác mới"));
        mockData.add(new ThongTinSite("SITE-CN04", "Nhà kho Thượng Hải, Trung Quốc", 10, 3, "Đối tác lớn nhất"));
        mockData.add(new ThongTinSite("SITE-SG05", "Nhà kho Singapore", 12, 4, "Đối tác Đông Nam Á"));
    }

    /**
     * Tìm danh sách thông tin Site theo danh sách mã Site.
     * Tương ứng: findByDSSiteID() : List trong Class Diagram.
     */
    public List<ThongTinSite> findByDSSiteID(List<String> dsSiteID) {
        List<ThongTinSite> result = new ArrayList<>();
        for (ThongTinSite site : mockData) {
            if (dsSiteID.contains(site.getMaSite())) {
                result.add(site);
            }
        }
        return result;
    }

    /**
     * Tìm thông tin 1 Site theo mã.
     */
    public ThongTinSite findById(String maSite) {
        for (ThongTinSite site : mockData) {
            if (site.getMaSite().equals(maSite)) {
                return site;
            }
        }
        return null;
    }

    /**
     * Lấy toàn bộ danh sách Site.
     */
    public List<ThongTinSite> findAll() {
        return new ArrayList<>(mockData);
    }
}
