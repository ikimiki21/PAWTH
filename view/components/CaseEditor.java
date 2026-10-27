package com.pawth.view.components;

import com.pawth.dao.RescueCaseDAO;
import com.pawth.model.RescueCase;
import javax.swing.*;

public final class CaseEditor {
    private CaseEditor() {}

    public static void show(WorkspaceFrame frame, RescueCase rc) {
        JDialog dialog = new JDialog(frame, "Manage rescue #" + rc.getCaseId(), true);
        JPanel p = Ui.column();
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        Ui.add(
                p,
                Ui.heading(
                        "Rescue operations",
                        "Report #" + rc.getCaseId(),
                        rc.getLocationLandmark()));
        JComboBox<RescueCase.CaseStatus> status = new JComboBox<>(RescueCase.CaseStatus.values());
        status.setSelectedItem(rc.getCaseStatus());
        Ui.add(p, Ui.field("Case status", status));
        JCheckBox assign =
                new JCheckBox(
                        "Assign this case to me",
                        rc.getAssignedHandlerId() != null
                                && rc.getAssignedHandlerId() == frame.user.getUserId());
        Ui.add(p, assign);
        Ui.gap(p, 12);
        Ui.add(
                p,
                Ui.label(
                        rc.getAssignedHandlerId() == null
                                ? "Currently unassigned"
                                : "Current handler ID: " + rc.getAssignedHandlerId(),
                        11,
                        Ui.MUTED));
        Ui.gap(p, 12);
        JTextArea notes = new JTextArea(rc.getHandlerNotes(), 4, 30);
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        Ui.add(p, new JScrollPane(notes));
        Ui.gap(p, 16);
        JButton save = Ui.button("Save case update", () -> {});
        save.addActionListener(
                e -> {
                    Integer handler =
                            assign.isSelected()
                                    ? frame.user.getUserId()
                                    : (rc.getAssignedHandlerId() != null
                                                    && rc.getAssignedHandlerId()
                                                            != frame.user.getUserId()
                                            ? rc.getAssignedHandlerId()
                                            : null);
                    RescueCase.CaseStatus next = (RescueCase.CaseStatus) status.getSelectedItem();
                    String noteText = notes.getText();
                    Ui.save(
                            save,
                            () ->
                                    new RescueCaseDAO()
                                            .updateCaseStatus(
                                                    rc.getCaseId(), next, handler, noteText),
                            () -> {
                                dialog.dispose();
                                frame.reload();
                            });
                });
        Ui.add(p, save);
        dialog.setContentPane(p);
        dialog.pack();
        dialog.setSize(560, 560);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
