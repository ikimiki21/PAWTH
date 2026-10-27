package com.pawth.view.components;

import com.pawth.model.*;
import com.pawth.view.auth.LoginFrame;
import com.pawth.view.community.*;
import com.pawth.view.staff.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/** Shared Figma shell; both roles reuse the same navigation and loading/error behavior. */
public class WorkspaceFrame extends JFrame {
    public final User user;
    public final boolean staff;
    public WorkspaceData data;
    private String current;
    private final JPanel host = new JPanel(new BorderLayout()), sidebar = Ui.column();
    private final JLabel section = Ui.label("Overview", 11, Ui.MUTED),
            notice = Ui.label(" ", 10, Ui.MUTED);
    private final Map<String, JButton> nav = new LinkedHashMap<>();
    private long generation;

    public WorkspaceFrame(User user) {
        this.user = user;
        staff = user.getRole() == User.Role.STAFF;
        current = staff ? "Overview" : "Home";
        setTitle("Pawth · " + (staff ? "Shelter operations" : "Community"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1183, 920);
        setMinimumSize(new Dimension(1080, 740));
        setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Ui.BG);
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 16));
        header.setBackground(Ui.CREAM);
        header.setPreferredSize(new Dimension(1167, 79));
        header.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, staff ? Color.decode("#ffc9cc") : Ui.LINE));
        JLabel brand = Ui.bold("Pawth", 25);
        brand.setForeground(Ui.CORAL);
        brand.setIcon(Ui.icon("46-2692/imgPawthLogo"));
        brand.setIconTextGap(10);
        header.add(Box.createHorizontalStrut(12));
        header.add(brand);
        header.add(Ui.label("   |   ", 14, Ui.LINE));
        header.add(section);
        header.add(
                Ui.iconButton(
                        "",
                        staff ? "46-5292/imgIcon" : "46-2692/imgIcon",
                        () -> navigate("Notifications")));
        JButton avatar = Ui.button(initials(user.getFullName()), () -> navigate("Profile"));
        avatar.setBackground(Ui.INK);
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.putClientProperty("FlatLaf.style", "arc: 999; margin: 0,0,0,0");
        header.add(avatar);
        JButton refresh = Ui.secondary("Refresh", this::reload);
        header.add(refresh);
        header.add(notice);
        root.add(header, BorderLayout.NORTH);
        sidebar.setOpaque(true);
        sidebar.setBackground(staff ? Color.decode("#fff9f9") : Color.WHITE);
        sidebar.setPreferredSize(new Dimension(236, 700));
        sidebar.setBorder(new EmptyBorder(30, 18, 20, 18));
        Ui.add(
                sidebar,
                Ui.label(staff ? "SHELTER WORKSPACE" : "MY PAWTH", 9, staff ? Ui.CORAL : Ui.MUTED));
        Ui.gap(sidebar, 18);
        String[] names =
                staff
                        ? new String[] {
                            "Operations",
                            "Rescue cases",
                            "Animal records",
                            "Notifications",
                            "Staff profile"
                        }
                        : new String[] {"Home", "Adopt", "Report", "Alerts", "Profile"};
        String[] paths =
                staff
                        ? new String[] {
                            "46-5292/imgIcon1",
                            "46-5292/imgIcon2",
                            "46-5292/imgIcon3",
                            "46-5292/imgIcon4",
                            "46-5292/imgIcon5"
                        }
                        : new String[] {
                            "46-2692/imgIcon1",
                            "46-2692/imgIcon2",
                            "46-2692/imgIcon3",
                            "46-2692/imgIcon4",
                            "46-2692/imgIcon5"
                        };
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            JButton b = Ui.iconButton(name, paths[i], () -> navigate(name));
            b.putClientProperty("FlatLaf.style", "arc: 20; borderWidth: 0; margin: 12,12,12,12");
            b.setHorizontalAlignment(SwingConstants.LEFT);
            Ui.stretch(b, 44);
            Ui.add(sidebar, b);
            Ui.gap(sidebar, 5);
            nav.put(name, b);
        }
        Ui.gap(sidebar, 65);
        Ui.Card support = new Ui.Card(staff ? Ui.PINK : Color.decode("#f0f5ec"));
        Ui.add(support, new JLabel(Ui.icon(staff ? "46-5292/imgIcon6" : "46-2692/imgIcon6")));
        Ui.gap(support, 16);
        Ui.add(support, Ui.bold(staff ? "Shelter workspace" : "Rescue community", 11));
        Ui.gap(support, 10);
        Ui.add(
                support,
                Ui.paragraph(
                        staff
                                ? "Review reports and prioritize animals needing help."
                                : "Report animals in need and follow your rescue cases.",
                        150));
        Ui.gap(support, 12);
        Ui.add(
                support,
                Ui.secondary(
                        staff ? "View cases" : "My reports",
                        () -> navigate(staff ? "Rescue queue" : "My reports")));
        Ui.add(sidebar, support);
        root.add(sidebar, BorderLayout.WEST);
        host.setBackground(Ui.BG);
        root.add(host, BorderLayout.CENTER);
        setContentPane(root);
        reload();
    }

    public static String initials(String name) {
        StringBuilder b = new StringBuilder();
        for (String p : name.trim().split("\\s+")) if (!p.isEmpty()) b.append(p.charAt(0));
        return b.substring(0, Math.min(2, b.length())).toUpperCase();
    }

    public void navigate(String name) {
        current =
                switch (name) {
                    case "Operations" -> "Overview";
                    case "Alerts" -> "Notifications";
                    case "Staff profile" -> "Profile";
                    default -> name;
                };
        if (data != null) showPage();
    }

    public String currentPage() {
        return current;
    }

    public void showDetails(Animal animal) {
        current = "Adopt";
        showPage();
        JPanel page = new AnimalDetails(this, animal);
        page.setBorder(new EmptyBorder(38, 32, 40, 32));
        host.removeAll();
        JScrollPane scroll = Ui.scroll(page);
        host.add(scroll);
        host.revalidate();
        host.repaint();
        SwingUtilities.invokeLater(
                () -> {
                    scroll.getViewport().setViewPosition(new Point(0, 0));
                });
    }

    public void reload() {
        long request = ++generation;
        notice.setText("Loading…");
        if (data == null) {
            host.removeAll();
            host.add(Ui.label("Loading your workspace…", 16, Ui.MUTED));
            host.revalidate();
        }
        new SwingWorker<WorkspaceData, Void>() {
            protected WorkspaceData doInBackground() {
                return WorkspaceData.load(user);
            }

            protected void done() {
                if (request != generation || !isDisplayable()) return;
                try {
                    data = get();
                    notice.setText("Updated just now");
                    showPage();
                } catch (Exception e) {
                    notice.setText("Connection unavailable");
                    host.removeAll();
                    Ui.Card failure =
                            Ui.card(
                                    "Could not load workspace",
                                    "Start MySQL and retry. Your data has not been changed.");
                    Ui.add(failure, Ui.button("Retry", WorkspaceFrame.this::reload));
                    host.add(failure, BorderLayout.NORTH);
                    host.revalidate();
                    host.repaint();
                }
            }
        }.execute();
    }

    private void showPage() {
        section.setText(
                switch (current) {
                    case "Adopt", "Animal records" -> "Adoption center";
                    case "Report", "My reports", "Rescue cases", "Rescue queue" -> "Rescue reports";
                    case "Profile" -> "My profile";
                    case "Notifications" -> "Notifications";
                    default -> staff ? "Shelter operations" : "Overview";
                });
        String active =
                staff
                        ? switch (current) {
                            case "Overview", "Applications", "Hotspots" -> "Operations";
                            case "Profile" -> "Staff profile";
                            case "Rescue queue" -> "Rescue cases";
                            default -> current;
                        }
                        : switch (current) {
                            case "My reports" -> "Report";
                            case "Notifications" -> "Alerts";
                            default -> current;
                        };
        String context =
                staff
                        ? switch (active) {
                            case "Animal records" -> "46-6516";
                            case "Notifications" -> "46-6821";
                            case "Staff profile" -> "46-6983";
                            case "Rescue cases" -> "46-6114";
                            default -> "46-5292";
                        }
                        : switch (active) {
                            case "Adopt" -> "46-3189";
                            case "Report" -> "46-3417";
                            case "Alerts" -> "46-3631";
                            case "Profile" -> "46-3801";
                            default -> "46-2692";
                        };
        int navIndex = 0;
        for (Map.Entry<String, JButton> entry : nav.entrySet()) {
            String name = entry.getKey();
            JButton b = entry.getValue();
            boolean selected = name.equals(active);
            b.setBackground(
                    selected
                            ? (staff ? Ui.CORAL : Ui.PINK)
                            : (staff ? Color.decode("#fff9f9") : Color.WHITE));
            b.setForeground(selected ? (staff ? Color.WHITE : Ui.CORAL) : Ui.MUTED);
            int offset = context.equals("46-3189") || context.equals("46-6821") ? 2 : 1;
            b.setIcon(Ui.icon(context + "/imgIcon" + (navIndex++ + offset)));
        }
        JPanel page =
                switch (current) {
                    case "Home" -> new HomePanel(this);
                    case "Adopt" -> new AdoptPanel(this);
                    case "Report", "Rescue cases" -> new ReportStrayPanel(this, false);
                    case "My reports", "Rescue queue" -> new ReportStrayPanel(this, true);
                    case "Notifications" -> new NotificationsPanel(this);
                    case "Profile" -> new ProfileView(this);
                    case "Animal records" -> new com.pawth.view.staff.AnimalRecordsPanel(this);
                    default -> new OperationsPanel(this, current);
                };
        page.setBorder(new EmptyBorder(48, 36, 40, 36));
        host.removeAll();
        JScrollPane scroll = Ui.scroll(page);
        host.add(scroll);
        host.revalidate();
        host.repaint();
        SwingUtilities.invokeLater(
                () -> {
                    scroll.getViewport().setViewPosition(new Point(0, 0));
                });
    }

    public void logout() {
        generation++;
        dispose();
        new LoginFrame().setVisible(true);
    }

    @Override
    public void dispose() {
        generation++;
        super.dispose();
    }
}
