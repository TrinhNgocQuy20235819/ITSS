package view;

import controller.DanhSachYeuCauNhap;
import controller.LapYeuCauNhapHang;
import entity.DonHangNhap;
import entity.NguoiDung;
import entity.SanPham;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Giao diện đơn duy nhất quản lý Yêu cầu nhập hàng (JPanel dạng CardLayout).
 * Nhúng trực tiếp vào PhanBoView.
 *
 * CARD_LIST  : Danh sách yêu cầu (GD3)
 * CARD_CREATE: Tạo yêu cầu mới  (GD4 + GD5 tích hợp)
 */
public class QuanLyYeuCauNhapHangPanel extends JPanel {

    // ---- Cards ----
    public static final String CARD_LIST   = "LIST";
    public static final String CARD_CREATE = "CREATE";

    // ---- Màu sắc ----
    static final Color C_HEADER  = new Color(26,  26,  46);
    static final Color C_PRIMARY = new Color(41,  98,  255);
    static final Color C_BG      = new Color(245, 247, 250);
    static final Color C_CARD    = Color.WHITE;
    static final Color C_ACCENT  = new Color(0,   168, 107);
    static final Color C_DANGER  = new Color(229, 57,  53);
    static final Color C_MUTED   = new Color(108, 117, 125);
    static final Color C_BORDER  = new Color(226, 232, 240);
    static final Color C_ROW_ALT = new Color(248, 250, 252);

    // ---- Controllers ----
    private final DanhSachYeuCauNhap ctrlList   = new DanhSachYeuCauNhap();
    private final LapYeuCauNhapHang  ctrlCreate = new LapYeuCauNhapHang();
    private final SimpleDateFormat   sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final NumberFormat       nf  = NumberFormat.getInstance(new Locale("vi", "VN"));

    private final PhanBoView phanBoView;

    // ---- Layout ----
    private CardLayout  cardLayout;
    private JPanel      cardPanel;
    private JLabel      lblPageTitle;

    // ---- TopBar elements ----
    private JComboBox<NguoiDung> cbCurrentUser;
    private JLabel               lblInfo;

    // ---- GD3: Danh sách ----
    private DefaultTableModel modelList;
    private JTable            tblList;
    private JComboBox<String> cbFilter;
    private JComboBox<String> cbUserFilter;
    private JLabel            lblCount;

    // ---- GD4: Tạo mới ----
    private JTextField         tfSearch;
    private DefaultTableModel  modelCatalog;
    private JTable             tblCatalog;
    private List<SanPham>      catalogItems = new ArrayList<>();

    private DefaultTableModel  modelCart;
    private JTable             tblCart;
    private JLabel             lblTotalPrice;
    private JTextArea          taGhiChu;

    // ====================================================================
    //  KHỞI TẠO
    // ====================================================================
    public QuanLyYeuCauNhapHangPanel(PhanBoView phanBoView) {
        this.phanBoView = phanBoView;
        setLayout(new BorderLayout(0, 0));

        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildCards(),    BorderLayout.CENTER);

