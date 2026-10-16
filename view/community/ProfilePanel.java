package com.pawth.view.community;

import com.pawth.model.User;
import javax.swing.*;

public class ProfilePanel extends JPanel {
    public ProfilePanel(User user) {
        add(new JLabel("Personal Profile: " + user.getFullName() + " | " + user.getBarangay()));
    }
}