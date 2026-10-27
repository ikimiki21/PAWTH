package com.pawth.view.components;

import com.pawth.dao.NotificationDAO;
import com.pawth.model.Notification;
import javax.swing.*;

public class NotificationsPanel extends JPanel {
    public NotificationsPanel(WorkspaceFrame frame) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Ui.add(
                this,
                Ui.heading(
                        frame.staff ? "Shelter updates" : "Your updates",
                        "Notifications",
                        "Case activity, adoption updates, and important reminders."));
        JButton read = Ui.secondary("Mark all as read", () -> {});
        read.setEnabled(frame.data.notifications().stream().anyMatch(n -> !n.isRead()));
        read.addActionListener(
                e ->
                        Ui.save(
                                read,
                                () -> new NotificationDAO().markAllAsRead(frame.user.getUserId()),
                                frame::reload));
        JComboBox<String> category = Ui.select("All updates", "RESCUE", "ADOPTION", "GENERAL");
        Ui.add(this, Ui.row(category, read));
        Ui.gap(this, 22);
        JPanel list = Ui.column();
        Ui.add(this, list);
        Runnable filter =
                () -> {
                    list.removeAll();
                    int count = 0;
                    for (Notification n : frame.data.notifications())
                        if (category.getSelectedIndex() == 0
                                || n.getCategory().name().equals(category.getSelectedItem())) {
                            Ui.Card card = new Ui.Card(n.isRead() ? java.awt.Color.WHITE : Ui.PINK);
                            Ui.add(
                                    card,
                                    Ui.row(
                                            Ui.bold(n.getTitle(), 13),
                                            Ui.badge(n.isRead() ? "READ" : "UNREAD")));
                            Ui.gap(card, 10);
                            Ui.add(card, Ui.paragraph(n.getMessage(), 650));
                            Ui.gap(card, 12);
                            Ui.add(
                                    card,
                                    Ui.label(
                                            n.getCategory() + " · " + n.getCreatedAt(),
                                            10,
                                            Ui.MUTED));
                            Ui.add(list, card);
                            Ui.gap(list, 12);
                            count++;
                        }
                    if (count == 0)
                        Ui.add(
                                list,
                                Ui.card(
                                        "You're all caught up",
                                        "No notifications in this category."));
                    list.revalidate();
                    list.repaint();
                };
        category.addActionListener(e -> filter.run());
        filter.run();
    }
}
