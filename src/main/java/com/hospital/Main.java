package com.hospital;

import com.hospital.config.DatabaseConfig;
import com.hospital.exception.NoBedsAvailableException;
import com.hospital.model.Patient;
import com.hospital.service.TriageService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hospital Emergency Triage & Bed Allocation System ===");

        // Initialize H2 Database tables and seed initial beds
        DatabaseConfig.initializeDatabase();

        TriageService triageService = new TriageService();

        // Register patients with varying urgency levels (1 = Critical, 5 = Non-urgent)
        triageService.registerPatient(new Patient("Alice Smith", 1));  // High urgency -> ICU
        triageService.registerPatient(new Patient("Bob Jones", 4));    // Low urgency -> General
        triageService.registerPatient(new Patient("Charlie Brown", 1));// High urgency -> ICU
        triageService.registerPatient(new Patient("David Miller", 2)); // High urgency -> ICU
        triageService.registerPatient(new Patient("Eva Green", 3));   // Medium urgency -> General

        System.out.println("\n--- Processing Bed Allocations ---");

        // Process priority queue until empty
        while (triageService.getQueueSize() > 0) {
            try {
                triageService.allocateBedNextPatient();
            } catch (NoBedsAvailableException e) {
                System.err.println("[EXPERT LOG] " + e.getMessage());
            }
        }

        System.out.println("\n=== Triage processing sequence complete ===");
    }
}
