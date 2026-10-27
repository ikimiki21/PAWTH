package com.pawth.view.staff;

import com.pawth.model.*;
import com.pawth.view.components.*;
import com.pawth.view.community.HomePanel;
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class OperationsPanel extends JPanel {
    private final WorkspaceFrame frame;

    public OperationsPanel(WorkspaceFrame frame, String tab) {
        this.frame = frame;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        String title =
                tab.equals("Applications")
                        ? "Adoption applications"
                        : tab.equals("Hotspots") ? "Stray hotspots" : "Shelter overview";
        Ui.add(
                this,
                Ui.heading(
                        "Live operations",
                        title,
                        "Shelter operations based on current database records."));
        JPanel tabs = Ui.row();
        for (String name : new String[] {"Overview", "Applications", "Hotspots"}) {
            JButton b = Ui.secondary(name, () -> frame.navigate(name));
            if (name.equals(tab)) {
                b.setBackground(Ui.CORAL);
                b.setForeground(Color.WHITE);
            }
            tabs.add(b);
        }
        Ui.add(this, tabs);
        Ui.gap(this, 26);
        switch (tab) {
            case "Applications" -> applications();
            case "Hotspots" -> hotspots();
            default -> overview();
        }
    }

    private void overview() {
        long
                pending =
                        frame.data.cases().stream()
                                .filter(r -> r.getCaseStatus() == RescueCase.CaseStatus.REPORTED)
                                .count(),
                critical =
                        frame.data.cases().stream()
                                .filter(
                                        r ->
                                                r.getCaseStatus() != RescueCase.CaseStatus.RESCUED
                                                        && (r.getPriorityLevel()
                                                                        == RescueCase.PriorityLevel
                                                                                .HIGH
                                                                || r.getPriorityLevel()
                                                                        == RescueCase.PriorityLevel
                                                                                .CRITICAL))
                                .count();
        long
                available =
                        frame.data.animals().stream()
                                .filter(
                                        a ->
                                                a.getAdoptionStatus()
                                                        == Animal.AdoptionStatus.AVAILABLE)
                                .count(),
                rescued =
                        frame.data.cases().stream()
                                .filter(r -> r.getCaseStatus() == RescueCase.CaseStatus.RESCUED)
                                .count();
        Ui.add(
                this,
                Ui.grid(
                        3,
                        HomePanel.metric("Pending reports", pending, "Awaiting verification"),
                        HomePanel.metric("High-priority cases", critical, "Needs action now"),
                        HomePanel.metric(
                                "Animals in shelter",
                                frame.data.animals().stream()
                                        .filter(
                                                a ->
                                                        a.getAdoptionStatus()
                                                                != Animal.AdoptionStatus.ADOPTED)
                                        .count(),
                                "Current shelter inventory"),
                        HomePanel.metric("Available animals", available, "Published to adopters"),
                        HomePanel.metric(
                                "Applications",
                                frame.data.applications().size(),
                                "All application statuses"),
                        HomePanel.metric("Rescued cases", rescued, "Completed rescues")));
        Ui.gap(this, 24);
        Ui.Card queue =
                Ui.card(
                        "Pending and high-priority rescue cases",
                        "Ranked automatically by urgency and risk.");
        java.util.List<RescueCase> active =
                frame.data.cases().stream()
                        .filter(r -> r.getCaseStatus() != RescueCase.CaseStatus.RESCUED)
                        .toList();
        if (active.isEmpty()) Ui.add(queue, Ui.label("No active cases.", 12, Ui.MUTED));
        for (RescueCase r : active) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            JPanel summary = Ui.column();
            Ui.add(
                    summary,
                    Ui.bold(
                            "#"
                                    + r.getCaseId()
                                    + " · "
                                    + r.getAnimalType()
                                    + " · "
                                    + r.getLocationLandmark(),
                            12));
            Ui.gap(summary, 8);
            Ui.add(
                    summary,
                    Ui.row(
                            Ui.badge(r.getPriorityLevel().name() + " · " + r.getPriorityScore()),
                            Ui.label(r.getCaseStatus().name().replace('_', ' '), 10, Ui.MUTED)));
            row.add(summary);
            row.add(Ui.secondary("Manage", () -> CaseEditor.show(frame, r)), BorderLayout.EAST);
            Ui.add(queue, row);
            Ui.gap(queue, 18);
        }
        Ui.add(this, queue);
    }

    private void applications() {
        Ui.Card list =
                Ui.card(
                        "Application queue",
                        "Review compatibility and the current application status.");
        if (frame.data.applications().isEmpty())
            Ui.add(list, Ui.label("No applications yet.", 12, Ui.MUTED));
        for (AdoptionApplication app : frame.data.applications()) {
            JPanel row = new JPanel(new BorderLayout(12, 0));
            row.setOpaque(false);
            JPanel details = Ui.column();
            Ui.add(details, Ui.bold(app.getApplicantName(), 13));
            Ui.gap(details, 9);
            Ui.add(details, Ui.label("Applied for " + app.getAnimalName(), 11, Ui.MUTED));
            row.add(details);
            row.add(
                    Ui.row(
                            Ui.badge(app.getMatchPercentage() + "% match"),
                            Ui.badge(app.getStatus().name()),
                            Ui.secondary(
                                    "Review",
                                    () -> {
                                        JOptionPane.showMessageDialog(
                                                frame,
                                                "Application #"
                                                        + app.getApplicationId()
                                                        + "\nApplicant: "
                                                        + app.getApplicantName()
                                                        + "\nAnimal: "
                                                        + app.getAnimalName()
                                                        + "\nCompatibility: "
                                                        + app.getMatchPercentage()
                                                        + "%\nStatus: "
                                                        + app.getStatus()
                                                        + "\nSubmitted: "
                                                        + app.getCreatedAt(),
                                                "Application details",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    })),
                    BorderLayout.EAST);
            Ui.add(list, row);
            Ui.gap(list, 24);
        }
        Ui.add(this, list);
    }

    private void hotspots() {
        Ui.Card list =
                Ui.card("Reports by area", "All recorded rescue reports, ranked by barangay.");
        int max =
                frame.data.hotspots().values().stream().mapToInt(Integer::intValue).max().orElse(1);
        int rank = 1;
        java.util.List<Map.Entry<String, Integer>> ordered =
                frame.data.hotspots().entrySet().stream()
                        .sorted(
                                Map.Entry.<String, Integer>comparingByValue()
                                        .reversed()
                                        .thenComparing(Map.Entry.comparingByKey()))
                        .toList();
        for (Map.Entry<String, Integer> item : ordered) {
            JProgressBar bar = new JProgressBar(0, max);
            bar.setValue(item.getValue());
            bar.setForeground(Ui.CORAL);
            bar.setPreferredSize(new Dimension(180, 8));
            Ui.add(
                    list,
                    Ui.row(
                            Ui.label(String.valueOf(rank++), 18, Ui.CORAL),
                            Ui.bold(item.getKey(), 13),
                            bar,
                            Ui.bold(item.getValue().toString(), 16)));
            Ui.gap(list, 25);
        }
        if (ordered.isEmpty()) Ui.add(list, Ui.label("No reported areas yet.", 12, Ui.MUTED));
        Ui.add(this, list);
    }
}
