package com.pawth.view.components;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.font.*;
import java.text.AttributedString;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

/** Shared visual primitives translated from the official Figma design. */
public final class Ui {
    public static final Color CORAL = Color.decode("#ff6268"),
            INK = Color.decode("#202427"),
            MUTED = Color.decode("#6d7475"),
            BG = Color.decode("#f8f7f4"),
            CREAM = Color.decode("#fcfbf8"),
            LINE = Color.decode("#e7e5e2"),
            PINK = Color.decode("#fff0f1"),
            GREEN = Color.decode("#277464"),
            MINT = Color.decode("#e8f5ef");
    private static Font base;

    private Ui() {}

    public static void setup() {
        try {
            base =
                    Font.createFont(
                            Font.TRUETYPE_FONT,
                            new File("assets/fonts/GoogleSansCode-Regular.ttf"));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .registerFont(
                            Font.createFont(
                                    Font.TRUETYPE_FONT,
                                    new File("assets/fonts/GoogleSansCode-Bold.ttf")));
        } catch (Exception e) {
            throw new IllegalStateException("Missing bundled Google Sans Code font", e);
        }
        FlatLightLaf.setup();
        UIManager.put("defaultFont", font(12));
        UIManager.put("Component.arc", 24);
        UIManager.put("Button.arc", 26);
        UIManager.put("TextComponent.arc", 22);
        UIManager.put("Button.default.borderColor", CORAL);
        UIManager.put("Button.default.focusColor", CORAL);
        UIManager.put("Component.focusColor", CORAL);
        UIManager.put("Component.accentColor", CORAL);
        UIManager.put("TabbedPane.underlineColor", CORAL);
        UIManager.put("Panel.background", BG);
        UIManager.put("Label.foreground", INK);
        UIManager.put("TextComponent.selectAllOnFocusPolicy", "never");
    }

    public static Font font(float size) {
        return (base == null ? new Font("Monospaced", Font.PLAIN, 12) : base).deriveFont(size);
    }

    public static JLabel label(String text, float size, Color color) {
        JLabel l =
                new JLabel(text) {
                    @Override
                    public Dimension getPreferredSize() {
                        Dimension d = super.getPreferredSize();
                        return new Dimension(d.width + 8, d.height + 2);
                    }

                    @Override
                    public Dimension getMaximumSize() {
                        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
                    }
                };
        l.setFont(font(size));
        l.setForeground(color);
        return l;
    }

    public static JLabel bold(String text, float size) {
        JLabel l = label(text, size, INK);
        l.setFont(font(size).deriveFont(Font.BOLD));
        return l;
    }

    public static String escape(Object text) {
        return String.valueOf(text == null ? "" : text)
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static JComponent paragraph(String text, int width) {
        return new Paragraph(text, width);
    }

    private static class Paragraph extends JComponent {
        final String text;
        final int width;

        Paragraph(String text, int width) {
            this.text = text == null ? "" : text;
            this.width = width;
            setFont(font(11));
            setForeground(MUTED);
        }

        private java.util.List<TextLayout> lines(int available) {
            java.util.List<TextLayout> lines = new java.util.ArrayList<>();
            FontRenderContext context = new FontRenderContext(null, true, true);
            AttributedString attributed = new AttributedString(text.isEmpty() ? " " : text);
            attributed.addAttribute(TextAttribute.FONT, getFont());
            LineBreakMeasurer measure = new LineBreakMeasurer(attributed.getIterator(), context);
            while (measure.getPosition() < attributed.getIterator().getEndIndex())
                lines.add(measure.nextLayout(Math.max(40, available - 4)));
            return lines;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, lines(getWidth() > 0 ? getWidth() : width).size() * 18 + 2);
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(0, getPreferredSize().height);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setColor(getForeground());
            float y = 12;
            for (TextLayout line : lines(getWidth())) {
                line.draw(g, 0, y);
                y += 18;
            }
            g.dispose();
        }
    }

    public static JPanel column() {
        JPanel p =
                new JPanel() {
                    @Override
                    public Dimension getMaximumSize() {
                        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
                    }
                };
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    public static JPanel row(Component... children) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        for (Component c : children) p.add(c);
        return p;
    }

