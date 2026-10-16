package com.pawth.view.components;

import com.pawth.config.SystemConstants;
import java.awt.*;
import javax.swing.*;

public class CustomButton extends JButton {

    public CustomButton(String text) {
        super(text);
        setFont(SystemConstants.FONT_BODY.deriveFont(Font.BOLD));
        setBackground(SystemConstants.COLOR_PRIMARY);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}