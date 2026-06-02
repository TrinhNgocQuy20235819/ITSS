import view.PhanBoView;

import javax.swing.*;

/**
 * Lớp Main - Điểm khởi chạy ứng dụng.
 * Chạy giao diện Swing trên Event Dispatch Thread (EDT).
 */
public class Main {
    public static void main(String[] args) {
        // Sử dụng Look and Feel hệ thống cho giao diện đẹp hơn
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Khởi chạy trên EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            PhanBoView view = new PhanBoView();
            view.setVisible(true);
        });
    }
}
