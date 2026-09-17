package com.hospital.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:h2:./triage_db;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initializeDatabase() {
        String createBedsTable = "CREATE TABLE IF NOT EXISTS beds (" +
                "bed_id INT PRIMARY KEY, " +
                "bed_type VARCHAR(20) NOT NULL, " +
                "is_occupied BOOLEAN DEFAULT FALSE);";

        String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                "patient_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "urgency_score INT NOT NULL, " +
                "bed_id INT, " +
                "FOREIGN KEY (bed_id) REFERENCES beds(bed_id));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createBedsTable);
            stmt.execute(createPatientsTable);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM beds");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute("INSERT INTO beds VALUES (101, 'ICU', FALSE)");
                stmt.execute("INSERT INTO beds VALUES (102, 'ICU', FALSE)");
                stmt.execute("INSERT INTO beds VALUES (201, 'GENERAL', FALSE)");
                stmt.execute("INSERT INTO beds VALUES (202, 'GENERAL', FALSE)");
                System.out.println("[INIT] Seeded default beds into H2 database.");
            }

            System.out.println("[INIT] H2 Database initialized successfully.");
        } catch (SQLException e) {
            System.err.println("[ERROR] Database initialization failed: " + e.getMessage());
        }
    }
}
