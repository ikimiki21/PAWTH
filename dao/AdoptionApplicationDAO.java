package com.pawth.dao;

import com.pawth.config.DatabaseConnection;
import com.pawth.model.AdoptionApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptionApplicationDAO {

    public boolean submitApplication(AdoptionApplication app) {
        String sql = "INSERT INTO adoption_applications (applicant_id, animal_id, match_percentage, application_status) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, app.getApplicantId());
            stmt.setInt(2, app.getAnimalId());
            stmt.setInt(3, app.getMatchPercentage());
            stmt.setString(4, app.getStatus().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AdoptionApplication> getPendingQueue() {
        List<AdoptionApplication> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, an.name AS animal_name FROM adoption_applications a " +
                     "JOIN users u ON a.applicant_id = u.user_id " +
                     "JOIN animals an ON a.animal_id = an.animal_id " +
                     "ORDER BY a.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AdoptionApplication app = new AdoptionApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setApplicantName(rs.getString("full_name"));
                app.setAnimalName(rs.getString("animal_name"));
                app.setMatchPercentage(rs.getInt("match_percentage"));
                app.setStatus(AdoptionApplication.ApplicationStatus.valueOf(rs.getString("application_status")));
                list.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}