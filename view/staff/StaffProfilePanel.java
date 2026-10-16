package com.pawth.view.staff;

import com.pawth.model.User;
import javax.swing.*;

public class StaffProfilePanel extends JPanel {
    public StaffProfilePanel(User user) {
        add(new JLabel("Staff Profile: " + user.getFullName() + " | ID: PTH-2048"));
    }
}