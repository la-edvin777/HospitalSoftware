package com.example.hospital.model;

public class Insurance {
    private int insuranceId;
    private String company;
    private String address;
    private String phone;

    public Insurance(int insuranceId, String company, String address, String phone) {
        this.insuranceId = insuranceId;
        this.company = company;
        this.address = address;
        this.phone = phone;
    }

    // Getters and Setters
    public int getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(int insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
