package com.example.hospital.model;

public class Drug {
    private int drugId;
    private String name;
    private String sideEffects;
    private String benefits;

    public Drug(int drugId, String name, String sideEffects, String benefits) {
        this.drugId = drugId;
        this.name = name;
        this.sideEffects = sideEffects;
        this.benefits = benefits;
    }

    // Getters and Setters
    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }
}
