package com.pawth.view.components;

import com.pawth.config.SystemConstants;
import javax.swing.*;

public class CustomTextField extends JTextField {

    public CustomTextField(int columns) {
        super(columns);
        setFont(SystemConstants.FONT_BODY);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
}