package com.pawth.dao;

import com.pawth.config.DatabaseConnection;
import com.pawth.model.AdopterProfile;
import com.pawth.model.StaffProfile;
import com.pawth.model.User;
import java.sql.*;

public class UserDAO {

    public User authenticate(String email, String password, String selectedWorkspace) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                String roleStr = rs.getString("role");

                // Handles both plain-text seeds and hashed passwords
                if (com.pawth.util.PasswordHandler.verifyPassword(password, storedPassword)) {
                    if (!roleStr.equalsIgnoreCase(selectedWorkspace)) {
                        System.out.println(
                                "Workspace Mismatch: Selected "
                                        + selectedWorkspace
                                        + " but account is "
                                        + roleStr);
                        return null;
                    }

                    User user =
                            new User(
                                    rs.getInt("user_id"),
                                    rs.getString("email"),
                                    storedPassword,
                                    rs.getString("full_name"),
                                    rs.getString("phone"),
                                    rs.getString("barangay"),
                                    User.Role.valueOf(roleStr));

                    if (user.getRole() == User.Role.COMMUNITY) {
                        user.setAdopterProfile(getAdopterProfile(user.getUserId(), conn));
                    } else {
                        user.setStaffProfile(getStaffProfile(user.getUserId(), conn));
                    }

                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Connection or Query Error: " + e.getMessage());
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
        return null;
    }

    public AdopterProfile getAdopterProfile(int userId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM adopter_profiles WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new AdopterProfile(
                        userId,
                        rs.getString("home_type"),
                        rs.getBoolean("has_children"),
                        rs.getBoolean("has_other_pets"),
                        rs.getString("preferred_animal"),
                        rs.getString("pet_experience"));
            }
        }
        return null;
    }

    public StaffProfile getStaffProfile(int userId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM staff_profiles WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new StaffProfile(
                        userId,
                        rs.getString("staff_custom_id"),
                        rs.getString("shelter_name"),
                        rs.getString("position"),
                        rs.getString("shift_schedule"),
                        rs.getInt("cases_handled"),
                        rs.getInt("rescues_count"));
            }
        }
        return null;
    }
}
