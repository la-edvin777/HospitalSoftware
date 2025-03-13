package com.example.hospital.dao;

import com.example.hospital.model.Specialist;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialistDAO {

    /**
     * Insert a Specialist into two tables:
     * 1) Update/insert the base 'doctors' row for the core fields
     * 2) Insert into 'specialists' for the extra 'experience' field
     *
     * If you already inserted the base Doctor row elsewhere, you only do step 2.
     */
    public void insertSpecialist(Specialist specialist) {
        // 1) Insert/Update base doctor row
        insertOrUpdateDoctorBase(specialist);

        // 2) Insert into specialists (just the specialist_id + experience)
        String sqlSpec = "INSERT INTO specialists (specialist_id, experience) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSpec)) {

            stmt.setInt(1, specialist.getDoctorId());
            stmt.setInt(2, specialist.getYearsOfExperience());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * For table-per-subclass, you typically have a 'doctors' table for base fields,
     * so we either do an upsert or separate code to ensure the base fields exist in 'doctors'.
     */
    private void insertOrUpdateDoctorBase(Specialist s) {
        // You can do an "INSERT IGNORE" or check if row exists, or an ON DUPLICATE KEY approach, etc.
        String sql = "INSERT INTO doctors (doctor_id, firstname, surname, address, email, specialization) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + " firstname=?, surname=?, address=?, email=?, specialization=?";

        // We store specialization in the doctors table too (some designs keep it in specialists),
        // but let's assume it remains a base field from "Doctor.csv."
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, s.getDoctorId());
            ps.setString(2, s.getFirstName());
            ps.setString(3, s.getSurname());
            ps.setString(4, s.getAddress());
            ps.setString(5, s.getEmail());
            ps.setString(6, s.getSpecialization());

            // For the ON DUPLICATE KEY part
            ps.setString(7, s.getFirstName());
            ps.setString(8, s.getSurname());
            ps.setString(9, s.getAddress());
            ps.setString(10, s.getEmail());
            ps.setString(11, s.getSpecialization());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ single
    public Specialist getSpecialistById(int id) {
        Specialist specialist = null;
        // We must JOIN doctors + specialists to get all columns
        String sql = "SELECT d.doctor_id, d.firstname, d.surname, d.address, d.email, d.specialization, s.experience "
                + "FROM specialists s "
                + "JOIN doctors d ON s.specialist_id = d.doctor_id "
                + "WHERE s.specialist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    specialist = new Specialist(
                            rs.getInt("doctor_id"),           // specialist_id
                            rs.getString("firstname"),        // from doctors
                            rs.getString("surname"),
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getString("specialization"),
                            rs.getInt("experience")           // from specialists
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialist;
    }

    /**
     * Update means update doctors + specialists
     */
    public void updateSpecialist(Specialist specialist) {
        // 1) Update base doctors row
        String sqlDoc = "UPDATE doctors SET firstname=?, surname=?, address=?, email=?, specialization=? "
                + "WHERE doctor_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlDoc)) {

            stmt.setString(1, specialist.getFirstName());
            stmt.setString(2, specialist.getSurname());
            stmt.setString(3, specialist.getAddress());
            stmt.setString(4, specialist.getEmail());
            stmt.setString(5, specialist.getSpecialization());
            stmt.setInt(6, specialist.getDoctorId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 2) Update 'specialists' row
        String sqlSpec = "UPDATE specialists SET experience=? WHERE specialist_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSpec)) {

            stmt.setInt(1, specialist.getYearsOfExperience());
            stmt.setInt(2, specialist.getDoctorId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteSpecialist(int id) {
        // Deleting from specialists is enough, because doctor is your base.
        // Or you might want to also remove from doctors if there's no more usage, but typically you only remove the subclass row.
        // If ON DELETE CASCADE is set, removing from specialists alone might not remove from doctors.
        // So decide your business logic carefully.
        String sql = "DELETE FROM specialists WHERE specialist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIST ALL
    public List<Specialist> getAllSpecialists() {
        List<Specialist> specialists = new ArrayList<>();
        // Again, join specialists + doctors
        String sql = "SELECT d.doctor_id, d.firstname, d.surname, d.address, d.email, d.specialization, s.experience "
                + "FROM specialists s "
                + "JOIN doctors d ON s.specialist_id = d.doctor_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Specialist sp = new Specialist(
                        rs.getInt("doctor_id"),
                        rs.getString("firstname"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getInt("experience")
                );
                specialists.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialists;
    }
}
