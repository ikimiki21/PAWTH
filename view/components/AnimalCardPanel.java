package com.pawth.view.components;

import com.pawth.config.SystemConstants;
import com.pawth.model.Animal;
import java.awt.*;
import javax.swing.*;

public class AnimalCardPanel extends JPanel {

    public AnimalCardPanel(Animal animal, Runnable onApplyClicked) {
        setLayout(new BorderLayout(10, 10));
        setBackground(SystemConstants.COLOR_CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SystemConstants.COLOR_BORDER, 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // Details Body
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(animal.getName());
        nameLabel.setFont(SystemConstants.FONT_HEADER);

        JLabel breedLabel = new JLabel(animal.getBreed() + " · " + animal.getAgeYears() + " yrs");
        breedLabel.setFont(SystemConstants.FONT_SMALL);
        breedLabel.setForeground(SystemConstants.COLOR_TEXT_MUTED);

        JLabel matchLabel = new JLabel(animal.getMatchPercentage() + "% Match");
        matchLabel.setFont(SystemConstants.FONT_SMALL.deriveFont(Font.BOLD));
        matchLabel.setForeground(SystemConstants.BADGE_SUCCESS);

        infoPanel.add(nameLabel);
        infoPanel.add(breedLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(matchLabel);

        CustomButton applyBtn = new CustomButton("Apply");
        applyBtn.addActionListener(e -> onApplyClicked.run());

        add(infoPanel, BorderLayout.CENTER);
        add(applyBtn, BorderLayout.SOUTH);
    }
}