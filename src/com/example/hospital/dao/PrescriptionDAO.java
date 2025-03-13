package com.example.hospital.dao;

import com.example.hospital.model.Prescription;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // CREATE
    public void insertPrescription(Prescription prescription) {
        // columns: (prescriptionID, dateprescribed, dosage, duration, comment, doctorID, patientID, drugID)
        String sql = "INSERT INTO prescriptions (prescriptionID, dateprescribed, dosage, duration, comment, doctorID, patientID, drugID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescription.getPrescriptionId());
            stmt.setDate(2, Date.valueOf(prescription.getDateOfPrescribe()));
            stmt.setString(3, prescription.getDosage());
            stmt.setString(4, prescription.getDuration());
            stmt.setString(5, prescription.getComment());
            stmt.setInt(6, prescription.getDoctorId());
            stmt.setInt(7, prescription.getPatientId());
            stmt.setInt(8, prescription.getDrugId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ single
    public Prescription getPrescriptionById(int id) {
        Prescription prescription = null;
        String sql = "SELECT * FROM prescriptions WHERE prescriptionID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    prescription = new Prescription(
                            rs.getInt("prescriptionID"),
                            rs.getDate("dateprescribed").toLocalDate(),
                            rs.getString("dosage"),
                            rs.getString("duration"),
                            rs.getString("comment"),
                            rs.getInt("doctorID"),
                            rs.getInt("patientID"),
                            rs.getInt("drugID")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescription;
    }

    // UPDATE
    public void updatePrescription(Prescription prescription) {
        String sql = "UPDATE prescriptions "
                + "SET dateprescribed=?, dosage=?, duration=?, comment=?, doctorID=?, patientID=?, drugID=? "
                + "WHERE prescriptionID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(prescription.getDateOfPrescribe()));
            stmt.setString(2, prescription.getDosage());
            stmt.setString(3, prescription.getDuration());
            stmt.setString(4, prescription.getComment());
            stmt.setInt(5, prescription.getDoctorId());
            stmt.setInt(6, prescription.getPatientId());
            stmt.setInt(7, prescription.getDrugId());
            stmt.setInt(8, prescription.getPrescriptionId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePrescription(int id) {
        String sql = "DELETE FROM prescriptions WHERE prescriptionID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Prescription> getAllPrescriptions() {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Prescription prescription = new Prescription(
                        rs.getInt("prescriptionID"),
                        rs.getDate("dateprescribed").toLocalDate(),
                        rs.getString("dosage"),
                        rs.getString("duration"),
                        rs.getString("comment"),
                        rs.getInt("doctorID"),
                        rs.getInt("patientID"),
                        rs.getInt("drugID")
                );
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }
}
