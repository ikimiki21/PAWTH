package com.pawth.view.community;

import com.pawth.config.SystemConstants;
import com.pawth.model.User;
import java.awt.*;
import javax.swing.*;

public class HomePanel extends JPanel {

    public HomePanel(User user) {
        setBackground(SystemConstants.COLOR_BG_LIGHT);
        setLayout(new BorderLayout(20, 20));

        JLabel welcomeLabel = new JLabel("Good morning, " + user.getFullName() + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(SystemConstants.FONT_TITLE);

        add(welcomeLabel, BorderLayout.NORTH);
        add(new JLabel("Welcome to your local rescue community dashboard."), BorderLayout.CENTER);
    }
}