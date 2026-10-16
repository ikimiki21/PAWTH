package com.pawth.view.community;

import com.pawth.dao.RescueCaseDAO;
import com.pawth.model.RescueCase;
import com.pawth.model.User;
import com.pawth.service.PriorityScoringEngine;
import com.pawth.view.components.CustomButton;
import com.pawth.view.components.CustomTextField;
import javax.swing.*;

public class ReportStrayPanel extends JPanel {

    public ReportStrayPanel(User user) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        CustomTextField locationField = new CustomTextField(20);
        CustomTextField barangayField = new CustomTextField(20);
        JCheckBox injuredBox = new JCheckBox("Injured");
        JCheckBox roadBox = new JCheckBox("Near busy road");
        CustomButton submitBtn = new CustomButton("Submit Rescue Report");

        add(new JLabel("Report a Stray Animal"));
        add(locationField);
        add(barangayField);
        add(injuredBox);
        add(roadBox);
        add(submitBtn);

        submitBtn.addActionListener(e -> {
            RescueCase rc = new RescueCase();
            rc.setReporterId(user.getUserId());
            rc.setAnimalType("Dog");
            rc.setLocationLandmark(locationField.getText());
            rc.setBarangay(barangayField.getText());

            StringBuilder flags = new StringBuilder();
            if (injuredBox.isSelected()) flags.append("Injured, ");
            if (roadBox.isSelected()) flags.append("Near busy road, ");
            rc.setConditionFlags(flags.toString());

            PriorityScoringEngine.calculateAndSetPriority(rc);
            new RescueCaseDAO().createReport(rc);
            JOptionPane.showMessageDialog(this, "Report submitted successfully!");
        });
    }
}