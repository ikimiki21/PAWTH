package com.pawth.view.components;

import com.pawth.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ProfileView extends JPanel {
    public ProfileView(WorkspaceFrame frame) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        User u = frame.user;
        Ui.add(
                this,
                Ui.heading(
                        frame.staff ? "Staff account" : "Personal account",
                        "My profile",
                        "Your details and preferences used across your Pawth account."));
        Ui.Card identity =
                new Ui.Card() {
                    protected void paintComponent(Graphics graphics) {
                        super.paintComponent(graphics);
                        Graphics2D g = (Graphics2D) graphics.create();
                        g.clip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 32, 32));
                        g.setPaint(
                                new GradientPaint(
                                        0,
                                        0,
                                        Color.decode("#263f39"),
                                        getWidth(),
                                        80,
                                        Color.decode("#6b9688")));
                        g.fillRect(0, 0, getWidth(), 80);
                        g.dispose();
                    }
                };
        Ui.gap(identity, 20);
        JPanel portrait = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        portrait.setOpaque(false);
        portrait.add(Ui.avatar(WorkspaceFrame.initials(u.getFullName())));
        Ui.stretch(portrait, 76);
        Ui.add(identity, portrait);
        Ui.gap(identity, 18);
        JLabel name = Ui.bold(u.getFullName(), 18);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        Ui.add(identity, name);
        Ui.gap(identity, 10);
        JLabel email = Ui.label(u.getEmail(), 11, Ui.MUTED);
        email.setHorizontalAlignment(SwingConstants.CENTER);
        Ui.add(identity, email);
        Ui.gap(identity, 16);
        Ui.add(identity, Ui.badge(u.getRole().name()));
        Ui.gap(identity, 24);
        if (frame.staff && u.getStaffProfile() != null) {
            StaffProfile s = u.getStaffProfile();
            Ui.add(identity, Ui.paragraph(s.getShelterName(), 230));
            Ui.gap(identity, 16);
            Ui.add(identity, Ui.label(s.getCasesHandled() + " cases handled", 12, Ui.INK));
            Ui.gap(identity, 8);
            Ui.add(identity, Ui.label(s.getRescuesCount() + " rescues", 12, Ui.INK));
        } else Ui.add(identity, Ui.label(frame.data.cases().size() + " reports", 12, Ui.INK));
        Ui.gap(identity, 30);
        Ui.add(identity, Ui.secondary("Log out", frame::logout));
        Ui.Card details = Ui.card("Account details", "Information from your existing profile.");
        Ui.add(details, field("Full name", u.getFullName()));
        Ui.add(details, field("Email address", u.getEmail()));
        Ui.add(
                details,
                Ui.grid(2, field("Phone", u.getPhone()), field("Barangay", u.getBarangay())));
        if (frame.staff) {
            StaffProfile s = u.getStaffProfile();
            if (s != null) {
                Ui.add(details, field("Shelter name", s.getShelterName()));
                Ui.add(details, field("Staff position", s.getPosition()));
                Ui.add(
                        details,
                        Ui.grid(
                                2,
                                field("Staff ID", s.getStaffCustomId()),
                                field("Shift", s.getShiftSchedule())));
            }
        } else {
            AdopterProfile a = u.getAdopterProfile();
            if (a != null) {
                Ui.add(
                        details,
                        Ui.grid(
                                2,
                                field("Home type", a.getHomeType()),
                                field("Preferred animal", a.getPreferredAnimal())));
                Ui.add(
                        details,
                        Ui.grid(
                                2,
                                field("Children at home", a.isHasChildren() ? "Yes" : "No"),
                                field("Other pets", a.isHasOtherPets() ? "Yes" : "No")));
                Ui.add(details, field("Pet care experience", a.getPetExperience()));
            }
        }
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(identity, BorderLayout.NORTH);
        left.setPreferredSize(new Dimension(280, identity.getPreferredSize().height));
        JPanel columns = new JPanel(new BorderLayout(18, 0));
        columns.setOpaque(false);
        columns.add(left, BorderLayout.WEST);
        columns.add(details, BorderLayout.CENTER);
        Ui.add(this, columns);
    }

    private JComponent field(String title, String value) {
        JTextField f = Ui.text("");
        f.setText(value == null ? "Not provided" : value);
        f.setEditable(false);
        return Ui.field(title, f);
    }
}