        showCard(CARD_LIST);
        loadListData();
    }

    // ====================================================================
    //  TOP BAR (chung cho mọi card)
    // ====================================================================
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(C_HEADER);
        bar.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Trái: icon + tiêu đề động
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel icon = new JLabel("📋");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        lblPageTitle = new JLabel("Danh sách Yêu cầu nhập hàng");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPageTitle.setForeground(Color.WHITE);
        left.add(icon);
        left.add(lblPageTitle);

        // Phải: user switcher + nút quay về trang chủ
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);

        JLabel lblUserText = new JLabel("Nhân viên hoạt động:");
        lblUserText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUserText.setForeground(new Color(144, 205, 244));

        List<NguoiDung> users = ctrlCreate.layDanhSachNguoiDung();
        cbCurrentUser = new JComboBox<>(users.toArray(new NguoiDung[0]));
        cbCurrentUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbCurrentUser.setPreferredSize(new Dimension(200, 28));
        cbCurrentUser.setBackground(Color.WHITE);

        // Đặt người dùng hiện tại làm mặc định được chọn
        NguoiDung ndHienTai = ctrlCreate.layNguoiDungHienTai();
        if (ndHienTai != null) {
            for (NguoiDung u : users) {
                if (u.getMaNguoiDung().equals(ndHienTai.getMaNguoiDung())) {
                    cbCurrentUser.setSelectedItem(u);
                    break;
                }
            }
        }

        cbCurrentUser.addActionListener(e -> {
            NguoiDung selected = (NguoiDung) cbCurrentUser.getSelectedItem();
            if (selected != null) {
                ctrlCreate.doiNguoiDungHienTai(selected.getMaNguoiDung());
                updateInfoBar();
            }
        });

        JButton btnHome = styledBtn("🏠  Trang chủ", new Color(74, 85, 104), Color.WHITE);
        btnHome.addActionListener(e -> phanBoView.showCard("TRANG_CHU"));

        right.add(lblUserText);
        right.add(cbCurrentUser);
        right.add(btnHome);

        bar.add(left,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ====================================================================
    //  CARD CONTAINER
    // ====================================================================
    private JPanel buildCards() {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(C_BG);
        cardPanel.add(buildListCard(),   CARD_LIST);
        cardPanel.add(buildCreateCard(), CARD_CREATE);
        return cardPanel;
    }

    public void showCard(String card) {
        cardLayout.show(cardPanel, card);
        if (CARD_LIST.equals(card)) {
            lblPageTitle.setText("Danh sách Yêu cầu nhập hàng");
        } else {
            lblPageTitle.setText("Tạo yêu cầu nhập hàng mới");
        }
    }

    // ====================================================================
    //  CARD 1 – DANH SÁCH (GD3)
    // ====================================================================
    private JPanel buildListCard() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(C_BG);
        root.setBorder(new EmptyBorder(16, 20, 16, 20));

        // ---- Toolbar ----
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        toolbar.setBackground(C_CARD);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                new EmptyBorder(4, 10, 4, 10)));

        JLabel lblFilter = new JLabel("Trạng thái:");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbFilter = new JComboBox<>(new String[]{"Tất cả", "Vừa tạo", "Đang xử lý", "Hoàn thành", "Huỷ"});
        cbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel lblUserFilter = new JLabel("Nhân viên tạo:");
        lblUserFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        List<NguoiDung> users = ctrlCreate.layDanhSachNguoiDung();
        List<String> userNames = new ArrayList<>();
        userNames.add("Tất cả");
        for (NguoiDung nd : users) {
            userNames.add(nd.getTenNguoiDung());
        }
        cbUserFilter = new JComboBox<>(userNames.toArray(new String[0]));
        cbUserFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Đăng ký sự kiện lọc kết hợp
        cbFilter.addActionListener(e -> applyFilters());
        cbUserFilter.addActionListener(e -> applyFilters());

        lblCount = new JLabel("  Tổng: 0 yêu cầu");
        lblCount.setForeground(C_MUTED);
        lblCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton btnRefresh = styledBtn("🔄  Làm mới", C_MUTED, Color.WHITE);
        btnRefresh.addActionListener(e -> loadListData());

        JButton btnNew = styledBtn("＋  Tạo yêu cầu nhập hàng", C_PRIMARY, Color.WHITE);
        btnNew.addActionListener(e -> {
            resetCreateForm();
            showCard(CARD_CREATE);
        });

        toolbar.add(lblFilter);
        toolbar.add(cbFilter);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(lblUserFilter);
        toolbar.add(cbUserFilter);
        toolbar.add(lblCount);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnRefresh);
        toolbar.add(btnNew);

        // ---- Table ----
        String[] cols = {"ID Yêu cầu", "Tạo bởi", "Ngày tạo", "Tên mặt hàng", "Tổng SL", "Tổng tiền (VND)", "Trạng thái"};
        modelList = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblList = new JTable(modelList);
        styleTable(tblList);

        // Renderer trạng thái
        tblList.getColumnModel().getColumn(6).setCellRenderer(statusRenderer());
        // Căn phải tiền
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        tblList.getColumnModel().getColumn(5).setCellRenderer(right);
        // Căn giữa SL
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tblList.getColumnModel().getColumn(4).setCellRenderer(center);

        int[] widths = {130, 150, 140, 240, 80, 150, 110};
        for (int i = 0; i < widths.length; i++)
            tblList.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        tblList.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tblList.getSelectedRow() >= 0) {
                    showDetail(modelList.getValueAt(tblList.getSelectedRow(), 0).toString());
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tblList);
        scroll.setBorder(BorderFactory.createLineBorder(C_BORDER));

        // ---- Footer gợi ý ----
        JLabel hint = new JLabel("  💡 Double-click vào một dòng để xem chi tiết yêu cầu");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setForeground(C_MUTED);
        hint.setBorder(new EmptyBorder(6, 0, 0, 0));

        root.add(toolbar, BorderLayout.NORTH);
        root.add(scroll,  BorderLayout.CENTER);
        root.add(hint,    BorderLayout.SOUTH);
        return root;
    }

    // ====================================================================
    //  CARD 2 – TẠO MỚI (GD4)
    // ====================================================================
    private JPanel buildCreateCard() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_BG);

        // ---- Info bar ----
        JPanel infoBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        infoBar.setBackground(new Color(232, 244, 253));
        infoBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(190, 227, 248)));
        lblInfo = new JLabel();
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfo.setForeground(new Color(44, 82, 130));
        updateInfoBar();
        infoBar.add(lblInfo);

        // ---- Body (2 cột) ----
        JPanel body = new JPanel(new GridLayout(1, 2, 14, 0));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(14, 18, 10, 18));
        body.add(buildCatalogPanel());
        body.add(buildCartPanel());

        // ---- Bottom action bar ----
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setBackground(C_CARD);
        actionBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER),
                new EmptyBorder(10, 20, 10, 20)));

        JButton btnBack = styledBtn("◀  Quay lại danh sách", C_MUTED, Color.WHITE);
        btnBack.addActionListener(e -> showCard(CARD_LIST));

        JButton btnSubmit = styledBtn("🚀  Tạo & Phân bổ ngay", C_PRIMARY, Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setPreferredSize(new Dimension(220, 40));
        btnSubmit.addActionListener(e -> handleSubmit());

        JPanel btnRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRight.setOpaque(false);
        btnRight.add(btnBack);
        btnRight.add(btnSubmit);

        actionBar.add(btnRight, BorderLayout.EAST);

        root.add(infoBar,   BorderLayout.NORTH);
        root.add(body,      BorderLayout.CENTER);
        root.add(actionBar, BorderLayout.SOUTH);
        return root;
    }

    private void updateInfoBar() {
        NguoiDung nd = ctrlCreate.layNguoiDungHienTai();
        String tenND = nd != null ? nd.getTenNguoiDung() + " (" + nd.getChucVu() + ")" : "N/A";
        lblInfo.setText("👤  Người tạo: " + tenND + "      📅  Ngày: " + sdf.format(new Date()));
    }

    // ---- Panel trái: Danh mục sản phẩm ----
    private JPanel buildCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(C_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel title = sectionTitle("📦  Danh mục sản phẩm");

        // Search bar
        JPanel searchRow = new JPanel(new BorderLayout(6, 0));
        searchRow.setOpaque(false);
        tfSearch = new JTextField();
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224)),
                new EmptyBorder(6, 10, 6, 10)));
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { loadCatalog(); }
            public void insertUpdate(DocumentEvent e)  { loadCatalog(); }
            public void removeUpdate(DocumentEvent e)  { loadCatalog(); }
        });
        JButton btnClear = styledBtn("✖", C_MUTED, Color.WHITE);
        btnClear.addActionListener(e -> tfSearch.setText(""));
        searchRow.add(tfSearch, BorderLayout.CENTER);
        searchRow.add(btnClear, BorderLayout.EAST);

        // Table
        modelCatalog = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm", "Đơn giá", "Thêm"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblCatalog = new JTable(modelCatalog);
        styleTable(tblCatalog);
        tblCatalog.getColumnModel().getColumn(3).setMaxWidth(85);
        tblCatalog.getColumnModel().getColumn(3).setCellRenderer(new BtnCellRenderer("＋ Chọn", C_ACCENT));

        tblCatalog.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tblCatalog.rowAtPoint(e.getPoint());
                int col = tblCatalog.columnAtPoint(e.getPoint());
                if (col == 3 && row >= 0 && row < catalogItems.size()) {
                    addToCart(catalogItems.get(row));
                }
            }
        });

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(searchRow, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblCatalog), BorderLayout.CENTER);
        loadCatalog();
        return panel;
    }

    // ---- Panel phải: Giỏ hàng + Ghi chú + Tổng tiền ----
    private JPanel buildCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(C_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel title = sectionTitle("🛒  Sản phẩm đã chọn");

        modelCart = new DefaultTableModel(new String[]{"Mã SP", "Tên sản phẩm", "Số lượng", "Xóa"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
        };
        tblCart = new JTable(modelCart);
        styleTable(tblCart);
        tblCart.getColumnModel().getColumn(2).setMaxWidth(90);
        tblCart.getColumnModel().getColumn(2).setMinWidth(70);
        tblCart.getColumnModel().getColumn(3).setMaxWidth(75);
        tblCart.getColumnModel().getColumn(3).setCellRenderer(new BtnCellRenderer("🗑 Xóa", C_DANGER));

        // Lắng nghe sự kiện update của bảng giỏ hàng để cập nhật Tổng tiền real-time
        modelCart.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                updateSummary();
            }
        });

        // Validate số lượng khi edit trực tiếp trên cell
        tblCart.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override public boolean stopCellEditing() {
                String val = (String) getCellEditorValue();
                try {
                    int n = Integer.parseInt(val.trim());
                    if (n <= 0) { showWarn("Số lượng phải lớn hơn 0."); return false; }
                } catch (NumberFormatException ex) {
                    showWarn("Vui lòng nhập số nguyên hợp lệ."); return false;
                }
                return super.stopCellEditing();
            }
        });

        tblCart.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = tblCart.rowAtPoint(e.getPoint());
                int col = tblCart.columnAtPoint(e.getPoint());
                if (col == 3 && row >= 0 && row < modelCart.getRowCount()) {
                    if (tblCart.isEditing()) tblCart.getCellEditor().cancelCellEditing();
                    modelCart.removeRow(row);
                    updateSummary(); // Cập nhật tổng tiền khi xóa dòng
                }
            }
        });

        // Khu vực thông tin tổng hợp dưới bảng giỏ hàng (không dùng popup nữa)
        JPanel summaryBox = new JPanel(new BorderLayout(0, 8));
        summaryBox.setOpaque(false);
        summaryBox.setBorder(new EmptyBorder(8, 0, 0, 0));

        lblTotalPrice = new JLabel("Tổng tiền dự kiến: 0 VND");
        lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalPrice.setForeground(C_PRIMARY);

        JPanel gcBox = new JPanel(new BorderLayout(0, 4));
        gcBox.setOpaque(false);
        JLabel lblGC = new JLabel("Ghi chú (không bắt buộc):");
        lblGC.setFont(new Font("Segoe UI", Font.BOLD, 12));
        taGhiChu = new JTextArea(2, 20);
        taGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taGhiChu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 224)),
                new EmptyBorder(4, 6, 4, 6)));
        JScrollPane spGC = new JScrollPane(taGhiChu);
        gcBox.add(lblGC, BorderLayout.NORTH);
        gcBox.add(spGC, BorderLayout.CENTER);

        summaryBox.add(lblTotalPrice, BorderLayout.NORTH);
        summaryBox.add(gcBox, BorderLayout.CENTER);

        JLabel hint = new JLabel("💡 Nhập số lượng và nhấn Enter. Mọi thao tác đều thực hiện trên 1 màn hình này.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(C_MUTED);
        summaryBox.add(hint, BorderLayout.SOUTH);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblCart), BorderLayout.CENTER);
        panel.add(summaryBox, BorderLayout.SOUTH);
        return panel;
    }

    // ====================================================================
    //  LOGIC – GD3 Danh sách
    // ====================================================================
    public void loadListData() {
        applyFilters();
    }

    private void applyFilters() {
        String trangThai = cbFilter != null ? (String) cbFilter.getSelectedItem() : "Tất cả";
        String nguoiTao = cbUserFilter != null ? (String) cbUserFilter.getSelectedItem() : "Tất cả";

        List<DonHangNhap> list = ctrlList.loadDanhSachYeuCau();
        List<DonHangNhap> filtered = new ArrayList<>();

        for (DonHangNhap d : list) {
            boolean matchStatus = "Tất cả".equals(trangThai) || d.getTrangThai().equals(trangThai);
            boolean matchUser = "Tất cả".equals(nguoiTao) || d.getTaoBoi().equals(nguoiTao);
            if (matchStatus && matchUser) {
                filtered.add(d);
            }
        }
        refreshListTable(filtered);
    }

    private void refreshListTable(List<DonHangNhap> list) {
        modelList.setRowCount(0);
        for (DonHangNhap d : list) {
            modelList.addRow(new Object[]{
                    d.getId(), d.getTaoBoi(),
                    d.getNgayTao() != null ? sdf.format(d.getNgayTao()) : "",
                    d.getTomTatMatHang(),
                    d.getTongSoLuong(),
                    nf.format(d.getTongTien()),
                    d.getTrangThai()
            });
        }
        lblCount.setText("  Tổng: " + list.size() + " yêu cầu");
    }

    private void showDetail(String id) {
        DonHangNhap d = ctrlList.loadDanhSachYeuCau().stream()
                .filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        if (d == null) return;

        JDialog dlg = new JDialog(phanBoView, "Chi tiết: " + d.getId(), true);
        dlg.setSize(600, 480);
        dlg.setLocationRelativeTo(phanBoView);

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBorder(new EmptyBorder(20, 22, 16, 22));
        body.setBackground(Color.WHITE);

        JLabel t = new JLabel("📋  Chi tiết Yêu cầu nhập hàng");
        t.setFont(new Font("Segoe UI", Font.BOLD, 16));
        body.add(t, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridLayout(0, 2, 10, 8));
        info.setOpaque(false);
        addInfo(info, "ID:", d.getId());
        addInfo(info, "Tạo bởi:", d.getTaoBoi());
        addInfo(info, "Ngày tạo:", d.getNgayTao() != null ? sdf.format(d.getNgayTao()) : "");
        addInfo(info, "Trạng thái:", d.getTrangThai());
        addInfo(info, "Tổng tiền:", nf.format(d.getTongTien()) + " VND");
        if (d.getGhiChu() != null && !d.getGhiChu().isBlank())
            addInfo(info, "Ghi chú:", d.getGhiChu());

        String[] cols = {"Mã SP", "Tên sản phẩm", "Số lượng", "Đơn giá"};
        DefaultTableModel dm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Map.Entry<SanPham, Integer> e : d.getChiTiet().entrySet())
            dm.addRow(new Object[]{e.getKey().getMaSanPham(), e.getKey().getTenSanPham(),
                    e.getValue(), nf.format(e.getKey().getGiaNhap()) + " đ"});
        JTable tbl = new JTable(dm);
        styleTable(tbl);
        JScrollPane sp = new JScrollPane(tbl);
        sp.setPreferredSize(new Dimension(540, 160));

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);
        center.add(info, BorderLayout.NORTH);
        center.add(new JLabel("Danh sách sản phẩm:") {{ setFont(new Font("Segoe UI", Font.BOLD, 13)); }}, BorderLayout.CENTER);
        center.add(sp, BorderLayout.SOUTH);
        body.add(center, BorderLayout.CENTER);

        JButton btnClose = styledBtn("Đóng", C_MUTED, Color.WHITE);
        btnClose.addActionListener(e -> dlg.dispose());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(btnClose);
        body.add(btnRow, BorderLayout.SOUTH);

        dlg.add(body);
        dlg.setVisible(true);
    }

    // ====================================================================
    //  LOGIC – GD4 Tạo mới
    // ====================================================================
    private void resetCreateForm() {
        tfSearch.setText("");
        modelCart.setRowCount(0);
        taGhiChu.setText("");
        lblTotalPrice.setText("Tổng tiền dự kiến: 0 VND");
        loadCatalog();
        updateInfoBar();
    }

    private void loadCatalog() {
        String kw = tfSearch != null ? tfSearch.getText().trim() : "";
        catalogItems = ctrlCreate.timKiemSanPham(kw);
        modelCatalog.setRowCount(0);
        for (SanPham sp : catalogItems) {
            modelCatalog.addRow(new Object[]{
                    sp.getMaSanPham(), sp.getTenSanPham(),
                    nf.format(sp.getGiaNhap()) + "đ", "＋ Chọn"
            });
        }
    }

    private void addToCart(SanPham sp) {
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            if (modelCart.getValueAt(i, 0).equals(sp.getMaSanPham())) {
                showWarn(sp.getTenSanPham() + " đã có trong danh sách.\nHãy chỉnh số lượng trực tiếp.");
                return;
            }
        }
        modelCart.addRow(new Object[]{sp.getMaSanPham(), sp.getTenSanPham(), 1, "🗑 Xóa"});
        updateSummary(); // Cập nhật tổng tiền khi thêm mới
    }

    private void updateSummary() {
        if (modelCart == null) return;
        double tong = 0;
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String ma = modelCart.getValueAt(i, 0).toString();
            int sl;
            try { sl = Integer.parseInt(modelCart.getValueAt(i, 2).toString()); }
            catch (Exception ex) { sl = 0; }
            SanPham sp = ctrlCreate.layDanhSachSanPham().stream()
                    .filter(s -> s.getMaSanPham().equals(ma)).findFirst().orElse(null);
            if (sp != null) {
                tong += sp.getGiaNhap() * sl;
            }
        }
        lblTotalPrice.setText("Tổng tiền dự kiến: " + nf.format(tong) + " VND");
    }

    private Map<SanPham, Integer> buildCartMap() {
        if (tblCart.isEditing()) tblCart.getCellEditor().stopCellEditing();
        Map<SanPham, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < modelCart.getRowCount(); i++) {
            String ma = modelCart.getValueAt(i, 0).toString();
            int sl;
            try { sl = Integer.parseInt(modelCart.getValueAt(i, 2).toString()); }
            catch (Exception ex) { sl = 0; }
            SanPham sp = ctrlCreate.layDanhSachSanPham().stream()
                    .filter(s -> s.getMaSanPham().equals(ma)).findFirst().orElse(null);
            if (sp != null) map.put(sp, sl);
        }
        return map;
    }

    private void handleSubmit() {
        Map<SanPham, Integer> chiTiet = buildCartMap();

        // 1. Validate dữ liệu đầu vào
        String loi = ctrlCreate.validateThongTinDauVao(chiTiet);
        if (loi != null) {
            showWarn(loi);
            return;
        }

        // 2. Tạo trực tiếp không cần hiện popup trung gian
        String gc = taGhiChu.getText().trim();
        DonHangNhap donHang = ctrlCreate.submitTaoYeuCau(chiTiet, gc);

        if (donHang != null) {
            // Chuyển thẳng sang phân bổ tự động cho yêu cầu vừa tạo
            String createdId = donHang.getId();
            resetCreateForm();
            
            // Log thành công và chuyển card
            phanBoView.hienThiThongBao("✅  Tạo yêu cầu thành công!\n"
                    + "Đang chuyển tự động sang giao diện phân bổ...");
            phanBoView.chuyenDenPhanBoYeuCau(createdId);
        } else {
            showWarn("Có lỗi xảy ra khi tạo yêu cầu. Vui lòng kiểm tra lại.");
        }
    }

    // ====================================================================
    //  HELPERS
    // ====================================================================
    static JButton styledBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private static JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(new Color(26, 26, 46));
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        return lbl;
    }

    private static void styleTable(JTable t) {
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(34);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(235, 244, 255));
        t.setSelectionForeground(new Color(33, 37, 41));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.getTableHeader().setBackground(new Color(248, 250, 252));
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(226, 232, 240)));
        // Xen màu dòng
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(tbl, val, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
                return comp;
            }
        });
    }

    private static TableCellRenderer statusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int r, int c) {
                JLabel lbl = new JLabel(val == null ? "" : val.toString(), SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String v = val == null ? "" : val.toString();
                switch (v) {
                    case "Vừa tạo":
                        lbl.setBackground(new Color(227,242,253)); lbl.setForeground(new Color(21,101,192)); break;
                    case "Hoàn thành":
                        lbl.setBackground(new Color(232,245,233)); lbl.setForeground(new Color(46,125,50)); break;
                    default:
                        lbl.setBackground(new Color(255,243,224)); lbl.setForeground(new Color(230,81,0));
                }
                return lbl;
            }
        };
    }

    private void addInfo(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(C_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lbl);
        panel.add(val);
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(phanBoView, msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    // ====================================================================
    //  INNER: Button Cell Renderer
    // ====================================================================
    static class BtnCellRenderer extends DefaultTableCellRenderer {
        private final Color bg;
        BtnCellRenderer(String text, Color bg) {
            this.bg = bg;
            setText(text);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(true);
        }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object val,
                boolean sel, boolean foc, int r, int c) {
            JButton btn = new JButton(getText());
            btn.setBackground(bg);
            btn.setForeground(Color.WHITE);
            btn.setFont(getFont());
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            return btn;
        }
    }
}
