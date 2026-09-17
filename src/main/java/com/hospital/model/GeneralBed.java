package com.hospital.model;

public class GeneralBed extends Bed {
    private int wardNumber;

    public GeneralBed(int bedId, boolean isOccupied, int wardNumber) {
        super(bedId, "GENERAL", isOccupied);
        this.wardNumber = wardNumber;
    }

    public int getWardNumber() { return wardNumber; }
    public void setWardNumber(int wardNumber) { this.wardNumber = wardNumber; }

    @Override
    public String getBedDetails() {
        return String.format("General Bed #%d [Ward: %d]", getBedId(), wardNumber);
    }
}
