package com.pawth.view.community;

import javax.swing.*;

public class ApplicationModalDialog extends JDialog {
    public ApplicationModalDialog(JFrame parent, String animalName) {
        super(parent, "Apply for " + animalName, true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        add(new JLabel("Adoption Compatibility Questionnaire"));
    }
}