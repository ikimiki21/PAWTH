package com.pawth.view.staff;

import com.pawth.config.SystemConstants;
import com.pawth.model.User;

import javax.swing.*;
import java.awt.*;

public class StaffDashboardFrame extends JFrame {

    private User staffUser;

    public StaffDashboardFrame(User user) {
        this.staffUser = user;

        setTitle("PAWTH - Shelter Operations Workspace");
        setSize(1250, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        header.setBackground(Color.WHITE);
        JLabel title = new JLabel("PAWTH SHELTER OPERATIONS | Welcome, " + staffUser.getFullName());
        title.setFont(SystemConstants.FONT_HEADER);
        header.add(title);

        add(header, BorderLayout.NORTH);

        // Center Content Workspace
        JPanel mainContent = new JPanel();
        mainContent.setBackground(SystemConstants.COLOR_BG_LIGHT);
        mainContent.add(new JLabel("Active Case Queue & Operations Center Ready."));

        add(mainContent, BorderLayout.CENTER);
    }
}