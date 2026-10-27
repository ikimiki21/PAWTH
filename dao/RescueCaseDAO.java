package com.pawth.dao;

import com.pawth.config.DatabaseConnection;
import com.pawth.model.RescueCase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RescueCaseDAO {

    public boolean createReport(RescueCase caseData) {
        String sql =
                "INSERT INTO rescue_cases (reporter_id, animal_type, approximate_size, location_landmark, "
                        + "barangay, area_street, photo_url, condition_flags, additional_notes, priority_level, priority_score) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, caseData.getReporterId());
            stmt.setString(2, caseData.getAnimalType());
            stmt.setString(3, caseData.getApproximateSize());
            stmt.setString(4, caseData.getLocationLandmark());
            stmt.setString(5, caseData.getBarangay());
            stmt.setString(6, caseData.getAreaStreet());
            stmt.setString(7, caseData.getPhotoUrl());
            stmt.setString(8, caseData.getConditionFlags());
            stmt.setString(9, caseData.getAdditionalNotes());
            stmt.setString(10, caseData.getPriorityLevel().name());
            stmt.setInt(11, caseData.getPriorityScore());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
    }

    public List<RescueCase> getAllActiveCases() {
        List<RescueCase> cases = new ArrayList<>();
        String sql =
                "SELECT r.*, u.full_name AS reporter_name, h.full_name AS handler_name FROM rescue_cases r "
                        + "LEFT JOIN users u ON r.reporter_id = u.user_id "
                        + "LEFT JOIN users h ON r.assigned_handler_id = h.user_id ORDER BY r.priority_score DESC, r.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                RescueCase rc = new RescueCase();
                rc.setCaseId(rs.getInt("case_id"));
                rc.setReporterId(rs.getInt("reporter_id"));
                rc.setAnimalType(rs.getString("animal_type"));
                rc.setLocationLandmark(rs.getString("location_landmark"));
                rc.setBarangay(rs.getString("barangay"));
                rc.setConditionFlags(rs.getString("condition_flags"));
                rc.setPriorityLevel(
                        RescueCase.PriorityLevel.valueOf(rs.getString("priority_level")));
                rc.setPriorityScore(rs.getInt("priority_score"));
                rc.setCaseStatus(RescueCase.CaseStatus.valueOf(rs.getString("case_status")));
                rc.setReporterName(rs.getString("reporter_name"));
                rc.setApproximateSize(rs.getString("approximate_size"));
                rc.setAreaStreet(rs.getString("area_street"));
                rc.setPhotoUrl(rs.getString("photo_url"));
                rc.setAdditionalNotes(rs.getString("additional_notes"));
                rc.setAssignedHandlerId((Integer) rs.getObject("assigned_handler_id"));
                rc.setAssignedHandlerName(rs.getString("handler_name"));
                rc.setHandlerNotes(rs.getString("handler_notes"));
                rc.setCreatedAt(rs.getTimestamp("created_at"));
                cases.add(rc);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
        return cases;
    }

    public boolean updateCaseStatus(
            int caseId, RescueCase.CaseStatus status, Integer handlerId, String notes) {
        String sql =
                "UPDATE rescue_cases SET case_status = ?, assigned_handler_id = ?, handler_notes = ? WHERE case_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            if (handlerId != null) stmt.setInt(2, handlerId);
            else stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, notes);
            stmt.setInt(4, caseId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Database operation failed. Check the local MySQL connection.", e);
        }
    }
}
