package com.example.hospital.dao;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Visit;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitDAO {

    // CREATE
    public void insertVisit(Visit visit) {
        String sql = "INSERT INTO visits (visit_id, date_of_visit, symptoms, diagnosis, doctor_id, patient_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, visit.getVisitId());
            stmt.setDate(2, Date.valueOf(visit.getDateOfVisit()));
            stmt.setString(3, visit.getSymptoms());
            stmt.setString(4, visit.getDiagnosis());
            stmt.setInt(5, visit.getDoctor().getDoctorId());
            stmt.setInt(6, visit.getPatient().getPatientId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public Visit getVisitById(int id) {
        Visit visit = null;
        String sql = "SELECT * FROM visits WHERE visit_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // In a real system, you’d retrieve the Doctor and Patient from their DAOs
                    Doctor doctorPlaceholder = new Doctor(rs.getInt("doctorid"), "", "", "", "");
                    Patient patientPlaceholder = new Patient(rs.getInt("patientid"), "", "");

                    visit = new Visit(
                        rs.getInt("visit_id"),
                        rs.getDate("dateofvisit").toLocalDate(),
                        rs.getString("symptoms"),
                        rs.getString("diagnosis"),
                        doctorPlaceholder,
                        patientPlaceholder
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visit;
    }

    // UPDATE
    public void updateVisit(Visit visit) {
        String sql = "UPDATE visits SET date_of_visit=?, symptoms=?, diagnosis=?, doctor_id=?, patient_id=? "
                   + "WHERE visit_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(visit.getDateOfVisit()));
            stmt.setString(2, visit.getSymptoms());
            stmt.setString(3, visit.getDiagnosis());
            stmt.setInt(4, visit.getDoctor().getDoctorId());
            stmt.setInt(5, visit.getPatient().getPatientId());
            stmt.setInt(6, visit.getVisitId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteVisit(int id) {
        String sql = "DELETE FROM visits WHERE visit_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Visit> getAllVisits() {
        List<Visit> visits = new ArrayList<>();
        String sql = "SELECT * FROM visits";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Minimal placeholders; normally you'd fetch from DoctorDAO/PatientDAO
                Doctor doctorPlaceholder = new Doctor(rs.getInt("doctorid"), "", "", "", "");
                Patient patientPlaceholder = new Patient(rs.getInt("patientid"), "", "");

                Visit visit = new Visit(
                    rs.getInt("visit_id"),
                    rs.getDate("dateofvisit").toLocalDate(),
                    rs.getString("symptoms"),
                    rs.getString("diagnosis"),
                    doctorPlaceholder,
                    patientPlaceholder
                );
                visits.add(visit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visits;
    }
}
