package com.hospital.model;

public abstract class Bed {
    private int bedId;
    private String bedType;
    private boolean isOccupied;

    public Bed(int bedId, String bedType, boolean isOccupied) {
        this.bedId = bedId;
        this.bedType = bedType;
        this.isOccupied = isOccupied;
    }

    public int getBedId() { return bedId; }
    public void setBedId(int bedId) { this.bedId = bedId; }

    public String getBedType() { return bedType; }
    public void setBedType(String bedType) { this.bedType = bedType; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public abstract String getBedDetails();

    @Override
    public String toString() {
        return String.format("Bed[ID=%d, Type='%s', Occupied=%b]", bedId, bedType, isOccupied);
    }
}