    public static JPanel grid(int cols, Component... children) {
        JPanel p =
                new JPanel(new GridLayout(0, cols, 14, 14)) {
                    @Override
                    public Dimension getMaximumSize() {
                        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
                    }
                };
        p.setOpaque(false);
        for (Component c : children) p.add(c);
        return p;
    }

    public static void add(JPanel p, Component c) {
        if (c instanceof JComponent j) j.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(c);
    }

    public static void gap(JPanel p, int height) {
        p.add(Box.createVerticalStrut(height));
    }

    public static void stretch(JComponent c, int height) {
        c.setMinimumSize(new Dimension(0, height));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        c.setPreferredSize(new Dimension(300, height));
    }

    public static class Card extends JPanel {
        public Card() {
            this(Color.WHITE);
        }

        public Card(Color color) {
            setBackground(color);
            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(new EmptyBorder(22, 22, 22, 22));
        }

        @Override
        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            g.setColor(LINE);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 32, 32);
            g.dispose();
        }
    }

    public static Card card(String title, String subtitle) {
        Card p = new Card();
        add(p, bold(title, 15));
        gap(p, 6);
        add(p, label(subtitle, 10, MUTED));
        gap(p, 22);
        return p;
    }

    public static JPanel heading(String eyebrow, String title, String subtitle) {
        JPanel p = column();
        add(p, label(eyebrow.toUpperCase(), 11, CORAL));
        gap(p, 12);
        add(p, label(title, title.length() > 30 ? 33 : 42, INK));
        gap(p, 10);
        add(p, paragraph(subtitle, 650));
        gap(p, 30);
        return p;
    }

    public static JButton button(String text, Runnable action) {
        JButton b = new CustomButton(text);
        b.addActionListener(e -> action.run());
        return b;
    }

    public static JButton secondary(String text, Runnable action) {
        JButton b = button(text, action);
        b.setBackground(Color.WHITE);
        b.setForeground(INK);
        return b;
    }

    public static JButton iconButton(String text, String path, Runnable action) {
        JButton b = secondary(text, action);
        b.setIcon(icon(path));
        b.setIconTextGap(11);
        return b;
    }

    public static Icon icon(String relative) {
        File f = new File("assets/figma/" + relative + ".svg");
        try {
            FlatSVGIcon icon = new FlatSVGIcon(f.toURI().toURL());
            if (icon.hasFound()) return icon;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load asset " + f, e);
        }
        throw new IllegalStateException("Missing asset " + f);
    }

    public static JLabel badge(String text) {
        JLabel l =
                new JLabel(text.replace('_', ' ')) {
                    @Override
                    public Dimension getMaximumSize() {
                        return getPreferredSize();
                    }

                    @Override
                    protected void paintComponent(Graphics graphics) {
                        Graphics2D g = (Graphics2D) graphics.create();
                        g.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(getBackground());
                        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                        g.dispose();
                        super.paintComponent(graphics);
                    }
                };
        l.setFont(font(10).deriveFont(Font.BOLD));
        l.setBackground(MINT);
        l.setForeground(GREEN);
        l.setBorder(new EmptyBorder(6, 9, 6, 9));
        if (text.contains("CRITICAL") || text.contains("MEDICAL")) {
            l.setForeground(CORAL);
            l.setBackground(PINK);
        } else if (text.contains("HIGH") || text.contains("HOLD")) {
            l.setForeground(Color.decode("#9a681c"));
            l.setBackground(Color.decode("#fff3dd"));
        }
        return l;
    }

    public static JComponent score(int value) {
        return new JComponent() {
            {
                setPreferredSize(new Dimension(130, 130));
                setMaximumSize(new Dimension(130, 130));
            }

            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(MINT);
                g.fillOval(0, 0, 130, 130);
                g.setColor(CREAM);
                g.fillOval(8, 8, 114, 114);
                g.setColor(GREEN);
                g.setFont(font(35).deriveFont(Font.BOLD));
                String text = value + "%";
                g.drawString(text, (130 - g.getFontMetrics().stringWidth(text)) / 2, 65);
                g.setFont(font(9));
                String caption = "YOUR MATCH";
                g.drawString(caption, (130 - g.getFontMetrics().stringWidth(caption)) / 2, 89);
                g.dispose();
            }
        };
    }

    public static JComponent avatar(String initials) {
        return new JComponent() {
            {
                setPreferredSize(new Dimension(76, 76));
                setMaximumSize(new Dimension(76, 76));
            }

            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.WHITE);
                g.fillOval(0, 0, 76, 76);
                g.setColor(CORAL);
                g.fillOval(4, 4, 68, 68);
                g.setColor(Color.WHITE);
                g.setFont(font(20).deriveFont(Font.BOLD));
                g.drawString(initials, (76 - g.getFontMetrics().stringWidth(initials)) / 2, 46);
                g.dispose();
            }
        };
    }

    public static JComponent field(String name, JComponent input) {
        JPanel p = column();
        add(p, bold(name, 11));
        gap(p, 8);
        stretch(input, 44);
        add(p, input);
        gap(p, 16);
        return p;
    }

    public static JTextField text(String placeholder) {
        JTextField f = new CustomTextField(20);
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    public static JComboBox<String> select(String... options) {
        return new JComboBox<>(options);
    }

    private static class Viewport extends JPanel implements Scrollable {
        Viewport(JComponent component) {
            super(new BorderLayout());
            setOpaque(false);
            add(component, BorderLayout.NORTH);
        }

        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
            return 22;
        }

        public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
            return Math.max(22, visible.height - 22);
        }

        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    public static JScrollPane scroll(JComponent component) {
        JScrollPane s = new JScrollPane(new Viewport(component));
        s.setBorder(null);
        s.getViewport().setOpaque(false);
        s.setOpaque(false);
        s.getVerticalScrollBar().setUnitIncrement(22);
        s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return s;
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent, message, "Unable to complete action", JOptionPane.ERROR_MESSAGE);
    }

    public static <T> void async(Component owner, Callable<T> task, Consumer<T> done) {
        new SwingWorker<T, Void>() {
            protected T doInBackground() throws Exception {
                return task.call();
            }

            protected void done() {
                try {
                    done.accept(get());
                } catch (Exception e) {
                    Throwable c = e.getCause() == null ? e : e.getCause();
                    error(
                            owner,
                            "The operation could not be completed. Check MySQL is running and try again.\n"
                                    + c.getMessage());
                }
            }
        }.execute();
    }

    public static void onChange(JTextField field, Runnable action) {
        field.getDocument()
                .addDocumentListener(
                        new DocumentListener() {
                            public void insertUpdate(DocumentEvent e) {
                                action.run();
                            }

                            public void removeUpdate(DocumentEvent e) {
                                action.run();
                            }

                            public void changedUpdate(DocumentEvent e) {
                                action.run();
                            }
                        });
    }

    public static void save(JButton button, Callable<Boolean> task, Runnable success) {
        button.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            protected Boolean doInBackground() throws Exception {
                return task.call();
            }

            protected void done() {
                try {
                    if (get()) success.run();
                    else error(button, "Nothing was saved. Refresh the page and try again.");
                } catch (Exception e) {
                    error(
                            button,
                            "Could not save. Your changes were not confirmed.\n"
                                    + (e.getCause() == null
                                            ? e.getMessage()
                                            : e.getCause().getMessage()));
                } finally {
                    button.setEnabled(true);
                }
            }
        }.execute();
    }

    /** Data-supplied animal images retain their database paths. */
    public static JComponent photo(String path, int width, int height) {
        return new JComponent() {
            BufferedImage image = read();

            private BufferedImage read() {
                try {
                    return path == null ? null : ImageIO.read(new File(path));
                } catch (Exception e) {
                    return null;
                }
            }

            {
                setPreferredSize(new Dimension(width, height));
                setMinimumSize(new Dimension(width, height));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
            }

            protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g.setColor(MINT);
                g.fillRect(0, 0, getWidth(), getHeight());
                if (image != null) {
                    double scale =
                            Math.max(
                                    (double) getWidth() / image.getWidth(),
                                    (double) getHeight() / image.getHeight());
                    int w = (int) (image.getWidth() * scale), h = (int) (image.getHeight() * scale);
                    g.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.drawImage(image, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, null);
                } else {
                    g.setColor(MUTED);
                    g.setFont(font(10));
                    String t = "Photo unavailable";
                    g.drawString(
                            t,
                            Math.max(5, (getWidth() - g.getFontMetrics().stringWidth(t)) / 2),
                            getHeight() / 2);
                }
                g.dispose();
            }
        };
    }
}
