package com.pawth.view.components;

import com.pawth.model.Animal;
import java.awt.*;
import javax.swing.*;

public class AnimalCardPanel extends Ui.Card {
    public AnimalCardPanel(Animal animal, Runnable onOpen) {
        setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        Ui.add(this, Ui.photo(animal.getPhotoUrl(), 273, 290));
        Ui.gap(this, 14);
        JPanel details = Ui.column();
        details.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        Ui.add(
                details,
                Ui.row(
                        Ui.label(animal.getName(), 19, Ui.INK),
                        Ui.badge(animal.getMatchPercentage() + "% match")));
        Ui.gap(details, 10);
        Ui.add(
                details,
                Ui.label(
                        animal.getBreed() + " · " + animal.getAgeYears() + " years", 10, Ui.MUTED));
        Ui.gap(details, 12);
        Ui.add(details, Ui.badge(animal.getAdoptionStatus().name()));
        Ui.gap(details, 12);
        Ui.add(details, Ui.paragraph(animal.getPersonalityTraits(), 190));
        Ui.gap(details, 16);
        Ui.add(details, Ui.secondary("View profile", onOpen));
        Ui.add(this, details);
    }
}
