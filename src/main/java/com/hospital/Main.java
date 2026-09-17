package com.hospital;

import com.hospital.config.DatabaseConfig;
import com.hospital.exception.NoBedsAvailableException;
import com.hospital.model.Patient;
import com.hospital.service.TriageService;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hospital Emergency Triage & Bed Allocation System ===");

        
        DatabaseConfig.initializeDatabase();

        TriageService triageService = new TriageService();

        
        triageService.registerPatient(new Patient("Aarav Sharma", 1));  // High urgency -> ICU
        triageService.registerPatient(new Patient("Rohan Verma", 4));   // Low urgency -> General
        triageService.registerPatient(new Patient("Ananya Iyer", 1));   // High urgency -> ICU
        triageService.registerPatient(new Patient("Vikram Singh", 2));  // High urgency -> ICU
        triageService.registerPatient(new Patient("Deepa Rao", 3));     // Medium urgency -> General

        triageService.printDashboard();

        System.out.println("\n--- Processing Bed Allocations ---");

       
        while (triageService.getQueueSize() > 0) {
            try {
                triageService.allocateBedNextPatient();
            } catch (NoBedsAvailableException e) {
                System.err.println("[EXPERT LOG] " + e.getMessage());
            }
        }

        
        triageService.printDashboard();

        System.out.println("=== Triage processing sequence complete ===");
    }
}
