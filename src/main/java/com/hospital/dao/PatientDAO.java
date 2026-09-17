package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public void savePatient(Patient patient) {
        String query = "INSERT INTO patients (name, urgency_score, bed_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getUrgencyScore());
            if (patient.getBedId() != null) {
                pstmt.setInt(3, patient.getBedId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    patient.setPatientId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Error saving patient: " + e.getMessage());
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");
                int urgency = rs.getInt("urgency_score");
                int bedIdVal = rs.getInt("bed_id");
                Integer bedId = rs.wasNull() ? null : bedIdVal;

                patients.add(new Patient(id, name, urgency, bedId));
            }
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Error fetching patients: " + e.getMessage());
        }

        return patients;
    }
}
