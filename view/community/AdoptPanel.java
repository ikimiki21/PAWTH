package com.pawth.view.community;

import com.pawth.view.components.*;
import com.pawth.model.Animal;
import java.awt.*;
import javax.swing.*;

public class AdoptPanel extends JPanel {
    public AdoptPanel(WorkspaceFrame frame) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ui.add(
                this,
                Ui.heading(
                        "Find your companion",
                        "Animals ready to meet you",
                        "Every profile is verified and updated by our shelter team."));
        JTextField search = Ui.text("Search by name or breed");
        JComboBox<String> species = Ui.select("All species", "Dog", "Cat"),
                status =
                        Ui.select(
                                "All statuses", "AVAILABLE", "ON_HOLD", "MEDICAL_CARE", "ADOPTED");
        JLabel count = Ui.label("", 10, Ui.MUTED);
        Ui.Card filters = new Ui.Card();
        filters.setLayout(new BorderLayout(14, 0));
        filters.add(search);
        filters.add(Ui.row(species, status, count), BorderLayout.EAST);
        Ui.stretch(filters, 82);
        Ui.add(this, filters);
        Ui.gap(this, 22);
        JPanel animals = Ui.grid(3);
        Ui.add(this, animals);
        Runnable filter =
                () -> {
                    animals.removeAll();
                    String query = search.getText().trim().toLowerCase();
                    int matches = 0;
                    for (Animal a : frame.data.animals())
                        if ((a.getName() + " " + a.getBreed()).toLowerCase().contains(query)
                                && (species.getSelectedIndex() == 0
                                        || a.getSpecies()
                                                .equalsIgnoreCase(
                                                        (String) species.getSelectedItem()))
                                && (status.getSelectedIndex() == 0
                                        || a.getAdoptionStatus()
                                                .name()
                                                .equals(status.getSelectedItem()))) {
                            animals.add(new AnimalCardPanel(a, () -> AnimalDetails.show(frame, a)));
                            matches++;
                        }
                    // Preserve the three-column card width when only Buddy is displayed.
                    if (matches > 0)
                        while (animals.getComponentCount() < 3) {
                            JPanel spacer = new JPanel();
                            spacer.setOpaque(false);
                            animals.add(spacer);
                        }
                    count.setText(matches == 1 ? "1 match" : matches + " matches");
                    if (matches == 0)
                        animals.add(Ui.label("No animals match your filters.", 12, Ui.MUTED));
                    animals.revalidate();
                    animals.repaint();
                };
        Ui.onChange(search, filter);
        species.addActionListener(e -> filter.run());
        status.addActionListener(e -> filter.run());
        filter.run();
    }
}
