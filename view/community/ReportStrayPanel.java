package com.pawth.view.community;

import com.pawth.dao.RescueCaseDAO;
import com.pawth.model.*;
import com.pawth.service.PriorityScoringEngine;
import com.pawth.view.components.*;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ReportStrayPanel extends JPanel {
    private final WorkspaceFrame frame;

    public ReportStrayPanel(WorkspaceFrame frame, boolean reports) {
        this.frame = frame;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ui.add(
                this,
                Ui.heading(
                        frame.staff ? "Shelter rescue" : "Community rescue",
                        "Report a stray animal",
                        "Your report helps our responders find and prioritize animals at risk."));
        Ui.add(
                this,
                Ui.row(
                        Ui.secondary(
                                "New report",
                                () -> frame.navigate(frame.staff ? "Rescue cases" : "Report")),
                        Ui.secondary(
                                frame.staff ? "Rescue queue" : "My reports",
                                () ->
                                        frame.navigate(
                                                frame.staff ? "Rescue queue" : "My reports"))));
        Ui.gap(this, 22);
        if (reports) showReports();
        else showForm();
    }

    private void showForm() {
        Ui.Card card = Ui.card("1  What did you see?", "Share a photo and basic animal details.");
        JComboBox<String> animal = Ui.select("Dog", "Cat", "Other"),
                size = Ui.select("Small", "Medium", "Large");
        Ui.add(
                card,
                Ui.grid(2, Ui.field("Animal type", animal), Ui.field("Approximate size", size)));
        JTextField photo = Ui.text("No photo selected");
        photo.setEditable(false);
        JButton upload =
                Ui.iconButton(
                        "Add a clear photo",
                        "46-3417/imgIcon9",
                        () -> {
                            JFileChooser chooser = new JFileChooser();
                            chooser.setFileFilter(
                                    new FileNameExtensionFilter(
                                            "Animal photos", "png", "jpg", "jpeg"));
                            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                                photo.setText(chooser.getSelectedFile().getAbsolutePath());
                        });
        Ui.add(card, Ui.row(upload));
        Ui.gap(card, 10);
        Ui.add(card, Ui.field("Photo", photo));
        Ui.gap(card, 10);
        Ui.add(card, Ui.bold("2  Where is the animal?", 15));
        Ui.gap(card, 10);
        Ui.add(
                card,
                Ui.label(
                        "Be as precise as possible so responders can help quickly.", 10, Ui.MUTED));
        Ui.gap(card, 20);
        JTextField location = Ui.text("Street, barangay, or nearby landmark"),
                barangay = Ui.text("e.g. Barangay Maligaya"),
                street = Ui.text("e.g. beside public market");
        Ui.add(card, Ui.field("Location or landmark", location));
        Ui.add(card, Ui.grid(2, Ui.field("Barangay", barangay), Ui.field("Area / street", street)));
        Ui.gap(card, 14);
        Ui.add(card, Ui.bold("3  Observed condition", 15));
        Ui.gap(card, 10);
        Ui.add(
                card,
                Ui.label(
                        "Select everything that applies. This determines priority.", 10, Ui.MUTED));
        Ui.gap(card, 18);
        JCheckBox injured = new JCheckBox("Injured"),
                road = new JCheckBox("Near busy road"),
                distressed = new JCheckBox("Aggressive or distressed"),
                school = new JCheckBox("Near school"),
                other = new JCheckBox("Other");
        Ui.add(card, Ui.grid(2, injured, distressed, road, school, other));
        Ui.gap(card, 20);
        JTextArea notes = new JTextArea(4, 30);
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        notes.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        Ui.add(card, Ui.bold("Additional notes", 11));
        Ui.gap(card, 8);
        Ui.add(card, new JScrollPane(notes));
        Ui.gap(card, 20);
        JButton submit = Ui.button("Submit rescue report", () -> {});
        Ui.stretch(submit, 46);
        submit.addActionListener(
                e -> {
                    if (location.getText().isBlank() || barangay.getText().isBlank()) {
                        Ui.error(this, "Enter the location and barangay.");
                        return;
                    }
                    if (location.getText().trim().length() > 255
                            || barangay.getText().trim().length() > 100
                            || street.getText().trim().length() > 255
                            || photo.getText().length() > 255) {
                        Ui.error(
                                this,
                                "Some details are too long. Shorten the location, barangay, street or photo path.");
                        return;
                    }
                    RescueCase rc = new RescueCase();
                    rc.setReporterId(frame.user.getUserId());
                    rc.setAnimalType((String) animal.getSelectedItem());
                    rc.setApproximateSize((String) size.getSelectedItem());
                    rc.setLocationLandmark(location.getText().trim());
                    rc.setBarangay(barangay.getText().trim());
                    rc.setAreaStreet(street.getText().trim());
                    rc.setPhotoUrl(
                            photo.getText().equals("No photo selected") ? null : photo.getText());
                    rc.setAdditionalNotes(notes.getText().trim());
                    java.util.List<String> flags = new java.util.ArrayList<>();
                    if (injured.isSelected()) flags.add("Injured");
                    if (road.isSelected()) flags.add("Near busy road");
                    if (distressed.isSelected()) flags.add("Aggressive or distressed");
                    if (school.isSelected()) flags.add("Near school");
                    if (other.isSelected()) flags.add("Other");
                    rc.setConditionFlags(String.join(", ", flags));
                    PriorityScoringEngine.calculateAndSetPriority(rc);
                    Ui.save(
                            submit,
                            () -> new RescueCaseDAO().createReport(rc),
                            () -> {
                                JOptionPane.showMessageDialog(
                                        frame, "Report saved. Priority: " + rc.getPriorityLevel());
                                frame.navigate(frame.staff ? "Rescue queue" : "My reports");
                                frame.reload();
                            });
                });
        Ui.add(card, submit);
        Ui.add(this, card);
    }

    private void showReports() {
        if (frame.data.cases().isEmpty()) {
            Ui.add(this, Ui.card("No reports yet", "New rescue reports will appear here."));
            return;
        }
        for (RescueCase rc : frame.data.cases()) {
            Ui.Card card = new Ui.Card();
            Ui.add(
                    card,
                    Ui.row(
                            Ui.bold("Report #" + rc.getCaseId() + " · " + rc.getAnimalType(), 15),
                            Ui.badge(rc.getPriorityLevel().name())));
            Ui.gap(card, 10);
            Ui.add(
                    card,
                    Ui.paragraph(rc.getLocationLandmark() + " · " + rc.getConditionFlags(), 650));
            Ui.gap(card, 18);
            JPanel progress = Ui.grid(4);
            for (RescueCase.CaseStatus s : RescueCase.CaseStatus.values()) {
                JLabel stage =
                        Ui.label(
                                s.name().replace('_', ' '),
                                9,
                                s.ordinal() <= rc.getCaseStatus().ordinal() ? Ui.GREEN : Ui.MUTED);
                stage.setBorder(
                        BorderFactory.createMatteBorder(
                                3,
                                0,
                                0,
                                0,
                                s.ordinal() <= rc.getCaseStatus().ordinal() ? Ui.GREEN : Ui.LINE));
                progress.add(stage);
            }
            Ui.add(card, progress);
            Ui.gap(card, 16);
            if (frame.staff)
                Ui.add(card, Ui.secondary("Manage case", () -> CaseEditor.show(frame, rc)));
            else
                Ui.add(
                        card,
                        Ui.label(
                                "Status: " + rc.getCaseStatus().name().replace('_', ' '),
                                10,
                                Ui.GREEN));
            Ui.add(this, card);
            Ui.gap(this, 14);
        }
    }
}
