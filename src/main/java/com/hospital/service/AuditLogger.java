package com.hospital.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {
    private static final String LOG_FILE = "triage_audit.log";

    public static synchronized void log(String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = String.format("[%s] %s%n", timestamp, action);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Audit log error: " + e.getMessage());
        }
    }
}
