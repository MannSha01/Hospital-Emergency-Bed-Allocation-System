package com.hospital.service;

import com.hospital.dao.BedDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.exception.NoBedsAvailableException;
import com.hospital.model.Bed;
import com.hospital.model.Patient;

import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

public class TriageService {
    private final PriorityBlockingQueue<Patient> patientQueue;
    private final BedDAO bedDAO;
    private final PatientDAO patientDAO;

    public TriageService() {
        this.patientQueue = new PriorityBlockingQueue<>();
        this.bedDAO = new BedDAO();
        this.patientDAO = new PatientDAO();
    }

    public void registerPatient(Patient patient) {
        patientQueue.add(patient);
        AuditLogger.log("Registered patient: " + patient.getName() + " (Urgency: " + patient.getUrgencyScore() + ")");
        System.out.println("[TRIAGE] Registered: " + patient.getName());
    }

    public synchronized Bed allocateBedNextPatient() throws NoBedsAvailableException {
        Patient patient = patientQueue.poll();
        if (patient == null) {
            System.out.println("[TRIAGE] No patients waiting in queue.");
            return null;
        }

        Map<Integer, Bed> beds = bedDAO.getAllBeds();
        Bed selectedBed = null;

        // Priority logic: High urgency (1-2) prefers ICU bed
        boolean needsICU = patient.getUrgencyScore() <= 2;

        if (needsICU) {
            for (Bed bed : beds.values()) {
                if ("ICU".equalsIgnoreCase(bed.getBedType()) && !bed.isOccupied()) {
                    selectedBed = bed;
                    break;
                }
            }
        }

        // If no ICU bed found or not required, assign any available bed
        if (selectedBed == null) {
            for (Bed bed : beds.values()) {
                if (!bed.isOccupied()) {
                    selectedBed = bed;
                    break;
                }
            }
        }

        if (selectedBed == null) {
            // Patient stays out of queue so execution drains cleanly
            throw new NoBedsAvailableException("No beds currently available for patient: " + patient.getName());
        }

        selectedBed.setOccupied(true);
        bedDAO.updateBedStatus(selectedBed.getBedId(), true);

        patient.setBedId(selectedBed.getBedId());
        patientDAO.savePatient(patient);

        AuditLogger.log("Allocated Bed #" + selectedBed.getBedId() + " to Patient " + patient.getName());
        System.out.println("[ALLOCATION] Bed #" + selectedBed.getBedId() + " assigned to " + patient.getName());

        return selectedBed;
    }

    public int getQueueSize() {
        return patientQueue.size();
    }
}
