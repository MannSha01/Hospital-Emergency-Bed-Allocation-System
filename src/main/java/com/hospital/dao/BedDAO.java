package com.hospital.dao;

import com.hospital.config.DatabaseConfig;
import com.hospital.model.Bed;
import com.hospital.model.GeneralBed;
import com.hospital.model.ICUBed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class BedDAO {

    public Map<Integer, Bed> getAllBeds() {
        Map<Integer, Bed> bedMap = new HashMap<>();
        String query = "SELECT * FROM beds";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int bedId = rs.getInt("bed_id");
                String bedType = rs.getString("bed_type");
                boolean isOccupied = rs.getBoolean("is_occupied");

                Bed bed;
                if ("ICU".equalsIgnoreCase(bedType)) {
                    bed = new ICUBed(bedId, isOccupied, true);
                } else {
                    bed = new GeneralBed(bedId, isOccupied, 1);
                }
                bedMap.put(bedId, bed);
            }
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Error fetching beds: " + e.getMessage());
        }

        return bedMap;
    }

    public void updateBedStatus(int bedId, boolean isOccupied) {
        String query = "UPDATE beds SET is_occupied = ? WHERE bed_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBoolean(1, isOccupied);
            pstmt.setInt(2, bedId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Error updating bed status: " + e.getMessage());
        }
    }
}
