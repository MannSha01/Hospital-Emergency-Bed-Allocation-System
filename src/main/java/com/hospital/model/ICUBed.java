package com.hospital.model;

public class ICUBed extends Bed {
    private boolean ventilatorAvailable;

    public ICUBed(int bedId, boolean isOccupied, boolean ventilatorAvailable) {
        super(bedId, "ICU", isOccupied);
        this.ventilatorAvailable = ventilatorAvailable;
    }

    public boolean isVentilatorAvailable() { return ventilatorAvailable; }
    public void setVentilatorAvailable(boolean ventilatorAvailable) { this.ventilatorAvailable = ventilatorAvailable; }

    @Override
    public String getBedDetails() {
        return String.format("ICU Bed #%d [Ventilator: %s]", getBedId(), ventilatorAvailable ? "Yes" : "No");
    }
}
