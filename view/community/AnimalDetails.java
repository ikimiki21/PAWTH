package com.pawth.view.community;

import com.pawth.model.Animal;
import com.pawth.view.components.*;
import javax.swing.*;

public class AnimalDetails extends JPanel {
    public static void show(WorkspaceFrame frame, Animal animal) {
        frame.showDetails(animal);
    }

    public AnimalDetails(WorkspaceFrame frame, Animal animal) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ui.add(this, Ui.secondary("← Back to all animals", () -> frame.navigate("Adopt")));
        Ui.gap(this, 32);
        JPanel details = Ui.column();
        Ui.add(
                details,
                Ui.label(
                        animal.getSpecies().toUpperCase() + " · #" + animal.getAnimalId(),
                        11,
                        Ui.CORAL));
        Ui.gap(details, 16);
        Ui.add(details, Ui.label(animal.getName(), 46, Ui.INK));
        Ui.gap(details, 14);
        Ui.add(
                details,
                Ui.paragraph(
                        animal.getBreed()
                                + " · "
                                + animal.getAgeYears()
                                + " years · "
                                + animal.getGender(),
                        340));
        Ui.gap(details, 28);
        Ui.add(details, Ui.badge(animal.getAdoptionStatus().name()));
        Ui.gap(details, 26);
        Ui.add(details, Ui.paragraph(animal.getPersonalityTraits(), 340));
        Ui.gap(details, 26);
        Ui.Card health = Ui.card("Health", "Shelter record");
        Ui.add(health, Ui.paragraph(animal.getHealthStatus(), 300));
        Ui.add(details, health);
        Ui.gap(details, 22);
        Ui.Card visit = Ui.card("Meet " + animal.getName(), "Shelter consultation schedule");
        Ui.add(visit, Ui.paragraph(animal.getVisitSchedule(), 300));
        Ui.add(details, visit);
        Ui.gap(details, 26);
        if (!frame.staff) {
            Ui.add(details, Ui.badge(animal.getMatchPercentage() + "% match"));
            Ui.gap(details, 22);
            JButton apply =
                    Ui.button(
                            "Apply to adopt " + animal.getName(),
                            () -> new ApplicationModalDialog(frame, animal).setVisible(true));
            apply.setEnabled(animal.getAdoptionStatus() == Animal.AdoptionStatus.AVAILABLE);
            Ui.stretch(apply, 46);
            Ui.add(details, apply);
        }
        Ui.add(this, Ui.grid(2, Ui.photo(animal.getPhotoUrl(), 380, 640), details));
    }
}
