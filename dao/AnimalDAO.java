package com.pawth.dao;

import com.pawth.config.DatabaseConnection;
import com.pawth.model.Animal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    public List<Animal> getAllAnimals() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * FROM animals ORDER BY animal_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                animals.add(mapResultSetToAnimal(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return animals;
    }

    public boolean updateStatus(int animalId, Animal.AdoptionStatus status) {
        String sql = "UPDATE animals SET adoption_status = ? WHERE animal_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, animalId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Animal mapResultSetToAnimal(ResultSet rs) throws SQLException {
        return new Animal(
            rs.getInt("animal_id"),
            rs.getString("name"),
            rs.getString("species"),
            rs.getString("breed"),
            rs.getInt("age_years"),
            rs.getString("gender"),
            rs.getString("health_status"),
            rs.getString("visit_schedule"),
            Animal.AdoptionStatus.valueOf(rs.getString("adoption_status")),
            rs.getString("photo_url"),
            rs.getString("personality_traits")
        );
    }
}