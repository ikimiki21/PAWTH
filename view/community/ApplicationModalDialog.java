package com.pawth.view.community;

import com.pawth.dao.AdoptionApplicationDAO;
import com.pawth.model.*;
import com.pawth.view.components.*;
import java.awt.*;
import javax.swing.*;

public class ApplicationModalDialog extends JDialog {
    private final WorkspaceFrame frame;
    private final Animal animal;
    private final JPanel content = Ui.column();

    public ApplicationModalDialog(WorkspaceFrame frame, Animal animal) {
        super(frame, "Apply for " + animal.getName(), true);
        this.frame = frame;
        this.animal = animal;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        content.setOpaque(true);
        content.setBackground(Ui.CREAM);
        setContentPane(Ui.scroll(content));
        setSize(640, 730);
        setLocationRelativeTo(frame);
        answers();
    }

    private void title(String eyebrow, String title) {
        content.removeAll();
        Ui.add(content, Ui.label(eyebrow.toUpperCase(), 11, Ui.CORAL));
        Ui.gap(content, 16);
        Ui.add(content, Ui.label(title, 27, Ui.INK));
        Ui.gap(content, 12);
        Ui.add(content, Ui.paragraph("Applying for " + animal.getName() + ".", 500));
        Ui.gap(content, 20);
    }

    private JTextField read(String value) {
        JTextField f = Ui.text("");
        f.setText(value == null ? "Not provided" : value);
        f.setEditable(false);
        return f;
    }

    private void answers() {
        title("Adoption application", "Tell us about your home");
        Ui.add(
                content,
                Ui.paragraph(
                        "Compatibility uses your existing profile. Your account details remain unchanged.",
                        510));
        Ui.gap(content, 20);
        Ui.add(
                content,
                Ui.grid(
                        2,
                        Ui.field("Full name", read(frame.user.getFullName())),
                        Ui.field("Phone number", read(frame.user.getPhone()))));
        AdopterProfile p = frame.user.getAdopterProfile();
        Ui.add(content, Ui.field("Home type", read(p == null ? null : p.getHomeType())));
        Ui.add(
                content,
                Ui.grid(
                        2,
                        Ui.field(
                                "Children at home?",
                                read(p == null ? null : p.isHasChildren() ? "Yes" : "No")),
                        Ui.field(
                                "Other pets?",
                                read(p == null ? null : p.isHasOtherPets() ? "Yes" : "No"))));
        Ui.add(
                content,
                Ui.field("Pet care experience", read(p == null ? null : p.getPetExperience())));
        Ui.add(content, Ui.button("Review compatibility", this::compatibility));
        render();
    }

    private void compatibility() {
        title("Adoption application", "Your compatibility");
        Ui.gap(content, 20);
        Ui.add(content, Ui.score(animal.getMatchPercentage()));
        Ui.gap(content, 25);
        Ui.add(content, Ui.label("Meet " + animal.getName(), 24, Ui.INK));
        Ui.gap(content, 16);
        Ui.add(
                content,
                Ui.paragraph(
                        "This score is an initial guide based on your saved home and lifestyle preferences. A shelter handler will confirm suitability during consultation.",
                        500));
        Ui.gap(content, 30);
        Ui.add(content, Ui.button("Submit application", this::confirm));
        Ui.gap(content, 12);
        Ui.add(content, Ui.secondary("Back to profile", this::answers));
        render();
    }

    private void confirm() {
        title("Final confirmation", "Apply to adopt " + animal.getName() + "?");
        Ui.add(
                content,
                Ui.paragraph(
                        "Once submitted, the Pawth shelter team will review your information and contact you to arrange the next step.",
                        500));
        Ui.gap(content, 24);
        Ui.Card animalCard = new Ui.Card();
        Ui.add(
                animalCard,
                Ui.row(
                        Ui.photo(animal.getPhotoUrl(), 65, 65),
                        Ui.bold(animal.getName(), 17),
                        Ui.badge(animal.getMatchPercentage() + "% match")));
        Ui.add(content, animalCard);
        Ui.gap(content, 20);
        Ui.add(
                content,
                Ui.paragraph(
                        "Your application does not guarantee adoption. A consultation and home assessment may still be required.",
                        500));
        Ui.gap(content, 26);
        JButton submit = Ui.button("Confirm & submit", () -> {});
        submit.addActionListener(
                e -> {
                    AdoptionApplication application = new AdoptionApplication();
                    application.setApplicantId(frame.user.getUserId());
                    application.setAnimalId(animal.getAnimalId());
                    application.setMatchPercentage(animal.getMatchPercentage());
                    application.setStatus(AdoptionApplication.ApplicationStatus.NEW);
                    Ui.save(
                            submit,
                            () -> new AdoptionApplicationDAO().submitApplication(application),
                            () -> {
                                dispose();
                                JOptionPane.showMessageDialog(
                                        frame,
                                        "Your application for "
                                                + animal.getName()
                                                + " was submitted.");
                                frame.reload();
                            });
                });
        Ui.add(content, Ui.grid(2, Ui.secondary("Review again", this::answers), submit));
        render();
    }

    private void render() {
        content.revalidate();
        content.repaint();
    }
}
