package com.pawth.service;

import com.pawth.config.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsService {

    public Map<String, Integer> getHotspotCountsByBarangay() {
        Map<String, Integer> hotspots = new HashMap<>();
        String sql = "SELECT barangay, COUNT(*) AS total FROM rescue_cases GROUP BY barangay ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                hotspots.put(rs.getString("barangay"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotspots;
    }
}