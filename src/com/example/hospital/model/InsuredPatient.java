package com.example.hospital.model;

// Demonstrates inheritance (InsuredPatient is-a Patient)
public class InsuredPatient extends Patient {
    private String insuranceType;
    private String insuranceCompanyName;
    private int durationOfInsurance; // e.g., number of months/years

    public InsuredPatient(int patientId, String firstName, String surname,
                          String postcode, String address, String phone, String email,
                          String insuranceType, String insuranceCompanyName,
                          int durationOfInsurance) {
        super(patientId, firstName, surname, postcode, address, phone, email);
        this.insuranceType = insuranceType;
        this.insuranceCompanyName = insuranceCompanyName;
        this.durationOfInsurance = durationOfInsurance;
    }

    // Getters and Setters
    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public int getDurationOfInsurance() {
        return durationOfInsurance;
    }

    public void setDurationOfInsurance(int durationOfInsurance) {
        this.durationOfInsurance = durationOfInsurance;
    }

    // Example method that might differ from a standard Patient
    public void displayInsuranceDetails() {
        System.out.println("Insurance Company: " + insuranceCompanyName
                           + ", Type: " + insuranceType
                           + ", Duration: " + durationOfInsurance + " months");
    }
}
