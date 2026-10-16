package com.pawth.util;

import java.awt.*;
import javax.swing.*;

public class ImageUtil {

    public static ImageIcon loadScaledImage(String path, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Failed to load asset at path: " + path);
            return null;
        }
    }
}