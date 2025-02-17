package com.example.hospital.model;

import java.time.LocalDate;

public class Visit {
    private int visitId;
    private LocalDate dateOfVisit;
    private String symptoms;
    private String diagnosis;

    // Associations (in a real system, might store doctorId and patientId here
    // or references to Doctor and Patient objects)
    private Doctor doctor;
    private Patient patient;

    public Visit(int visitId, LocalDate dateOfVisit, String symptoms, String diagnosis,
                 Doctor doctor, Patient patient) {
        this.visitId = visitId;
        this.dateOfVisit = dateOfVisit;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.doctor = doctor;
        this.patient = patient;
    }

    // Getters and Setters
    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public LocalDate getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(LocalDate dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
