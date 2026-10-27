package com.pawth.view.auth;

import com.pawth.config.SystemConstants;
import com.pawth.view.components.CustomButton;
import com.pawth.view.components.CustomTextField;
import java.awt.*;
import javax.swing.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle(SystemConstants.APP_NAME + " - Create Account");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        add(new JLabel("Create your PAWTH Account"));
        add(new CustomTextField(20));
        add(new CustomButton("Register"));
    }
}