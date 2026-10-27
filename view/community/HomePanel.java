package com.pawth.view.community;

import com.pawth.view.components.*;
import com.pawth.model.*;
import java.awt.*;
import java.time.LocalTime;
import javax.swing.*;

public class HomePanel extends JPanel {
    public HomePanel(WorkspaceFrame frame) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        int hour = LocalTime.now().getHour();
        String greeting =
                hour < 12 ? "Good morning." : hour < 18 ? "Good afternoon." : "Good evening.";
        Ui.add(
                this,
                Ui.heading(
                        "Community overview",
                        greeting,
                        "Here is what is happening in your rescue community today."));
        JPanel actions =
                Ui.grid(
                        2,
                        Ui.iconButton(
                                "Animal in danger?  Report a stray",
                                "46-2692/imgIcon8",
                                () -> frame.navigate("Report")),
                        Ui.iconButton(
                                "Find your match  ·  Adopt",
                                "46-2692/imgIcon10",
                                () -> frame.navigate("Adopt")));
        Ui.stretch(actions, 84);
        Ui.add(this, actions);
        Ui.gap(this, 16);
        long available =
                frame.data.animals().stream()
                        .filter(a -> a.getAdoptionStatus() == Animal.AdoptionStatus.AVAILABLE)
                        .count();
        Ui.add(
                this,
                Ui.grid(
                        3,
                        metric(
                                "Your reports",
                                frame.data.cases().size(),
                                "Reports submitted by you"),
                        metric(
                                "Unread updates",
                                frame.data.notifications().stream()
                                        .filter(n -> !n.isRead())
                                        .count(),
                                "Your latest notifications"),
                        metric("Available to adopt", available, "Verified by shelter staff")));
        Ui.gap(this, 24);
        Ui.Card recommended =
                Ui.card("Recommended for you", "Based on your home and lifestyle preferences.");
        frame.data.animals().stream()
                .filter(a -> a.getAdoptionStatus() == Animal.AdoptionStatus.AVAILABLE)
                .sorted((a, b) -> Integer.compare(b.getMatchPercentage(), a.getMatchPercentage()))
                .limit(3)
                .forEach(
                        a -> {
                            JPanel details = Ui.column();
                            Ui.add(details, Ui.bold(a.getName(), 13));
                            Ui.gap(details, 8);
                            Ui.add(
                                    details,
                                    Ui.label(
                                            a.getBreed() + " · " + a.getAgeYears() + " years",
                                            10,
                                            Ui.MUTED));
                            Ui.gap(details, 6);
                            Ui.add(details, Ui.badge(a.getMatchPercentage() + "% match"));
                            JPanel item = new JPanel(new BorderLayout(14, 0));
                            item.setOpaque(false);
                            item.add(Ui.photo(a.getPhotoUrl(), 55, 65), BorderLayout.WEST);
                            item.add(details);
                            item.add(
                                    Ui.secondary("Meet", () -> AnimalDetails.show(frame, a)),
                                    BorderLayout.EAST);
                            Ui.add(recommended, item);
                            Ui.gap(recommended, 20);
                        });
        if (available == 0)
            Ui.add(recommended, Ui.label("No animals are currently available.", 12, Ui.MUTED));
        Ui.add(recommended, Ui.secondary("See all animals", () -> frame.navigate("Adopt")));
        Ui.Card activity = Ui.card("Rescue activity", "Latest updates for your account.");
        frame.data.notifications().stream()
                .limit(3)
                .forEach(
                        n -> {
                            Ui.add(activity, Ui.bold(n.getTitle(), 11));
                            Ui.gap(activity, 7);
                            Ui.add(activity, Ui.paragraph(n.getMessage(), 280));
                            Ui.gap(activity, 22);
                        });
        if (frame.data.notifications().isEmpty())
            Ui.add(activity, Ui.label("You're all caught up.", 12, Ui.MUTED));
        Ui.add(activity, Ui.secondary("View notifications", () -> frame.navigate("Notifications")));
        Ui.add(this, Ui.grid(2, recommended, activity));
    }

    public static Ui.Card metric(String title, long value, String detail) {
        Ui.Card p = new Ui.Card();
        Ui.add(p, Ui.label(title, 10, Ui.MUTED));
        Ui.gap(p, 10);
        Ui.add(p, Ui.bold(String.valueOf(value), 26));
        Ui.gap(p, 10);
        Ui.add(p, Ui.label(detail, 9, Ui.GREEN));
        return p;
    }
}
