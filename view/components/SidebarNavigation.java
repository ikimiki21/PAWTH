package com.pawth.view.components;

import com.pawth.config.SystemConstants;
import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

public class SidebarNavigation extends JPanel {

    public SidebarNavigation(String[] items, Consumer<String> onNavigation) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(200, 700));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SystemConstants.COLOR_BORDER));

        JLabel logo = new JLabel(" PAWTH");
        logo.setFont(SystemConstants.FONT_TITLE);
        logo.setForeground(SystemConstants.COLOR_PRIMARY);

        add(Box.createVerticalStrut(20));
        add(logo);
        add(Box.createVerticalStrut(30));

        for (String item : items) {
            JButton btn = new JButton(item);
            btn.setFont(SystemConstants.FONT_BODY);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.addActionListener(e -> onNavigation.accept(item));

            add(btn);
            add(Box.createVerticalStrut(12));
        }
    }
}