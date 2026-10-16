package com.pawth.config;

import java.awt.Color;
import java.awt.Font;

public class SystemConstants {
    
    // Application Metadata
    public static final String APP_NAME = "PAWTH - Stray Animal Rescue & Adoption";
    
    // PAWTH Theme Colors (Extracted from UI Prototype)
    public static final Color COLOR_PRIMARY = new Color(255, 90, 95);      // Coral Accent
    public static final Color COLOR_PRIMARY_HOVER = new Color(230, 75, 80);
    public static final Color COLOR_BG_LIGHT = new Color(248, 249, 250);     // App Background
    public static final Color COLOR_CARD_BG = new Color(255, 255, 255);     // Card Background
    public static final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    public static final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    public static final Color COLOR_BORDER = new Color(222, 226, 230);
    
    // Status Badge Colors
    public static final Color BADGE_CRITICAL = new Color(220, 53, 69);
    public static final Color BADGE_HIGH = new Color(255, 193, 7);
    public static final Color BADGE_MEDIUM = new Color(23, 162, 184);
    public static final Color BADGE_SUCCESS = new Color(40, 167, 69);

    // Default Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
}