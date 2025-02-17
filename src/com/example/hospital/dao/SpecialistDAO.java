package com.example.hospital.dao;

import com.example.hospital.model.Specialist;
import com.example.hospital.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialistDAO {

    // CREATE
    public void insertSpecialist(Specialist specialist) {
        String sql = "INSERT INTO specialists (specialist_id, first_name, surname, address, email, specialization, experience) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, specialist.getDoctorId());
            stmt.setString(2, specialist.getFirstName());
            stmt.setString(3, specialist.getSurname());
            stmt.setString(4, specialist.getAddress());
            stmt.setString(5, specialist.getEmail());
            stmt.setString(6, specialist.getSpecialization());
            stmt.setInt(7, specialist.getYearsOfExperience());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    public Specialist getSpecialistById(int id) {
        Specialist specialist = null;
        String sql = "SELECT * FROM specialists WHERE specialist_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    specialist = new Specialist(
                        rs.getInt("specialist_id"),
                        rs.getString("first_name"),
                        rs.getString("surname"),
                        rs.getString("address"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getInt("experience")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialist;
    }

    // UPDATE
    public void updateSpecialist(Specialist specialist) {
        String sql = "UPDATE specialists SET first_name=?, surname=?, address=?, email=?, specialization=?, experience=? "
                   + "WHERE specialist_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, specialist.getFirstName());
            stmt.setString(2, specialist.getSurname());
            stmt.setString(3, specialist.getAddress());
            stmt.setString(4, specialist.getEmail());
            stmt.setString(5, specialist.getSpecialization());
            stmt.setInt(6, specialist.getYearsOfExperience());
            stmt.setInt(7, specialist.getDoctorId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteSpecialist(int id) {
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
        String sql = "SELECT * FROM specialists";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Specialist sp = new Specialist(
                    rs.getInt("specialist_id"),
                    rs.getString("first_name"),
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
