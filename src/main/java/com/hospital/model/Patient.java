package com.hospital.model;

public class Patient implements Comparable<Patient> {
    private int patientId;
    private String name;
    private int urgencyScore; 
    private Integer bedId;

    public Patient(String name, int urgencyScore) {
        this.name = name;
        this.urgencyScore = urgencyScore;
    }

    public Patient(int patientId, String name, int urgencyScore, Integer bedId) {
        this.patientId = patientId;
        this.name = name;
        this.urgencyScore = urgencyScore;
        this.bedId = bedId;
    }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getUrgencyScore() { return urgencyScore; }
    public void setUrgencyScore(int urgencyScore) { this.urgencyScore = urgencyScore; }

    public Integer getBedId() { return bedId; }
    public void setBedId(Integer bedId) { this.bedId = bedId; }

    @Override
    public int compareTo(Patient other) {
        
        return Integer.compare(this.urgencyScore, other.urgencyScore);
    }

    @Override
    public String toString() {
        return String.format("Patient[ID=%d, Name='%s', Urgency=%d, BedID=%s]", 
                patientId, name, urgencyScore, (bedId != null ? bedId : "None"));
    }
}
