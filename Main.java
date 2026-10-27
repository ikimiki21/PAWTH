package com.pawth;

import com.pawth.view.components.Ui;
import com.pawth.view.auth.LoginFrame;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> {
                    Ui.setup();
                    new LoginFrame().setVisible(true);
                });
    }
}
