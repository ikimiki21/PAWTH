package com.pawth.view.auth;

import com.pawth.config.SystemConstants;
import com.pawth.model.User;
import com.pawth.service.AuthService;
import com.pawth.view.community.CommunityDashboardFrame;
import com.pawth.view.components.CustomButton;
import com.pawth.view.components.CustomTextField;
import com.pawth.view.staff.StaffDashboardFrame;
import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private CustomTextField emailField;
    private JPasswordField passwordField;
    private JRadioButton communityToggle, staffToggle;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService();

        setTitle(SystemConstants.APP_NAME + " - Sign In");
        setSize(420, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        communityToggle = new JRadioButton("Community", true);
        staffToggle = new JRadioButton("Staff");
        ButtonGroup bg = new ButtonGroup();
        bg.add(communityToggle);
        bg.add(staffToggle);

        JPanel togglePanel = new JPanel();
        togglePanel.add(communityToggle);
        togglePanel.add(staffToggle);

        emailField = new CustomTextField(20);
        passwordField = new JPasswordField(20);
        CustomButton loginBtn = new CustomButton("Open Workspace");

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Choose Workspace:"), gbc);
        gbc.gridy = 1; add(togglePanel, gbc);
        gbc.gridy = 2; add(new JLabel("Email Address:"), gbc);
        gbc.gridy = 3; add(emailField, gbc);
        gbc.gridy = 4; add(new JLabel("Password:"), gbc);
        gbc.gridy = 5; add(passwordField, gbc);
        gbc.gridy = 6; add(loginBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String role = communityToggle.isSelected() ? "COMMUNITY" : "STAFF";
        User user = authService.login(emailField.getText(), new String(passwordField.getPassword()), role);

        if (user != null) {
            dispose();
            if (user.getRole() == User.Role.STAFF) {
                new StaffDashboardFrame(user).setVisible(true);
            } else {
                new CommunityDashboardFrame(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials or unauthorized role workspace selection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}