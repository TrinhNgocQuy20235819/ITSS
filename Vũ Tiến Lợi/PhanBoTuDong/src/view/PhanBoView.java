package view;

import controller.PhanBoController;
import entity.KetQuaPhanBo;
import entity.MatHang;
import entity.YeuCauNhapHang;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Lớp giao diện chính - Hiển thị các màn hình cho Use Case "Tính toán phân bổ tự động".
 * Tương ứng với class PhânBổView / MànHìnhPhânBổ trong Class Diagram.
 *
 * Sơ đồ chuyển màn hình (Screen Transition):
 *   Màn hình 0 (Trang chủ) --> [Nhấn "Phân bổ đơn đặt hàng"] --> Màn hình 1 (DS Yêu cầu)
 *   Màn hình 1 --> [Nhấn "Xem"] --> Màn hình 2 (Chi tiết & Kết quả)
 *   Màn hình 2 --> [Nhấn "Tính toán phân bổ"] --> MH2.2 (Hiển thị bảng kết quả)
 *   MH2.2 --> [Nhấn "Xác nhận phân bổ"] --> MH2.3 (Thông báo thành công)
 *
 * Phương thức (theo Class Diagram):
 *   + hiểnThịDSYêuCầu(ds) : void
 *   + hiểnThịChiTiết() : void
 *   + hiểnThịBảngKếtQuả(ds) : void
 *   + hiểnThịBảngKếtQuảVớiCảnhBáo(ds) : void
 *   + hiểnThịThôngBáo(msg) : void
 */
public class PhanBoView extends JFrame {

    private PhanBoController controller;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Các panel con cho từng màn hình
    private JPanel panelTrangChu;    // Màn hình 0
    private JPanel panelDSYeuCau;    // Màn hình 1
    private JPanel panelChiTiet;     // Màn hình 2

    // Dữ liệu hiện tại
    private String currentYeuCauID;
    private List<KetQuaPhanBo> currentKetQua;

    // Bảng hiển thị
    private JTable tableYeuCau;
    private JTable tableMatHang;
    private JTable tableKetQua;

