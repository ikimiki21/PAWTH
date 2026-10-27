package com.pawth.view.auth;

import com.pawth.model.User;
import com.pawth.service.AuthService;
import com.pawth.view.community.CommunityDashboardFrame;
import com.pawth.view.staff.StaffDashboardFrame;
import com.pawth.view.components.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private final JTextField email = Ui.text("you@example.com");
    private final JPasswordField password = new JPasswordField();
    private final JToggleButton
            community =
                    new JToggleButton(
                            "<html><b>Community</b><br><small>Adopter or reporter</small></html>"),
            staff =
                    new JToggleButton(
                            "<html><b>Staff</b><br><small>Handler or admin</small></html>");
    private final JButton login = Ui.button("Open my account", this::login);
    private final JLabel status = Ui.label(" ", 11, Ui.CORAL);

    public LoginFrame() {
        setTitle("Pawth · Sign in");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(Ui.CREAM);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = .46;
        JPanel story =
                new JPanel() {
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D x = (Graphics2D) g.create();
                        x.setColor(new Color(255, 255, 255, 14));
                        x.draw(new Ellipse2D.Double(getWidth() / 2.0, -190, 520, 520));
                        x.dispose();
                    }
                };
        story.setPreferredSize(new Dimension(541, 606));
        story.setBackground(Ui.CORAL);
        story.setBorder(new EmptyBorder(48, 56, 36, 48));
        story.setLayout(new BoxLayout(story, BoxLayout.Y_AXIS));
        JLabel brand = Ui.bold("Pawth", 25);
        brand.setForeground(Color.WHITE);
        brand.setIcon(Ui.icon("login/imgVector"));
        brand.setIconTextGap(10);
        Ui.add(story, brand);
        Ui.gap(story, 62);
        Ui.add(story, Ui.label("RESCUE · CARE · ADOPT", 10, Color.WHITE));
        Ui.gap(story, 17);
        Ui.add(
                story,
                Ui.label(
                        "<html>Every safe<br>journey starts<br>with someone<br>who cares.</html>",
                        48,
                        Color.WHITE));
        Ui.gap(story, 22);
        JComponent intro =
                Ui.paragraph(
                        "Join your local rescue community to report animals in need, track cases, and help them find a home.",
                        360);
        intro.setForeground(Color.WHITE);
        Ui.add(story, intro);
        Ui.gap(story, 30);
        JLabel tagline = (JLabel) story.getComponent(2), headline = (JLabel) story.getComponent(4);
        story.removeAll();
        story.add(brand);
        story.add(tagline);
        story.add(headline);
        story.add(intro);
        story.setLayout(
                new LayoutManager() {
                    public void addLayoutComponent(String name, Component component) {}

                    public void removeLayoutComponent(Component component) {}

                    public Dimension preferredLayoutSize(Container parent) {
                        return new Dimension(541, 606);
                    }

                    public Dimension minimumLayoutSize(Container parent) {
                        return new Dimension(490, 606);
                    }

                    public void layoutContainer(Container parent) {
                        int width = parent.getWidth() - 104;
                        brand.setBounds(56, 48, width, 40);
                        tagline.setBounds(56, 130, width, 18);
                        headline.setBounds(56, 158, width, 245);
                        intro.setBounds(56, 414, width, 62);
                    }
                });
        root.add(story, c);
        c.gridx = 1;
        c.weightx = .54;
        JPanel right = new JPanel(new GridBagLayout());
        right.setPreferredSize(new Dimension(626, 606));
        right.setBackground(Ui.CREAM);
        right.setBorder(new EmptyBorder(44, 44, 44, 44));
        JPanel form = Ui.column();
        form.setPreferredSize(new Dimension(440, 540));
        Ui.add(form, Ui.label("WELCOME!", 11, Ui.CORAL));
        Ui.gap(form, 14);
        Ui.add(form, Ui.label("Sign in to your account", 29, Ui.INK));
        Ui.gap(form, 12);
        Ui.add(
                form,
                Ui.label("Choose your workspace and continue where you left off.", 10, Ui.MUTED));
        Ui.gap(form, 26);
        ButtonGroup group = new ButtonGroup();
        group.add(community);
        group.add(staff);
        community.setSelected(true);
        community.setIcon(Ui.icon("login/imgIcon1"));
        staff.setIcon(Ui.icon("login/imgIcon2"));
        for (JToggleButton b : new JToggleButton[] {community, staff}) {
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setIconTextGap(10);
            b.putClientProperty(
                    "FlatLaf.style",
                    "selectedBackground: #fff0f1; selectedForeground: #d94f57; arc: 22; margin: 12,12,12,12");
            b.setBackground(Color.WHITE);
            b.setFont(Ui.font(11));
        }
        JPanel roles = Ui.grid(2, community, staff);
        Ui.stretch(roles, 54);
        Ui.add(form, roles);
        Ui.gap(form, 24);
        Ui.add(form, Ui.field("Email address", email));
        password.putClientProperty("JTextField.placeholderText", "Enter your password");
        Ui.add(form, Ui.field("Password", password));
        login.setIcon(Ui.icon("login/imgIcon3"));
        login.setHorizontalTextPosition(SwingConstants.LEFT);
        login.setIconTextGap(12);
        Ui.stretch(login, 46);
        Ui.add(form, login);
        Ui.gap(form, 12);
        Ui.add(form, status);
        Ui.gap(form, 22);
        Ui.add(
                form,
                Ui.paragraph(
                        "Use your existing Pawth account. Choose the workspace assigned to your account.",
                        400));
        right.add(form);
        root.add(right, c);
        setContentPane(root);
        setSize(1183, 680);
        setMinimumSize(new Dimension(1100, 670));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(login);
    }

    private void login() {
        String address = email.getText().trim();
        char[] chars = password.getPassword();
        String secret = new String(chars);
        java.util.Arrays.fill(chars, '\0');
        if (address.isEmpty() || secret.isEmpty()) {
            status.setText("Enter your email address and password.");
            return;
        }
        String role = staff.isSelected() ? "STAFF" : "COMMUNITY";
        login.setEnabled(false);
        community.setEnabled(false);
        staff.setEnabled(false);
        status.setText("Signing in…");
        new SwingWorker<User, Void>() {
            protected User doInBackground() {
                return new AuthService().login(address, secret, role);
            }

            protected void done() {
                login.setEnabled(true);
                community.setEnabled(true);
                staff.setEnabled(true);
                try {
                    User user = get();
                    if (user == null) {
                        status.setText("Check your credentials and selected workspace.");
                        return;
                    }
                    password.setText("");
                    JFrame frame =
                            user.getRole() == User.Role.STAFF
                                    ? new StaffDashboardFrame(user)
                                    : new CommunityDashboardFrame(user);
                    frame.setVisible(true);
                    dispose();
                } catch (Exception e) {
                    status.setText("Database unavailable. Start MySQL and try again.");
                }
            }
        }.execute();
    }
}
