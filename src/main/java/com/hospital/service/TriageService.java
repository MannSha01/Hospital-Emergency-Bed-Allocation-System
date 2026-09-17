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

    private String getConditionLabel(int urgency) {
        switch (urgency) {
            case 1: return "CRITICAL (ICU Required)";
            case 2: return "SEVERE (ICU Preferred)";
            case 3: return "MODERATE";
            case 4: return "LOW URGENCY";
            case 5: return "NON-URGENT";
            default: return "UNKNOWN";
        }
    }

    public void registerPatient(Patient patient) {
        patientQueue.add(patient);
        String label = getConditionLabel(patient.getUrgencyScore());
        AuditLogger.log("Registered patient: " + patient.getName() + " | Condition: " + label);
        System.out.println("[TRIAGE] Registered: " + patient.getName() + " -> Condition: [" + label + "]");
    }

    public synchronized Bed allocateBedNextPatient() throws NoBedsAvailableException {
        Patient patient = patientQueue.poll();
        if (patient == null) {
            System.out.println("[TRIAGE] No patients waiting in queue.");
            return null;
        }

        Map<Integer, Bed> beds = bedDAO.getAllBeds();
        Bed selectedBed = null;

        boolean needsICU = patient.getUrgencyScore() <= 2;

        if (needsICU) {
            for (Bed bed : beds.values()) {
                if ("ICU".equalsIgnoreCase(bed.getBedType()) && !bed.isOccupied()) {
                    selectedBed = bed;
                    break;
                }
            }
        }

        if (selectedBed == null) {
            for (Bed bed : beds.values()) {
                if (!bed.isOccupied()) {
                    selectedBed = bed;
                    break;
                }
            }
        }

        if (selectedBed == null) {
            throw new NoBedsAvailableException("No beds currently available for patient: " + patient.getName() 
                + " [" + getConditionLabel(patient.getUrgencyScore()) + "]");
        }

        selectedBed.setOccupied(true);
        bedDAO.updateBedStatus(selectedBed.getBedId(), true);

        patient.setBedId(selectedBed.getBedId());
        patientDAO.savePatient(patient);

        String bedInfo = selectedBed.getBedType() + " Bed #" + selectedBed.getBedId();
        AuditLogger.log("Allocated " + bedInfo + " to Patient " + patient.getName());
        System.out.println("[ALLOCATION] " + bedInfo + " assigned to " + patient.getName() 
            + " (" + getConditionLabel(patient.getUrgencyScore()) + ")");

        return selectedBed;
    }

    public int getQueueSize() {
        return patientQueue.size();
    }
}
