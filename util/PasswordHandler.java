package com.pawth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordHandler {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean verifyPassword(String rawPassword, String storedHash) {
        return hashPassword(rawPassword).equalsIgnoreCase(storedHash) || rawPassword.equals(storedHash);
    }
}