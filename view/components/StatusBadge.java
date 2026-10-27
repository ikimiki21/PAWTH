package com.pawth.view.components;

import com.pawth.config.SystemConstants;

import javax.swing.*;
import java.awt.*;

public class StatusBadge extends JLabel {

    public StatusBadge(String text, Color bgColor) {
        super(" " + text.toUpperCase() + " ", SwingConstants.CENTER);
        setOpaque(true);
        setBackground(bgColor);
        setForeground(Color.WHITE);
        setFont(SystemConstants.FONT_SMALL.deriveFont(Font.BOLD));
        setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
    }
}