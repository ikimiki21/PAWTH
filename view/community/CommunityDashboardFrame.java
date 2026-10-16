package com.pawth.view.community;

import com.pawth.config.SystemConstants;
import com.pawth.model.User;

import javax.swing.*;
import java.awt.*;

public class CommunityDashboardFrame extends JFrame {

    private User currentUser;
    private JPanel contentArea;

    public CommunityDashboardFrame(User user) {
        this.currentUser = user;

        setTitle("PAWTH - Community Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Left Sidebar Navigation
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Center Dynamic Content View
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(SystemConstants.COLOR_BG_LIGHT);

        // Add child panels
        contentArea.add(new HomePanel(currentUser), "HOME");
        
        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 750));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, SystemConstants.COLOR_BORDER));

        JLabel logo = new JLabel("  PAWTH");
        logo.setFont(SystemConstants.FONT_TITLE);
        logo.setForeground(SystemConstants.COLOR_PRIMARY);

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(30));

        // Navigation Buttons
        JButton homeBtn = new JButton("Home");
        JButton adoptBtn = new JButton("Adopt");
        JButton reportBtn = new JButton("Report Stray");
        
        sidebar.add(homeBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(adoptBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(reportBtn);

        return sidebar;
    }
}