package com.pawth.dao;

import com.pawth.config.DatabaseConnection;
import com.pawth.model.AdoptionApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdoptionApplicationDAO {

    public boolean submitApplication(AdoptionApplication app) {
        String sql =
                "INSERT INTO adoption_applications (applicant_id, animal_id, match_percentage, application_status) "
                        + "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, app.getApplicantId());
            stmt.setInt(2, app.getAnimalId());
            stmt.setInt(3, app.getMatchPercentage());
            stmt.setString(4, app.getStatus().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
    }

    public List<AdoptionApplication> getPendingQueue() {
        List<AdoptionApplication> list = new ArrayList<>();
        String sql =
                "SELECT a.*, u.full_name, an.name AS animal_name, an.species AS animal_species FROM adoption_applications a "
                        + "JOIN users u ON a.applicant_id = u.user_id "
                        + "JOIN animals an ON a.animal_id = an.animal_id "
                        + "ORDER BY a.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AdoptionApplication app = new AdoptionApplication();
                app.setApplicationId(rs.getInt("application_id"));
                app.setApplicantId(rs.getInt("applicant_id"));
                app.setAnimalId(rs.getInt("animal_id"));
                app.setAnimalSpecies(rs.getString("animal_species"));
                app.setCreatedAt(rs.getTimestamp("created_at"));
                app.setApplicantName(rs.getString("full_name"));
                app.setAnimalName(rs.getString("animal_name"));
                app.setMatchPercentage(rs.getInt("match_percentage"));
                app.setStatus(
                        AdoptionApplication.ApplicationStatus.valueOf(
                                rs.getString("application_status")));
                list.add(app);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
        return list;
    }
}
