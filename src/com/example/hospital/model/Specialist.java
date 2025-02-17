package com.example.hospital.model;

// Demonstrates inheritance (Specialist is-a Doctor)
public class Specialist extends Doctor {
    private String specialization;
    private int yearsOfExperience;

    // Constructor calls super to initialize Doctor fields
    public Specialist(int doctorId, String firstName, String surname, String address, String email,
                      String specialization, int yearsOfExperience) {
        super(doctorId, firstName, surname, address, email);
        this.specialization = specialization;
        this.yearsOfExperience = yearsOfExperience;
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    // Polymorphic behavior: override displayDoctorInfo
    @Override
    public void displayDoctorInfo() {
        super.displayDoctorInfo();
        System.out.println("Specialization: " + specialization
                           + ", Years of Experience: " + yearsOfExperience);
    }
}
