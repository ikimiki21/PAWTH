package com.pawth.view.community;

import com.pawth.dao.AnimalDAO;
import com.pawth.model.Animal;
import com.pawth.view.components.AnimalCardPanel;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class AdoptPanel extends JPanel {

    public AdoptPanel() {
        setLayout(new GridLayout(0, 3, 15, 15));
        AnimalDAO animalDAO = new AnimalDAO();
        List<Animal> animals = animalDAO.getAllAnimals();

        for (Animal animal : animals) {
            add(new AnimalCardPanel(animal, () -> {
                JOptionPane.showMessageDialog(this, "Opening adoption application for " + animal.getName());
            }));
        }
    }
}