package com.example.hospital.dao;

import com.example.hospital.model.Insurance;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsuranceDAO {

    // CREATE
    public void insertInsurance(Insurance insurance) {
        String sql = "INSERT INTO insurance (insurance_id, company, address, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, insurance.getInsuranceId());
            stmt.setString(2, insurance.getCompany());
            stmt.setString(3, insurance.getAddress());
            stmt.setString(4, insurance.getPhone());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public Insurance getInsuranceById(int id) {
        Insurance insurance = null;
        String sql = "SELECT * FROM insurance WHERE insurance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    insurance = new Insurance(
                        rs.getInt("insurance_id"),
                        rs.getString("company"),
                        rs.getString("address"),
                        rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insurance;
    }

    // UPDATE
    public void updateInsurance(Insurance insurance) {
        String sql = "UPDATE insurance SET company=?, address=?, phone=? WHERE insurance_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, insurance.getCompany());
            stmt.setString(2, insurance.getAddress());
            stmt.setString(3, insurance.getPhone());
            stmt.setInt(4, insurance.getInsuranceId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteInsurance(int id) {
        String sql = "DELETE FROM insurance WHERE insurance_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Insurance> getAllInsuranceRecords() {
        List<Insurance> list = new ArrayList<>();
        String sql = "SELECT * FROM insurance";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Insurance insurance = new Insurance(
                    rs.getInt("insurance_id"),
                    rs.getString("company"),
                    rs.getString("address"),
                    rs.getString("phone")
                );
                list.add(insurance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
