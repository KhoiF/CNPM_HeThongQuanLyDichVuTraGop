import view.LoginFrm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Không thể thiết lập Look and Feel.");
        }

        SwingUtilities.invokeLater(() -> new LoginFrm().setVisible(true));
    }
}
