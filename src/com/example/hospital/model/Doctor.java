package com.example.hospital.model;

public class Doctor {
    private int doctorId;
    private String firstName;
    private String surname;
    private String address;
    private String email;

    // Constructor
    public Doctor(int doctorId, String firstName, String surname, String address, String email) {
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.email = email;
    }

    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Example method (could be overridden by subclasses if needed)
    public void displayDoctorInfo() {
        System.out.println("Doctor ID: " + doctorId + ", Name: " + firstName + " " + surname);
    }
}
