package com.example.hospital;

import com.example.hospital.dao.*;
import com.example.hospital.model.*;
import com.example.hospital.util.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * A Swing GUI demonstrating Add, Remove, Modify, and Show All operations
 * for Doctor, Specialist, Patient, InsuredPatient, Visit, Prescription, Drug, and Insurance.
 */
public class MainApp extends JFrame {

    private JComboBox<String> entityCombo;
    private JButton addButton;
    private JButton removeButton;
    private JButton modifyButton;
    private JButton showAllButton;

    // DAO instances
    private DoctorDAO doctorDAO;
    private SpecialistDAO specialistDAO;
    private PatientDAO patientDAO;
    private InsuredPatientDAO insuredPatientDAO;
    private VisitDAO visitDAO;
    private PrescriptionDAO prescriptionDAO;
    private DrugDAO drugDAO;
    private InsuranceDAO insuranceDAO;

    public MainApp() {
        super("Hospital Database App - Full Implementation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 130);
        setLocationRelativeTo(null); // Center window on screen

        // 1. Initialize the DAOs
        doctorDAO = new DoctorDAO();
        specialistDAO = new SpecialistDAO();
        patientDAO = new PatientDAO();
        insuredPatientDAO = new InsuredPatientDAO();
        visitDAO = new VisitDAO();
        prescriptionDAO = new PrescriptionDAO();
        drugDAO = new DrugDAO();
        insuranceDAO = new InsuranceDAO();

        // 2. Set up basic layout
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));

        // 3. ComboBox for selecting which entity to manipulate
        entityCombo = new JComboBox<>(new String[]{
                "Doctor",
                "Specialist",
                "Patient",
                "InsuredPatient",
                "Visit",
                "Prescription",
                "Drug",
                "Insurance"
        });

        // 4. Buttons
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        modifyButton = new JButton("Modify");
        showAllButton = new JButton("Show All");

        // 5. Add components to frame
        add(new JLabel("Select Entity:"));
        add(entityCombo);
        add(addButton);
        add(removeButton);
        add(modifyButton);
        add(showAllButton);

        // 6. ActionListeners for each button
        addButton.addActionListener(e -> handleAdd());
        removeButton.addActionListener(e -> handleRemove());
        modifyButton.addActionListener(e -> handleModify());
        showAllButton.addActionListener(e -> handleShowAll());

