package com.example.hospital.dao;

import com.example.hospital.model.Drug;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Prescription;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // CREATE
    public void insertPrescription(Prescription prescription) {
        String sql = "INSERT INTO prescriptions (prescription_id, date_of_prescribe, dosage, duration, comment, patient_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescription.getPrescriptionId());
            stmt.setDate(2, Date.valueOf(prescription.getDateOfPrescribe()));
            stmt.setString(3, prescription.getDosage());
            stmt.setString(4, prescription.getDuration());
            stmt.setString(5, prescription.getComment());
            stmt.setInt(6, prescription.getPatient().getPatientId());
            stmt.executeUpdate();

            // Now insert into the bridging table for each drug
            insertPrescriptionDrugs(prescription.getPrescriptionId(), prescription.getDrugs());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertPrescriptionDrugs(int prescriptionId, List<Drug> drugs) {
        String sql = "INSERT INTO prescription_drugs (prescription_id, drug_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Drug drug : drugs) {
                stmt.setInt(1, prescriptionId);
                stmt.setInt(2, drug.getDrugId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public Prescription getPrescriptionById(int id) {
        Prescription prescription = null;
        String sql = "SELECT * FROM prescriptions WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Minimal approach: fetch patient from a placeholder
                    Patient patientPlaceholder = new Patient(rs.getInt("patient_id"), "", "");
                    prescription = new Prescription(
                        rs.getInt("prescription_id"),
                        rs.getDate("date_of_prescribe").toLocalDate(),
                        rs.getString("dosage"),
                        rs.getString("duration"),
                        rs.getString("comment"),
                        patientPlaceholder,
                        new ArrayList<>()
                    );
                }
            }
            // Load associated drugs
            if (prescription != null) {
                prescription.setDrugs(getDrugsForPrescription(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescription;
    }

    // Retrieve associated drugs from bridging table
    private List<Drug> getDrugsForPrescription(int prescriptionId) {
        List<Drug> drugs = new ArrayList<>();
        String sql = "SELECT d.drug_id, d.name, d.side_effects, d.benefits "
                   + "FROM prescription_drugs pd "
                   + "JOIN drugs d ON pd.drug_id = d.drug_id "
                   + "WHERE pd.prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescriptionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Drug drug = new Drug(
                        rs.getInt("drug_id"),
                        rs.getString("name"),
                        rs.getString("side_effects"),
                        rs.getString("benefits")
                    );
                    drugs.add(drug);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drugs;
    }

    // UPDATE
    public void updatePrescription(Prescription prescription) {
        String sql = "UPDATE prescriptions SET date_of_prescribe=?, dosage=?, duration=?, comment=?, patient_id=? "
                   + "WHERE prescription_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(prescription.getDateOfPrescribe()));
            stmt.setString(2, prescription.getDosage());
            stmt.setString(3, prescription.getDuration());
            stmt.setString(4, prescription.getComment());
            stmt.setInt(5, prescription.getPatient().getPatientId());
            stmt.setInt(6, prescription.getPrescriptionId());
            stmt.executeUpdate();

            // Update bridging table: simplest approach is to delete all current rows and re-insert
            deletePrescriptionDrugs(prescription.getPrescriptionId());
            insertPrescriptionDrugs(prescription.getPrescriptionId(), prescription.getDrugs());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deletePrescriptionDrugs(int prescriptionId) {
        String sql = "DELETE FROM prescription_drugs WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescriptionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deletePrescription(int id) {
        // Must delete bridging table rows first
        deletePrescriptionDrugs(id);

        String sql = "DELETE FROM prescriptions WHERE prescription_id = ?";
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
                Patient patientPlaceholder = new Patient(rs.getInt("patient_id"), "", "");
                Prescription prescription = new Prescription(
                    rs.getInt("prescription_id"),
                    rs.getDate("date_of_prescribe").toLocalDate(),
                    rs.getString("dosage"),
                    rs.getString("duration"),
                    rs.getString("comment"),
                    patientPlaceholder,
                    // We'll fill the drug list in a moment
                    new ArrayList<>()
                );
                // Load associated drugs
                prescription.setDrugs(getDrugsForPrescription(prescription.getPrescriptionId()));
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }
}
