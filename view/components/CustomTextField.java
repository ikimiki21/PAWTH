package com.pawth.view.components;

import javax.swing.*;

public class CustomTextField extends JTextField {
    public CustomTextField(int columns) {
        super(columns);
        setFont(Ui.font(12));
        putClientProperty("FlatLaf.style", "arc: 22; margin: 12,13,12,13");
    }
}
