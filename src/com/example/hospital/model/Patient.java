package com.example.hospital.model;

public class Patient {
    private int patientId;
    private String firstName;
    private String surname;
    private String postcode;
    private String address;
    private String phone;
    private String email;

    // Basic Constructor
    public Patient(int patientId, String firstName, String surname,
                   String postcode, String address, String phone, String email) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.surname = surname;
        this.postcode = postcode;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Overloaded Constructor (example of polymorphism with constructor overloading)
    public Patient(int patientId, String firstName, String surname) {
        this(patientId, firstName, surname, "", "", "", "");
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
