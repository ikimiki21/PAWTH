package com.pawth.view.components;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text) {
        super(text);
        setFont(Ui.font(12).deriveFont(Font.BOLD));
        setBackground(Ui.CORAL);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        putClientProperty("FlatLaf.style", "arc: 26; margin: 12,16,12,16");
    }
}
