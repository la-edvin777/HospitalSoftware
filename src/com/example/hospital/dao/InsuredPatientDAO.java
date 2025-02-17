package com.example.hospital.dao;

import com.example.hospital.model.InsuredPatient;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsuredPatientDAO {

    // CREATE
    public void insertInsuredPatient(InsuredPatient ip) {
        String sql = "INSERT INTO insured_patients (patient_id, first_name, surname, postcode, address, phone, email, "
                   + "insurance_type, insurance_company, duration_of_insurance) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ip.getPatientId());
            stmt.setString(2, ip.getFirstName());
            stmt.setString(3, ip.getSurname());
            stmt.setString(4, ip.getPostcode());
            stmt.setString(5, ip.getAddress());
            stmt.setString(6, ip.getPhone());
            stmt.setString(7, ip.getEmail());
            stmt.setString(8, ip.getInsuranceType());
            stmt.setString(9, ip.getInsuranceCompanyName());
            stmt.setInt(10, ip.getDurationOfInsurance());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public InsuredPatient getInsuredPatientById(int id) {
        InsuredPatient ip = null;
        String sql = "SELECT * FROM insured_patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ip = new InsuredPatient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("postcode"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("insurance_type"),
                        rs.getString("insurance_company"),
                        rs.getInt("duration_of_insurance")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ip;
    }

    // UPDATE
    public void updateInsuredPatient(InsuredPatient ip) {
        String sql = "UPDATE insured_patients SET first_name=?, surname=?, postcode=?, address=?, phone=?, email=?, "
                   + "insurance_type=?, insurance_company=?, duration_of_insurance=? WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ip.getFirstName());
            stmt.setString(2, ip.getSurname());
            stmt.setString(3, ip.getPostcode());
            stmt.setString(4, ip.getAddress());
            stmt.setString(5, ip.getPhone());
            stmt.setString(6, ip.getEmail());
            stmt.setString(7, ip.getInsuranceType());
            stmt.setString(8, ip.getInsuranceCompanyName());
            stmt.setInt(9, ip.getDurationOfInsurance());
            stmt.setInt(10, ip.getPatientId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteInsuredPatient(int id) {
        String sql = "DELETE FROM insured_patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<InsuredPatient> getAllInsuredPatients() {
        List<InsuredPatient> insuredPatients = new ArrayList<>();
        String sql = "SELECT * FROM insured_patients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                InsuredPatient ip = new InsuredPatient(
                    rs.getInt("patient_id"),
                    rs.getString("first_name"),
                    rs.getString("surname"),
                    rs.getString("postcode"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("insurance_type"),
                    rs.getString("insurance_company"),
                    rs.getInt("duration_of_insurance")
                );
                insuredPatients.add(ip);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return insuredPatients;
    }
}
