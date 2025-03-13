package com.example.hospital.dao;

import com.example.hospital.model.Patient;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // CREATE
    public void insertPatient(Patient patient) {
        String sql = "INSERT INTO patients (patient_id, first_name, surname, postcode, address, phone, email) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patient.getPatientId());
            stmt.setString(2, patient.getFirstName());
            stmt.setString(3, patient.getSurname());
            stmt.setString(4, patient.getPostcode());
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getPhone());
            stmt.setString(7, patient.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public Patient getPatientById(int id) {
        Patient patient = null;
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("firstname"),
                        rs.getString("surname"),
                        rs.getString("postcode"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    // UPDATE
    public void updatePatient(Patient patient) {
        String sql = "UPDATE patients SET first_name=?, surname=?, postcode=?, address=?, phone=?, email=? "
                   + "WHERE patient_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getSurname());
            stmt.setString(3, patient.getPostcode());
            stmt.setString(4, patient.getAddress());
            stmt.setString(5, patient.getPhone());
            stmt.setString(6, patient.getEmail());
            stmt.setInt(7, patient.getPatientId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("patient_id"),
                    rs.getString("firstname"),
                    rs.getString("surname"),
                    rs.getString("postcode"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }
}
