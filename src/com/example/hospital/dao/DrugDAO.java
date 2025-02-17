package com.example.hospital.dao;

import com.example.hospital.model.Drug;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrugDAO {

    // CREATE
    public void insertDrug(Drug drug) {
        String sql = "INSERT INTO drugs (drug_id, name, side_effects, benefits) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, drug.getDrugId());
            stmt.setString(2, drug.getName());
            stmt.setString(3, drug.getSideEffects());
            stmt.setString(4, drug.getBenefits());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Single)
    public Drug getDrugById(int id) {
        Drug drug = null;
        String sql = "SELECT * FROM drugs WHERE drug_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    drug = new Drug(
                        rs.getInt("drug_id"),
                        rs.getString("name"),
                        rs.getString("side_effects"),
                        rs.getString("benefits")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drug;
    }

    // UPDATE
    public void updateDrug(Drug drug) {
        String sql = "UPDATE drugs SET name=?, side_effects=?, benefits=? WHERE drug_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, drug.getName());
            stmt.setString(2, drug.getSideEffects());
            stmt.setString(3, drug.getBenefits());
            stmt.setInt(4, drug.getDrugId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteDrug(int id) {
        String sql = "DELETE FROM drugs WHERE drug_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Drug> getAllDrugs() {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT * FROM drugs";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Drug drug = new Drug(
                    rs.getInt("drug_id"),
                    rs.getString("name"),
                    rs.getString("side_effects"),
                    rs.getString("benefits")
                );
                drugs.add(drug);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drugs;
    }
}