    // Màu sắc chủ đạo
    private static final Color PRIMARY_COLOR = new Color(41, 98, 255);
    private static final Color PRIMARY_DARK = new Color(24, 62, 171);
    private static final Color ACCENT_COLOR = new Color(0, 200, 83);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color BG_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private static final Color TABLE_HEADER_BG = new Color(52, 58, 64);
    private static final Color TABLE_STRIPE = new Color(248, 249, 250);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 14);

    public PhanBoView() {
        this.controller = new PhanBoController();
        initUI();
    }

    private void initUI() {
        setTitle("Hệ thống đặt hàng nhập khẩu - Phân bổ tự động");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo 3 màn hình
        panelTrangChu = createPanelTrangChu();
        panelDSYeuCau = createPanelDSYeuCau();
        panelChiTiet = createPanelChiTiet();

        mainPanel.add(panelTrangChu, "TRANG_CHU");
        mainPanel.add(panelDSYeuCau, "DS_YEU_CAU");
        mainPanel.add(panelChiTiet, "CHI_TIET");

        add(mainPanel);
        cardLayout.show(mainPanel, "TRANG_CHU");
    }

    // ========================================================================
    // MÀN HÌNH 0: TRANG CHỦ
    // ========================================================================
    private JPanel createPanelTrangChu() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header
        JPanel header = createHeader("TRANG CHỦ - HỆ THỐNG ĐẶT HÀNG NHẬP KHẨU");
        panel.add(header, BorderLayout.NORTH);

        // Nội dung chính - Grid các nút chức năng
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Nút Phân bổ đơn đặt hàng (nút chính)
        JButton btnPhanBo = createMenuButton("📦  Phân bổ đơn đặt hàng", "Tính toán phân bổ tự động cho yêu cầu nhập hàng", PRIMARY_COLOR);
        btnPhanBo.addActionListener(e -> {
            hienThiDSYeuCau(controller.yeuCauDSYeuCau());
            cardLayout.show(mainPanel, "DS_YEU_CAU");
        });
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(btnPhanBo, gbc);

        // Các nút khác (placeholder cho các Use Case khác)
        JButton btnYeuCau = createMenuButton("📋  Quản lý yêu cầu nhập hàng", "Tạo, xem, chỉnh sửa yêu cầu nhập hàng", new Color(108, 117, 125));
        btnYeuCau.setEnabled(false);
        gbc.gridx = 1; gbc.gridy = 0;
        content.add(btnYeuCau, gbc);

        JButton btnDonHang = createMenuButton("🚚  Quản lý đơn hàng", "Xem và gửi đơn hàng tới các Site", new Color(108, 117, 125));
        btnDonHang.setEnabled(false);
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(btnDonHang, gbc);

        JButton btnKiemHang = createMenuButton("✅  Kiểm hàng", "Đối chiếu hàng thực tế với đơn hàng", new Color(108, 117, 125));
        btnKiemHang.setEnabled(false);
        gbc.gridx = 1; gbc.gridy = 1;
        content.add(btnKiemHang, gbc);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ========================================================================
    // MÀN HÌNH 1: DANH SÁCH YÊU CẦU SẴN SÀNG PHÂN BỔ
    // ========================================================================
    private JPanel createPanelDSYeuCau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header
        JPanel header = createHeader("DANH SÁCH YÊU CẦU SẴN SÀNG PHÂN BỔ");
        panel.add(header, BorderLayout.NORTH);

        // Bảng yêu cầu
        String[] columns = {"Mã YC", "Ngày tạo", "Số mặt hàng", "Trạng thái", "Thao tác"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Chỉ cột Thao tác có thể click
            }
        };
        tableYeuCau = createStyledTable(model);

        JScrollPane scrollPane = new JScrollPane(tableYeuCau);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 30, 20, 30),
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1)
        ));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút Quay lại
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        footer.setBackground(BG_COLOR);
        JButton btnBack = createStyledButton("← Về trang chủ", TEXT_SECONDARY, CARD_COLOR);
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "TRANG_CHU"));
        footer.add(btnBack);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Hiển thị danh sách yêu cầu lên bảng.
     * Tương ứng: hiểnThịDSYêuCầu(dsYêuCầu : List) : void
     */
    public void hienThiDSYeuCau(List<YeuCauNhapHang> dsYeuCau) {
        DefaultTableModel model = (DefaultTableModel) tableYeuCau.getModel();
        model.setRowCount(0);

        for (YeuCauNhapHang yc : dsYeuCau) {
            model.addRow(new Object[]{
                    yc.getYeuCauID(),
                    sdf.format(yc.getNgayTao()),
                    yc.getSoMatHang(),
                    yc.getTrangThai(),
                    "Xem chi tiết"
            });
        }

        // Thêm event click vào nút "Xem chi tiết"
        tableYeuCau.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tableYeuCau.rowAtPoint(e.getPoint());
                int col = tableYeuCau.columnAtPoint(e.getPoint());
                if (col == 4 && row >= 0) {
                    String yeuCauID = (String) tableYeuCau.getValueAt(row, 0);
                    currentYeuCauID = yeuCauID;
                    YeuCauNhapHang yc = controller.yeuCauChiTiet(yeuCauID);
                    if (yc != null) {
                        hienThiChiTiet(yc);
                        cardLayout.show(mainPanel, "CHI_TIET");
                    }
                }
            }
        });
    }

    // ========================================================================
    // MÀN HÌNH 2: CHI TIẾT YÊU CẦU & KẾT QUẢ PHÂN BỔ
    // ========================================================================
    private JPanel createPanelChiTiet() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        // Header
        JPanel header = createHeader("CHI TIẾT YÊU CẦU & KẾT QUẢ PHÂN BỔ");
        panel.add(header, BorderLayout.NORTH);

        // Nội dung: Chia đôi trên/dưới
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(10, 30, 10, 30));

        // --- Phần trên: Bảng danh sách mặt hàng ---
        JPanel panelMatHang = createCardPanel("📋 Danh sách mặt hàng trong yêu cầu");
        String[] colMH = {"Mã hàng", "Tên hàng", "SL yêu cầu", "Đơn vị", "Ngày nhận mong muốn"};
        DefaultTableModel modelMH = new DefaultTableModel(colMH, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMatHang = createStyledTable(modelMH);
        JScrollPane scrollMH = new JScrollPane(tableMatHang);
        scrollMH.setPreferredSize(new Dimension(0, 160));
        scrollMH.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panelMatHang.add(scrollMH, BorderLayout.CENTER);
        content.add(panelMatHang);

        content.add(Box.createVerticalStrut(15));

        // --- Nút Tính toán phân bổ ---
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBtn.setBackground(BG_COLOR);
        panelBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JButton btnTinhToan = createStyledButton("⚡ Tính toán phân bổ tự động", Color.WHITE, PRIMARY_COLOR);
        btnTinhToan.setPreferredSize(new Dimension(320, 45));
        btnTinhToan.addActionListener(e -> {
            if (currentYeuCauID != null) {
                currentKetQua = controller.tinhToanPhanBo(currentYeuCauID);
                if (currentKetQua != null && !currentKetQua.isEmpty()) {
                    // Kiểm tra có cảnh báo thiếu hàng không
                    boolean coThieuHang = false;
                    for (KetQuaPhanBo kq : currentKetQua) {
                        if (kq.isThieuHang()) {
                            coThieuHang = true;
                            break;
                        }
                    }
                    if (coThieuHang) {
                        hienThiBangKetQuaVoiCanhBao(currentKetQua);
                    } else {
                        hienThiBangKetQua(currentKetQua);
                    }
                } else {
                    hienThiThongBao("Không có kết quả phân bổ.");
                }
            }
        });
        panelBtn.add(btnTinhToan);

        JButton btnXacNhan = createStyledButton("✅ Xác nhận phân bổ", Color.WHITE, ACCENT_COLOR);
        btnXacNhan.setPreferredSize(new Dimension(220, 45));
        btnXacNhan.addActionListener(e -> {
            if (currentKetQua != null && !currentKetQua.isEmpty()) {
                boolean success = controller.xacNhanPhanBo(currentKetQua, currentYeuCauID);
                if (success) {
                    hienThiThongBao("Phân bổ thành công! Đã lưu kết quả và tạo dự thảo đơn hàng.");
                    // Refresh lại danh sách yêu cầu
                    hienThiDSYeuCau(controller.yeuCauDSYeuCau());
                } else {
                    hienThiThongBao("Có lỗi xảy ra khi xác nhận phân bổ.");
                }
            } else {
                hienThiThongBao("Chưa có kết quả phân bổ. Vui lòng tính toán trước.");
            }
        });
        panelBtn.add(btnXacNhan);

        content.add(panelBtn);
        content.add(Box.createVerticalStrut(10));

        // --- Phần dưới: Bảng kết quả phân bổ ---
        JPanel panelKetQua = createCardPanel("📊 Kết quả phân bổ");
        String[] colKQ = {"Mã hàng", "Tên hàng", "Site", "Tên Site", "SL kho", "SL phân bổ", "PT vận chuyển", "Ngày giao dự kiến"};
        DefaultTableModel modelKQ = new DefaultTableModel(colKQ, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableKetQua = createStyledTable(modelKQ);
        JScrollPane scrollKQ = new JScrollPane(tableKetQua);
        scrollKQ.setPreferredSize(new Dimension(0, 200));
        scrollKQ.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));
        panelKetQua.add(scrollKQ, BorderLayout.CENTER);
        content.add(panelKetQua);

        panel.add(new JScrollPane(content), BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        footer.setBackground(BG_COLOR);
        JButton btnBackDS = createStyledButton("← Quay lại DS yêu cầu", TEXT_SECONDARY, CARD_COLOR);
        btnBackDS.addActionListener(e -> {
            // Reset kết quả
            currentKetQua = null;
            DefaultTableModel m = (DefaultTableModel) tableKetQua.getModel();
            m.setRowCount(0);
            cardLayout.show(mainPanel, "DS_YEU_CAU");
        });
        footer.add(btnBackDS);

        JButton btnHome = createStyledButton("🏠 Về trang chủ", TEXT_SECONDARY, CARD_COLOR);
        btnHome.addActionListener(e -> cardLayout.show(mainPanel, "TRANG_CHU"));
        footer.add(btnHome);

        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Hiển thị chi tiết danh sách mặt hàng.
     * Tương ứng: hiểnThịChiTiết(dsMặtHàng : List) : void
     */
    public void hienThiChiTiet(YeuCauNhapHang yeuCau) {
        DefaultTableModel model = (DefaultTableModel) tableMatHang.getModel();
        model.setRowCount(0);
        DefaultTableModel modelKQ = (DefaultTableModel) tableKetQua.getModel();
        modelKQ.setRowCount(0);
        currentKetQua = null;

        if (yeuCau.getDanhSachMatHang() != null) {
            for (MatHang mh : yeuCau.getDanhSachMatHang()) {
                model.addRow(new Object[]{
                        mh.getMaHang(),
                        mh.getTenHang(),
                        mh.getSoLuongYeuCau(),
                        mh.getDonVi(),
                        sdf.format(mh.getNgayNhanMongMuon())
                });
            }
        }
    }

    /**
     * Hiển thị bảng kết quả phân bổ (không cảnh báo).
     * Tương ứng: hiểnThịBảngKếtQuả(dsKếtQuả : List) : void
     */
    public void hienThiBangKetQua(List<KetQuaPhanBo> dsKetQua) {
        DefaultTableModel model = (DefaultTableModel) tableKetQua.getModel();
        model.setRowCount(0);

        for (KetQuaPhanBo kq : dsKetQua) {
            model.addRow(new Object[]{
                    kq.getMaHang(),
                    kq.getTenHang(),
                    kq.getMaSite(),
                    kq.getTenSite(),
                    kq.getSoLuongKho(),
                    kq.getSoLuongPhanBo(),
                    kq.getPhuongTienVC(),
                    kq.getNgayGiaoDuKien() != null ? sdf.format(kq.getNgayGiaoDuKien()) : "N/A"
            });
        }

        // Tô màu các dòng bình thường
        tableKetQua.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_COLOR : TABLE_STRIPE);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });

        hienThiThongBao("Tính toán phân bổ hoàn tất! Tất cả mặt hàng đều đủ số lượng.");
    }

    /**
     * Hiển thị bảng kết quả phân bổ VỚI cảnh báo thiếu hàng.
     * Tương ứng: hiểnThịBảngKếtQuảVớiCảnhBáo(dsKếtQuả : List) : void
     */
    public void hienThiBangKetQuaVoiCanhBao(List<KetQuaPhanBo> dsKetQua) {
        DefaultTableModel model = (DefaultTableModel) tableKetQua.getModel();
        model.setRowCount(0);

        for (KetQuaPhanBo kq : dsKetQua) {
            model.addRow(new Object[]{
                    kq.getMaHang(),
                    kq.getTenHang(),
                    kq.getMaSite(),
                    kq.getTenSite(),
                    kq.getSoLuongKho(),
                    kq.getSoLuongPhanBo(),
                    kq.getPhuongTienVC(),
                    kq.getNgayGiaoDuKien() != null ? sdf.format(kq.getNgayGiaoDuKien()) : "N/A"
            });
        }

        // Tô đỏ các dòng cảnh báo thiếu hàng
        tableKetQua.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    // Kiểm tra cột "Tên Site" xem có phải dòng cảnh báo không
                    String tenSite = (String) table.getValueAt(row, 3);
                    if ("CẢNH BÁO THIẾU HÀNG".equals(tenSite)) {
                        c.setBackground(new Color(255, 235, 238));
                        c.setForeground(DANGER_COLOR);
                        ((JLabel) c).setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setBackground(row % 2 == 0 ? CARD_COLOR : TABLE_STRIPE);
                        c.setForeground(TEXT_PRIMARY);
                        ((JLabel) c).setFont(FONT_TABLE);
                    }
                }
                return c;
            }
        });

        // Đếm số mặt hàng thiếu
        int soThieu = 0;
        for (KetQuaPhanBo kq : dsKetQua) {
            if (kq.isThieuHang()) soThieu++;
        }
        JOptionPane.showMessageDialog(this,
                "⚠️ CẢNH BÁO: Có " + soThieu + " mặt hàng không đủ số lượng cung cấp!\n" +
                        "Các dòng tô đỏ trong bảng kết quả là mặt hàng bị thiếu.\n" +
                        "Bạn vẫn có thể xác nhận phân bổ cho các mặt hàng còn lại.",
                "Cảnh báo thiếu hàng",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Hiển thị thông báo cho người dùng.
     * Tương ứng: hiểnThịThôngBáo(thôngBáo : String) : void
     */
    public void hienThiThongBao(String thongBao) {
        JOptionPane.showMessageDialog(this, thongBao, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================================================
    // HELPER METHODS - TẠO GIAO DIỆN
    // ========================================================================

    private JPanel createHeader(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);

        JLabel lblUser = new JLabel("👤 NV Đặt hàng quốc tế");
        lblUser.setFont(FONT_BODY);
        lblUser.setForeground(new Color(200, 220, 255));
        header.add(lblUser, BorderLayout.EAST);

        return header;
    }

    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_SUBTITLE);
        lblTitle.setForeground(TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);

        return card;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(233, 236, 239));
        table.setSelectionBackground(new Color(206, 224, 255));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(0, 1));

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_HEADER);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 38));

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? CARD_COLOR : TABLE_STRIPE);
                }
                return c;
            }
        });

        return table;
    }

    private JButton createStyledButton(String text, Color fgColor, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setForeground(fgColor);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.equals(CARD_COLOR) ? new Color(206, 212, 218) : bgColor.darker(), 1),
                new EmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        // Hover effect
        Color hoverBg = bgColor.equals(CARD_COLOR) ? TABLE_STRIPE : bgColor.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private JButton createMenuButton(String title, String subtitle, Color color) {
        JButton btn = new JButton("<html><center><b style='font-size:14px'>" + title +
                "</b><br><span style='font-size:11px; color:#ccc'>" + subtitle + "</span></center></html>");
        btn.setFont(FONT_BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(350, 120));

        // Hover effect
        Color hoverColor = color.darker();
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(color);
            }
        });

        return btn;
    }
}