        // 7. Make the frame visible
        setVisible(true);
    }

    /**
     * Determine which entity is selected, then handle "Add".
     * We'll do very minimal field prompts for demonstration.
     */
    private void handleAdd() {
        String entity = (String) entityCombo.getSelectedItem();
        if (entity == null) return;

        switch (entity) {
            case "Doctor":
                addDoctor();
                break;
            case "Specialist":
                addSpecialist();
                break;
            case "Patient":
                addPatient();
                break;
            case "InsuredPatient":
                addInsuredPatient();
                break;
            case "Visit":
                addVisit();
                break;
            case "Prescription":
                addPrescription();
                break;
            case "Drug":
                addDrug();
                break;
            case "Insurance":
                addInsurance();
                break;
        }
    }

    /**
     * Determine which entity is selected, then handle "Remove" by prompting for an ID.
     */
    private void handleRemove() {
        String entity = (String) entityCombo.getSelectedItem();
        if (entity == null) return;

        String inputId = JOptionPane.showInputDialog(this,
                "Enter ID to remove:", "Remove " + entity,
                JOptionPane.QUESTION_MESSAGE);
        if (inputId == null || inputId.trim().isEmpty()) return;

        int id = -1;
        try {
            id = Integer.parseInt(inputId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (entity) {
            case "Doctor":
                doctorDAO.deleteDoctor(id);
                JOptionPane.showMessageDialog(this,
                        "Doctor with ID " + id + " removed (if existed).");
                break;
            case "Specialist":
                specialistDAO.deleteSpecialist(id);
                JOptionPane.showMessageDialog(this,
                        "Specialist with ID " + id + " removed (if existed).");
                break;
            case "Patient":
                patientDAO.deletePatient(id);
                JOptionPane.showMessageDialog(this,
                        "Patient with ID " + id + " removed (if existed).");
                break;
            case "InsuredPatient":
                insuredPatientDAO.deleteInsuredPatient(id);
                JOptionPane.showMessageDialog(this,
                        "InsuredPatient with ID " + id + " removed (if existed).");
                break;
            case "Visit":
                visitDAO.deleteVisit(id);
                JOptionPane.showMessageDialog(this,
                        "Visit with ID " + id + " removed (if existed).");
                break;
            case "Prescription":
                prescriptionDAO.deletePrescription(id);
                JOptionPane.showMessageDialog(this,
                        "Prescription with ID " + id + " removed (if existed).");
                break;
            case "Drug":
                drugDAO.deleteDrug(id);
                JOptionPane.showMessageDialog(this,
                        "Drug with ID " + id + " removed (if existed).");
                break;
            case "Insurance":
                insuranceDAO.deleteInsurance(id);
                JOptionPane.showMessageDialog(this,
                        "Insurance with ID " + id + " removed (if existed).");
                break;
        }
    }

    /**
     * Determine which entity is selected, then handle "Modify" by prompting for an ID
     * and new field data.
     */
    private void handleModify() {
        String entity = (String) entityCombo.getSelectedItem();
        if (entity == null) return;

        String inputId = JOptionPane.showInputDialog(this,
                "Enter ID to modify:", "Modify " + entity,
                JOptionPane.QUESTION_MESSAGE);
        if (inputId == null || inputId.trim().isEmpty()) return;

        int id = -1;
        try {
            id = Integer.parseInt(inputId);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // We'll do minimal fields for demonstration. Real usage would do more advanced forms:
        switch (entity) {
            case "Doctor":
                Doctor doc = doctorDAO.getDoctorById(id);
                if (doc == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Doctor found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newDocEmail = JOptionPane.showInputDialog(this,
                        "Enter new email (current: " + doc.getEmail() + "):",
                        "Modify Doctor", JOptionPane.QUESTION_MESSAGE);
                if (newDocEmail != null && !newDocEmail.trim().isEmpty()) {
                    doc.setEmail(newDocEmail);
                    doctorDAO.updateDoctor(doc);
                    JOptionPane.showMessageDialog(this, "Doctor updated!");
                }
                break;

            case "Specialist":
                Specialist sp = specialistDAO.getSpecialistById(id);
                if (sp == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Specialist found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newSpecialty = JOptionPane.showInputDialog(this,
                        "Enter new specialization (current: " + sp.getSpecialization() + "):",
                        "Modify Specialist", JOptionPane.QUESTION_MESSAGE);
                if (newSpecialty != null && !newSpecialty.trim().isEmpty()) {
                    sp.setSpecialization(newSpecialty);
                    specialistDAO.updateSpecialist(sp);
                    JOptionPane.showMessageDialog(this, "Specialist updated!");
                }
                break;

            case "Patient":
                Patient pat = patientDAO.getPatientById(id);
                if (pat == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Patient found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newAddress = JOptionPane.showInputDialog(this,
                        "Enter new address (current: " + pat.getAddress() + "):",
                        "Modify Patient", JOptionPane.QUESTION_MESSAGE);
                if (newAddress != null && !newAddress.trim().isEmpty()) {
                    pat.setAddress(newAddress);
                    patientDAO.updatePatient(pat);
                    JOptionPane.showMessageDialog(this, "Patient updated!");
                }
                break;

            case "InsuredPatient":
                InsuredPatient ip = insuredPatientDAO.getInsuredPatientById(id);
                if (ip == null) {
                    JOptionPane.showMessageDialog(this,
                            "No InsuredPatient found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newCompany = JOptionPane.showInputDialog(this,
                        "Enter new insurance company (current: " + ip.getInsuranceCompanyName() + "):",
                        "Modify InsuredPatient", JOptionPane.QUESTION_MESSAGE);
                if (newCompany != null && !newCompany.trim().isEmpty()) {
                    ip.setInsuranceCompanyName(newCompany);
                    insuredPatientDAO.updateInsuredPatient(ip);
                    JOptionPane.showMessageDialog(this, "InsuredPatient updated!");
                }
                break;

            case "Visit":
                Visit vis = visitDAO.getVisitById(id);
                if (vis == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Visit found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newDiagnosis = JOptionPane.showInputDialog(this,
                        "Enter new diagnosis (current: " + vis.getDiagnosis() + "):",
                        "Modify Visit", JOptionPane.QUESTION_MESSAGE);
                if (newDiagnosis != null && !newDiagnosis.trim().isEmpty()) {
                    vis.setDiagnosis(newDiagnosis);
                    visitDAO.updateVisit(vis);
                    JOptionPane.showMessageDialog(this, "Visit updated!");
                }
                break;

            case "Prescription":
                Prescription pres = prescriptionDAO.getPrescriptionById(id);
                if (pres == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Prescription found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newComment = JOptionPane.showInputDialog(this,
                        "Enter new comment (current: " + pres.getComment() + "):",
                        "Modify Prescription", JOptionPane.QUESTION_MESSAGE);
                if (newComment != null && !newComment.trim().isEmpty()) {
                    pres.setComment(newComment);
                    prescriptionDAO.updatePrescription(pres);
                    JOptionPane.showMessageDialog(this, "Prescription updated!");
                }
                break;

            case "Drug":
                Drug dr = drugDAO.getDrugById(id);
                if (dr == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Drug found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newBenefits = JOptionPane.showInputDialog(this,
                        "Enter new benefits (current: " + dr.getBenefits() + "):",
                        "Modify Drug", JOptionPane.QUESTION_MESSAGE);
                if (newBenefits != null && !newBenefits.trim().isEmpty()) {
                    dr.setBenefits(newBenefits);
                    drugDAO.updateDrug(dr);
                    JOptionPane.showMessageDialog(this, "Drug updated!");
                }
                break;

            case "Insurance":
                Insurance ins = insuranceDAO.getInsuranceById(id);
                if (ins == null) {
                    JOptionPane.showMessageDialog(this,
                            "No Insurance found with ID " + id, "Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String newPhone = JOptionPane.showInputDialog(this,
                        "Enter new phone (current: " + ins.getPhone() + "):",
                        "Modify Insurance", JOptionPane.QUESTION_MESSAGE);
                if (newPhone != null && !newPhone.trim().isEmpty()) {
                    ins.setPhone(newPhone);
                    insuranceDAO.updateInsurance(ins);
                    JOptionPane.showMessageDialog(this, "Insurance updated!");
                }
                break;
        }
    }

    /**
     * Determine which entity is selected, then handle "Show All".
     * We'll display them in a multiline text area inside a JOptionPane.
     */
    private void handleShowAll() {
        String entity = (String) entityCombo.getSelectedItem();
        if (entity == null) return;

        switch (entity) {
            case "Doctor":
                List<Doctor> docList = doctorDAO.getAllDoctors();
                showResultList("All Doctors", docListToString(docList));
                break;
            case "Specialist":
                List<Specialist> spList = specialistDAO.getAllSpecialists();
                showResultList("All Specialists", specialistListToString(spList));
                break;
            case "Patient":
                List<Patient> patList = patientDAO.getAllPatients();
                showResultList("All Patients", patientListToString(patList));
                break;
            case "InsuredPatient":
                List<InsuredPatient> ipList = insuredPatientDAO.getAllInsuredPatients();
                showResultList("All InsuredPatients", insuredPatientListToString(ipList));
                break;
            case "Visit":
                List<Visit> visList = visitDAO.getAllVisits();
                showResultList("All Visits", visitListToString(visList));
                break;
            case "Prescription":
                List<Prescription> presList = prescriptionDAO.getAllPrescriptions();
                showResultList("All Prescriptions", prescriptionListToString(presList));
                break;
            case "Drug":
                List<Drug> drugList = drugDAO.getAllDrugs();
                showResultList("All Drugs", drugListToString(drugList));
                break;
            case "Insurance":
                List<Insurance> insList = insuranceDAO.getAllInsuranceRecords();
                showResultList("All Insurance", insuranceListToString(insList));
                break;
        }
    }

    // ------------------------------------------------------------------------------------------
    // Below: Minimal "Add" methods for each entity. You should adapt them for your actual fields.
    // ------------------------------------------------------------------------------------------

    private void addDoctor() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Doctor ID:", "Add Doctor",
                JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        int id = parseIntOrFail(idStr);
        String fname = JOptionPane.showInputDialog(this, "Enter First Name:", "Add Doctor",
                JOptionPane.QUESTION_MESSAGE);
        String sname = JOptionPane.showInputDialog(this, "Enter Surname:", "Add Doctor",
                JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(this, "Enter Address:", "Add Doctor",
                JOptionPane.QUESTION_MESSAGE);
        String email = JOptionPane.showInputDialog(this, "Enter Email:", "Add Doctor",
                JOptionPane.QUESTION_MESSAGE);

        Doctor d = new Doctor(id, fname, sname, address, email);
        doctorDAO.insertDoctor(d);
        JOptionPane.showMessageDialog(this, "Doctor added successfully!");
    }

    private void addSpecialist() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Specialist ID:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        int id = parseIntOrFail(idStr);
        String fname = JOptionPane.showInputDialog(this, "Enter First Name:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        String sname = JOptionPane.showInputDialog(this, "Enter Surname:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(this, "Enter Address:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        String email = JOptionPane.showInputDialog(this, "Enter Email:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        String spec = JOptionPane.showInputDialog(this, "Enter Specialization:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        String expStr = JOptionPane.showInputDialog(this, "Enter Years of Experience:", "Add Specialist",
                JOptionPane.QUESTION_MESSAGE);
        int exp = parseIntOrFail(expStr);

        Specialist s = new Specialist(id, fname, sname, address, email, spec, exp);
        specialistDAO.insertSpecialist(s);
        JOptionPane.showMessageDialog(this, "Specialist added successfully!");
    }

    private void addPatient() {
        String idStr = JOptionPane.showInputDialog(this, "Enter Patient ID:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        int id = parseIntOrFail(idStr);

        String fname = JOptionPane.showInputDialog(this, "Enter First Name:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        String sname = JOptionPane.showInputDialog(this, "Enter Surname:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        String postcode = JOptionPane.showInputDialog(this, "Enter Postcode:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(this, "Enter Address:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        String phone = JOptionPane.showInputDialog(this, "Enter Phone:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);
        String email = JOptionPane.showInputDialog(this, "Enter Email:", "Add Patient",
                JOptionPane.QUESTION_MESSAGE);

        Patient p = new Patient(id, fname, sname, postcode, address, phone, email);
        patientDAO.insertPatient(p);
        JOptionPane.showMessageDialog(this, "Patient added successfully!");
    }

    private void addInsuredPatient() {
        String idStr = JOptionPane.showInputDialog(this, "Enter InsuredPatient ID:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        if (idStr == null || idStr.trim().isEmpty()) return;
        int id = parseIntOrFail(idStr);

        String fname = JOptionPane.showInputDialog(this, "Enter First Name:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String sname = JOptionPane.showInputDialog(this, "Enter Surname:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String postcode = JOptionPane.showInputDialog(this, "Enter Postcode:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(this, "Enter Address:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String phone = JOptionPane.showInputDialog(this, "Enter Phone:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String email = JOptionPane.showInputDialog(this, "Enter Email:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String insType = JOptionPane.showInputDialog(this, "Enter Insurance Type:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String insCompany = JOptionPane.showInputDialog(this, "Enter Insurance Company:", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        String durStr = JOptionPane.showInputDialog(this, "Enter Duration (months):", "Add InsuredPatient",
                JOptionPane.QUESTION_MESSAGE);
        int duration = parseIntOrFail(durStr);

        InsuredPatient ip = new InsuredPatient(id, fname, sname, postcode, address, phone, email,
                insType, insCompany, duration);
        insuredPatientDAO.insertInsuredPatient(ip);
        JOptionPane.showMessageDialog(this, "InsuredPatient added successfully!");
    }

    private void addVisit() {
        // Minimal example: we assume the user will specify visitId, date, doctorId, patientId, symptoms, diagnosis.
        String visitIdStr = JOptionPane.showInputDialog(this, "Enter Visit ID:", "Add Visit",
                JOptionPane.QUESTION_MESSAGE);
        if (visitIdStr == null || visitIdStr.trim().isEmpty()) return;
        int visitId = parseIntOrFail(visitIdStr);

        String dateStr = JOptionPane.showInputDialog(this,
                "Enter Date (YYYY-MM-DD):", "Add Visit", JOptionPane.QUESTION_MESSAGE);
        LocalDate date = LocalDate.parse(dateStr);

        String symptoms = JOptionPane.showInputDialog(this, "Enter Symptoms:", "Add Visit",
                JOptionPane.QUESTION_MESSAGE);
        String diagnosis = JOptionPane.showInputDialog(this, "Enter Diagnosis:", "Add Visit",
                JOptionPane.QUESTION_MESSAGE);

        // For real usage, you'd retrieve an existing Doctor or Patient. For now, we store placeholders:
        String doctorIdStr = JOptionPane.showInputDialog(this, "Enter Doctor ID for this visit:", "Add Visit",
                JOptionPane.QUESTION_MESSAGE);
        int doctorId = parseIntOrFail(doctorIdStr);
        Doctor doc = new Doctor(doctorId, "PlaceholderF", "PlaceholderS", "PlaceholderAddr", "PlaceholderEmail");

        String patientIdStr = JOptionPane.showInputDialog(this, "Enter Patient ID for this visit:", "Add Visit",
                JOptionPane.QUESTION_MESSAGE);
        int patientId = parseIntOrFail(patientIdStr);
        Patient pat = new Patient(patientId, "PlaceholderF", "PlaceholderS");

        Visit v = new Visit(visitId, date, symptoms, diagnosis, doc, pat);
        visitDAO.insertVisit(v);
        JOptionPane.showMessageDialog(this, "Visit added successfully!");
    }

    private void addPrescription() {
        String presIdStr = JOptionPane.showInputDialog(this, "Enter Prescription ID:", "Add Prescription",
                JOptionPane.QUESTION_MESSAGE);
        if (presIdStr == null || presIdStr.trim().isEmpty()) return;
        int presId = parseIntOrFail(presIdStr);

        String dateStr = JOptionPane.showInputDialog(this,
                "Enter Date of Prescribe (YYYY-MM-DD):", "Add Prescription", JOptionPane.QUESTION_MESSAGE);
        LocalDate date = LocalDate.parse(dateStr);

        String dosage = JOptionPane.showInputDialog(this, "Enter Dosage:", "Add Prescription",
                JOptionPane.QUESTION_MESSAGE);
        String duration = JOptionPane.showInputDialog(this, "Enter Duration:", "Add Prescription",
                JOptionPane.QUESTION_MESSAGE);
        String comment = JOptionPane.showInputDialog(this, "Enter Comment:", "Add Prescription",
                JOptionPane.QUESTION_MESSAGE);

        // Minimal approach: we just prompt for patientId and assume we create placeholders:
        String patientIdStr = JOptionPane.showInputDialog(this, "Enter Patient ID for this prescription:",
                "Add Prescription", JOptionPane.QUESTION_MESSAGE);
        int patientId = parseIntOrFail(patientIdStr);
        Patient pat = new Patient(patientId, "PlaceholderF", "PlaceholderS");

        // For the drugs, let's just create an empty list:
        List<Drug> drugList = new ArrayList<>();

        Prescription p = new Prescription(presId, date, dosage, duration, comment, pat, drugList);
        prescriptionDAO.insertPrescription(p);
        JOptionPane.showMessageDialog(this, "Prescription added successfully (no drugs linked yet).");
    }

    private void addDrug() {
        String drugIdStr = JOptionPane.showInputDialog(this, "Enter Drug ID:", "Add Drug",
                JOptionPane.QUESTION_MESSAGE);
        if (drugIdStr == null || drugIdStr.trim().isEmpty()) return;
        int drugId = parseIntOrFail(drugIdStr);

        String name = JOptionPane.showInputDialog(this, "Enter Drug Name:", "Add Drug",
                JOptionPane.QUESTION_MESSAGE);
        String sideEff = JOptionPane.showInputDialog(this, "Enter Side Effects:", "Add Drug",
                JOptionPane.QUESTION_MESSAGE);
        String benefits = JOptionPane.showInputDialog(this, "Enter Benefits:", "Add Drug",
                JOptionPane.QUESTION_MESSAGE);

        Drug d = new Drug(drugId, name, sideEff, benefits);
        drugDAO.insertDrug(d);
        JOptionPane.showMessageDialog(this, "Drug added successfully!");
    }

    private void addInsurance() {
        String insIdStr = JOptionPane.showInputDialog(this, "Enter Insurance ID:", "Add Insurance",
                JOptionPane.QUESTION_MESSAGE);
        if (insIdStr == null || insIdStr.trim().isEmpty()) return;
        int insId = parseIntOrFail(insIdStr);

        String company = JOptionPane.showInputDialog(this, "Enter Insurance Company:", "Add Insurance",
                JOptionPane.QUESTION_MESSAGE);
        String address = JOptionPane.showInputDialog(this, "Enter Company Address:", "Add Insurance",
                JOptionPane.QUESTION_MESSAGE);
        String phone = JOptionPane.showInputDialog(this, "Enter Company Phone:", "Add Insurance",
                JOptionPane.QUESTION_MESSAGE);

        Insurance i = new Insurance(insId, company, address, phone);
        insuranceDAO.insertInsurance(i);
        JOptionPane.showMessageDialog(this, "Insurance added successfully!");
    }

    // ------------------------------------------------------------------------------------------
    // Utility methods for displaying "Show All" results
    // ------------------------------------------------------------------------------------------

    private void showResultList(String title, String content) {
        JTextArea textArea = new JTextArea(content, 20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Convert lists to strings (very naive formatting):
    private String docListToString(List<Doctor> list) {
        StringBuilder sb = new StringBuilder();
        for (Doctor d : list) {
            sb.append("ID: ").append(d.getDoctorId())
              .append(", Name: ").append(d.getFirstName()).append(" ").append(d.getSurname())
              .append(", Email: ").append(d.getEmail())
              .append("\n");
        }
        return sb.toString();
    }

    private String specialistListToString(List<Specialist> list) {
        StringBuilder sb = new StringBuilder();
        for (Specialist s : list) {
            sb.append("ID: ").append(s.getDoctorId())
              .append(", Name: ").append(s.getFirstName()).append(" ").append(s.getSurname())
              .append(", Specialization: ").append(s.getSpecialization())
              .append(", Experience: ").append(s.getYearsOfExperience())
              .append("\n");
        }
        return sb.toString();
    }

    private String patientListToString(List<Patient> list) {
        StringBuilder sb = new StringBuilder();
        for (Patient p : list) {
            sb.append("ID: ").append(p.getPatientId())
              .append(", Name: ").append(p.getFirstName()).append(" ").append(p.getSurname())
              .append(", Postcode: ").append(p.getPostcode())
              .append(", Address: ").append(p.getAddress())
              .append(", Phone: ").append(p.getPhone())
              .append("\n");
        }
        return sb.toString();
    }

    private String insuredPatientListToString(List<InsuredPatient> list) {
        StringBuilder sb = new StringBuilder();
        for (InsuredPatient ip : list) {
            sb.append("ID: ").append(ip.getPatientId())
              .append(", Name: ").append(ip.getFirstName()).append(" ").append(ip.getSurname())
              .append(", Insurance Company: ").append(ip.getInsuranceCompanyName())
              .append(", Type: ").append(ip.getInsuranceType())
              .append(", Duration: ").append(ip.getDurationOfInsurance())
              .append("\n");
        }
        return sb.toString();
    }

    private String visitListToString(List<Visit> list) {
        StringBuilder sb = new StringBuilder();
        for (Visit v : list) {
            sb.append("Visit ID: ").append(v.getVisitId())
              .append(", Date: ").append(v.getDateOfVisit())
              .append(", Symptoms: ").append(v.getSymptoms())
              .append(", Diagnosis: ").append(v.getDiagnosis())
              .append(", DoctorID: ").append(v.getDoctor().getDoctorId())
              .append(", PatientID: ").append(v.getPatient().getPatientId())
              .append("\n");
        }
        return sb.toString();
    }

    private String prescriptionListToString(List<Prescription> list) {
        StringBuilder sb = new StringBuilder();
        for (Prescription p : list) {
            sb.append("Prescription ID: ").append(p.getPrescriptionId())
              .append(", Date: ").append(p.getDateOfPrescribe())
              .append(", Dosage: ").append(p.getDosage())
              .append(", Duration: ").append(p.getDuration())
              .append(", PatientID: ").append(p.getPatient().getPatientId())
              .append(", Drugs#: ").append(p.getDrugs().size())
              .append("\n");
        }
        return sb.toString();
    }

    private String drugListToString(List<Drug> list) {
        StringBuilder sb = new StringBuilder();
        for (Drug d : list) {
            sb.append("Drug ID: ").append(d.getDrugId())
              .append(", Name: ").append(d.getName())
              .append(", SideEffects: ").append(d.getSideEffects())
              .append(", Benefits: ").append(d.getBenefits())
              .append("\n");
        }
        return sb.toString();
    }

    private String insuranceListToString(List<Insurance> list) {
        StringBuilder sb = new StringBuilder();
        for (Insurance i : list) {
            sb.append("Insurance ID: ").append(i.getInsuranceId())
              .append(", Company: ").append(i.getCompany())
              .append(", Address: ").append(i.getAddress())
              .append(", Phone: ").append(i.getPhone())
              .append("\n");
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------------------------------
    // Helper to parse int or return -1 if invalid
    // ------------------------------------------------------------------------------------------
    private int parseIntOrFail(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Entry point: Launch the GUI on the EDT.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Optionally test DB connection before launching:
            try {
                DBConnection.getConnection().close();
            } catch (Exception ex) {
                System.err.println("Warning: Could not connect to DB. " + ex.getMessage());
            }
            new MainApp();
        });
    }
}
