
package com.example.hospital.model;

import java.time.LocalDate;
import java.util.List;

public class Prescription {
    private int prescriptionId;
    private LocalDate dateOfPrescribe;
    private String dosage;
    private String duration;
    private String comment;

    // Potential association to a Patient, or just a patientId
    private Patient patient;

    // One prescription can include multiple drugs
    private List<Drug> drugs;

    public Prescription(int prescriptionId, LocalDate dateOfPrescribe, String dosage, 
                        String duration, String comment, Patient patient, List<Drug> drugs) {
        this.prescriptionId = prescriptionId;
        this.dateOfPrescribe = dateOfPrescribe;
        this.dosage = dosage;
        this.duration = duration;
        this.comment = comment;
        this.patient = patient;
        this.drugs = drugs;
    }

    // Getters and Setters
    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public LocalDate getDateOfPrescribe() {
        return dateOfPrescribe;
    }

    public void setDateOfPrescribe(LocalDate dateOfPrescribe) {
        this.dateOfPrescribe = dateOfPrescribe;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }
}
