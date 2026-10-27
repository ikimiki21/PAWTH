package com.pawth.view.staff;

import com.pawth.dao.AnimalDAO;
import com.pawth.model.Animal;
import com.pawth.view.components.*;
import com.pawth.view.community.HomePanel;
import javax.swing.*;
import java.awt.*;

public class AnimalRecordsPanel extends JPanel {
    public AnimalRecordsPanel(WorkspaceFrame frame) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ui.add(
                this,
                Ui.heading(
                        "Shelter inventory",
                        "Animal listings",
                        "View animal profiles and control their adoption availability."));
        Ui.add(
                this,
                Ui.grid(
                        4,
                        HomePanel.metric(
                                "Total records", frame.data.animals().size(), "Shelter inventory"),
                        metric(frame, "Available", Animal.AdoptionStatus.AVAILABLE),
                        metric(frame, "On hold", Animal.AdoptionStatus.ON_HOLD),
                        metric(frame, "Adopted", Animal.AdoptionStatus.ADOPTED)));
        Ui.gap(this, 24);
        Ui.Card list =
                Ui.card("Adoption profiles", "Changes update the existing shelter inventory.");
        for (Animal animal : frame.data.animals()) {
            JPanel row = new JPanel(new BorderLayout(16, 0));
            row.setOpaque(false);
            row.add(Ui.photo(animal.getPhotoUrl(), 55, 70), BorderLayout.WEST);
            JPanel description = Ui.column();
            Ui.add(description, Ui.bold(animal.getName() + " · #" + animal.getAnimalId(), 12));
            Ui.gap(description, 8);
            Ui.add(
                    description,
                    Ui.label(
                            animal.getSpecies()
                                    + " · "
                                    + animal.getBreed()
                                    + " · "
                                    + animal.getAgeYears()
                                    + " years",
                            10,
                            Ui.MUTED));
            Ui.gap(description, 8);
            Ui.add(
                    description,
                    Ui.paragraph(
                            animal.getHealthStatus() + " · " + animal.getVisitSchedule(), 350));
            row.add(description);
            JComboBox<Animal.AdoptionStatus> status =
                    new JComboBox<>(Animal.AdoptionStatus.values());
            status.setSelectedItem(animal.getAdoptionStatus());
            JButton save = Ui.secondary("Save", () -> {});
            save.addActionListener(
                    e -> {
                        Animal.AdoptionStatus value =
                                (Animal.AdoptionStatus) status.getSelectedItem();
                        status.setEnabled(false);
                        Ui.save(
                                save,
                                () -> {
                                    try {
                                        return new AnimalDAO()
                                                .updateStatus(animal.getAnimalId(), value);
                                    } finally {
                                        SwingUtilities.invokeLater(() -> status.setEnabled(true));
                                    }
                                },
                                frame::reload);
                    });
            row.add(Ui.row(status, save), BorderLayout.EAST);
            Ui.add(list, row);
            Ui.gap(list, 24);
        }
        if (frame.data.animals().isEmpty())
            Ui.add(list, Ui.label("No animal records yet.", 12, Ui.MUTED));
        Ui.add(this, list);
    }

    private Ui.Card metric(WorkspaceFrame frame, String title, Animal.AdoptionStatus status) {
        return HomePanel.metric(
                title,
                frame.data.animals().stream().filter(a -> a.getAdoptionStatus() == status).count(),
                "Current status");
    }
}
