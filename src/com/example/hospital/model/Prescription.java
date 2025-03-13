package com.example.hospital.model;

import java.time.LocalDate;

public class Prescription {
    private int prescriptionId;
    private LocalDate dateOfPrescribe;
    private String dosage;
    private String duration;
    private String comment;

    // Single references
    private int doctorId;  // or a Doctor object
    private int patientId; // or a Patient object
    private int drugId;    // or a Drug object

    public Prescription(int prescriptionId,
                        LocalDate dateOfPrescribe,
                        String dosage,
                        String duration,
                        String comment,
                        int doctorId,
                        int patientId,
                        int drugId) {
        this.prescriptionId = prescriptionId;
        this.dateOfPrescribe = dateOfPrescribe;
        this.dosage = dosage;
        this.duration = duration;
        this.comment = comment;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.drugId = drugId;
    }

    // Getters and setters
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

    public int getDoctorId() {
        return doctorId;
    }


    public int getPatientId() {
        return patientId;
    }


    public int getDrugId() {
        return drugId;
    }

}
