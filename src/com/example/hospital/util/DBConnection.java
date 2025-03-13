package com.example.hospital.util;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

/**
 * A single class that:
 * 1) Connects to MariaDB (user=edvin, pass=last).
 * 2) Creates DB "hospitaldatabase" if not exists.
 * 3) Builds tables matching the 6 CSV columns + 2 subclass tables (specialists, insured_patients).
 * 4) Loads each CSV from the classpath.
 * 5) Then extracts data from doctors/patients to fill specialists/insured_patients
 *    if certain columns (e.g. specialization, insurance_id) match.
 * 6) Skips re-init on repeated calls in the same JVM (via static boolean).
 */
public class DBConnection {

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "hospitaldatabase";
    private static final String DB_USER = "edvin";
    private static final String DB_PASS = "last";

    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB JDBC driver not found.", e);
        }

        // 1) Possibly create the DB
        String initUrl = String.format("jdbc:mariadb://%s:%s/", DB_HOST, DB_PORT);
        try (Connection conn = DriverManager.getConnection(initUrl, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }

        // 2) Connect to that DB
        String finalUrl = String.format("jdbc:mariadb://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
        Connection finalConn = DriverManager.getConnection(finalUrl, DB_USER, DB_PASS);

        // 3) Only once: create tables, load CSV, extract subclass data
        if (!initialized) {
            initialized = true;
            createTables(finalConn);
            loadCsvData(finalConn);
            extractSubclassData(finalConn);
        }

        return finalConn;
    }

    /**
     * Creates the 6 base tables + "specialists" + "insured_patients".
     */
    private static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {

            // doctors
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS doctors ("
                            + "  doctor_id INT PRIMARY KEY,"
                            + "  firstname VARCHAR(50),"
                            + "  surname VARCHAR(50),"
                            + "  address VARCHAR(100),"
                            + "  email VARCHAR(100),"
                            + "  specialization VARCHAR(100)"
                            + ") ENGINE=InnoDB"
            );

            // specialists referencing doctors
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS specialists ("
                            + "  specialist_id INT PRIMARY KEY,"
                            + "  experience INT,"
                            + "  FOREIGN KEY (specialist_id) REFERENCES doctors(doctor_id)"
                            + "    ON DELETE CASCADE ON UPDATE CASCADE"
                            + ") ENGINE=InnoDB"
            );

            // insurance
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS insurance ("
                            + "  insurance_id INT PRIMARY KEY,"
                            + "  company VARCHAR(100),"
                            + "  address VARCHAR(100),"
                            + "  phone VARCHAR(50)"
                            + ") ENGINE=InnoDB"
            );

            // patients
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS patients ("
                            + "  patient_id INT PRIMARY KEY,"
                            + "  firstname VARCHAR(50),"
                            + "  surname VARCHAR(50),"
                            + "  postcode VARCHAR(50),"
                            + "  address VARCHAR(100),"
                            + "  phone VARCHAR(50),"
                            + "  email VARCHAR(100),"
                            + "  insurance_id INT,"
                            + "  FOREIGN KEY (insurance_id) REFERENCES insurance(insurance_id)"
                            + ") ENGINE=InnoDB"
            );

            // insured_patients referencing patients
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS insured_patients ("
                            + "  patient_id INT PRIMARY KEY,"
                            + "  insurance_type VARCHAR(50),"
                            + "  insurance_company_name VARCHAR(100),"
                            + "  duration_of_insurance INT,"
                            + "  FOREIGN KEY (patient_id) REFERENCES patients(patient_id)"
                            + "    ON DELETE CASCADE ON UPDATE CASCADE"
                            + ") ENGINE=InnoDB"
            );

            // drugs
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS drugs ("
                            + "  drug_id INT PRIMARY KEY,"
                            + "  name VARCHAR(100),"
                            + "  sideeffects VARCHAR(200),"
                            + "  benefits VARCHAR(300)"
                            + ") ENGINE=InnoDB"
            );

            // visits
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS visits ("
                            + "  visit_id INT AUTO_INCREMENT PRIMARY KEY,"
                            + "  patientID INT NOT NULL,"
                            + "  doctorID INT NOT NULL,"
                            + "  dateofvisit DATE,"
                            + "  symptoms VARCHAR(200),"
                            + "  diagnosis VARCHAR(200),"
                            + "  FOREIGN KEY (patientID) REFERENCES patients(patient_id),"
                            + "  FOREIGN KEY (doctorID) REFERENCES doctors(doctor_id)"
                            + ") ENGINE=InnoDB"
            );

            // prescriptions
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS prescriptions ("
                            + "  prescriptionID INT PRIMARY KEY,"
                            + "  dateprescribed DATE,"
                            + "  dosage VARCHAR(100),"
                            + "  duration VARCHAR(100),"
                            + "  comment VARCHAR(200),"
                            + "  drugID INT,"
                            + "  doctorID INT,"
                            + "  patientID INT,"
                            + "  FOREIGN KEY (drugID) REFERENCES drugs(drug_id),"
                            + "  FOREIGN KEY (doctorID) REFERENCES doctors(doctor_id),"
                            + "  FOREIGN KEY (patientID) REFERENCES patients(patient_id)"
                            + ") ENGINE=InnoDB"
            );

        }
    }

    /** Loads the 6 base CSV files for doctors, patients, etc. No direct CSV for specialists or insured_patients. */
    private static void loadCsvData(Connection conn) throws SQLException {
        loadDoctorsCSV(conn, "/com/example/hospital/csv/Doctor.csv");
        loadInsuranceCSV(conn, "/com/example/hospital/csv/Insurance.csv");
        loadPatientsCSV(conn, "/com/example/hospital/csv/Patient.csv");
        loadDrugCSV(conn, "/com/example/hospital/csv/Drug.csv");
        loadVisitCSV(conn, "/com/example/hospital/csv/Visit.csv");
        loadPrescriptionCSV(conn, "/com/example/hospital/csv/Prescription.csv");
    }

    /**
     * Extract data for specialists and insured_patients from the existing doctors/patients
     * by reading the 'specialization' column or a non-zero 'insurance_id'.
     */
    private static void extractSubclassData(Connection conn) throws SQLException {
        // 1) If a doctor row has a non-empty 'specialization', also insert into specialists with random 'experience'.
        String sqlDocs = "SELECT doctor_id, specialization FROM doctors";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlDocs)) {

            String insertSpec = "INSERT IGNORE INTO specialists (specialist_id, experience) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSpec)) {
                Random rand = new Random();
                while (rs.next()) {
                    int docId = rs.getInt("doctor_id");
                    String spec = rs.getString("specialization");
                    if (spec != null && !spec.trim().isEmpty()) {
                        // We'll say if there's a specialization, it's a specialist
                        // Insert with some random experience
                        int exp = 1 + rand.nextInt(30);
                        ps.setInt(1, docId);
                        ps.setInt(2, exp);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
        }

        // 2) If a patient row has a non-zero insurance_id, we also consider them insured. We'll fill
        //    'insurance_type', 'insurance_company_name', 'duration_of_insurance' with partial or random data.
        String sqlPats = "SELECT p.patient_id, p.insurance_id, i.company FROM patients p "
                + "LEFT JOIN insurance i ON p.insurance_id = i.insurance_id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sqlPats)) {

            String insertInsPat = "INSERT IGNORE INTO insured_patients (patient_id, insurance_type, insurance_company_name, duration_of_insurance) "
                    + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertInsPat)) {
                Random rand = new Random();
                String[] types = {"Basic", "Gold", "Premium"};
                while (rs.next()) {
                    int patId = rs.getInt("patient_id");
                    int insId = rs.getInt("insurance_id");
                    String company = rs.getString("company"); // from insurance table
                    if (insId != 0) {
                        // We'll say if insurance_id != 0 => it's an InsuredPatient
                        String chosenType = types[rand.nextInt(types.length)];
                        int dur = 6 + rand.nextInt(24); // 6..29 months
                        ps.setInt(1, patId);
                        ps.setString(2, chosenType);
                        // if the insurance table had "company," we use it as insurance_company_name
                        ps.setString(3, (company != null && !company.trim().isEmpty()) ? company : "PlaceholderCo");
                        ps.setInt(4, dur);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
        }
    }

    // -----------------------------------------------------------------------
    // CSV loaders for each base table
    // -----------------------------------------------------------------------
    private static void loadDoctorsCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL = "INSERT IGNORE INTO doctors (doctor_id, firstname, surname, address, email, specialization)"
                + " VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {

            if (is == null) {
                System.err.println("Doctor CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 6) {
                    System.err.println("Malformed doctor line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setString(2, cols[1].trim());
                ps.setString(3, cols[2].trim());
                ps.setString(4, cols[3].trim());
                ps.setString(5, cols[4].trim());
                ps.setString(6, cols[5].trim());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadInsuranceCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL = "INSERT IGNORE INTO insurance (insurance_id, company, address, phone)"
                + " VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Insurance CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 4) {
                    System.err.println("Malformed insurance line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setString(2, cols[1].trim());
                ps.setString(3, cols[2].trim());
                ps.setString(4, cols[3].trim());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPatientsCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL = "INSERT IGNORE INTO patients (patient_id, firstname, surname, postcode, address, phone, email, insurance_id)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Patient CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 8) {
                    System.err.println("Malformed patient line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setString(2, cols[1].trim());
                ps.setString(3, cols[2].trim());
                ps.setString(4, cols[3].trim());
                ps.setString(5, cols[4].trim());
                ps.setString(6, cols[5].trim());
                ps.setString(7, cols[6].trim());
                ps.setInt(8, parseIntOrZero(cols[7]));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadDrugCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL = "INSERT IGNORE INTO drugs (drug_id, name, sideeffects, benefits) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Drug CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 4) {
                    System.err.println("Malformed drug line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setString(2, cols[1].trim());
                ps.setString(3, cols[2].trim());
                ps.setString(4, cols[3].trim());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadVisitCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL = "INSERT INTO visits (patientID, doctorID, dateofvisit, symptoms, diagnosis) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Visit CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 5) {
                    System.err.println("Malformed visit line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setInt(2, parseIntOrZero(cols[1]));
                ps.setDate(3, parseSqlDate(cols[2]));
                ps.setString(4, cols[3].trim());
                ps.setString(5, cols[4].trim());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPrescriptionCSV(Connection conn, String resourcePath) throws SQLException {
        String insertSQL =
                "INSERT IGNORE INTO prescriptions ("
                        + " prescriptionID, dateprescribed, dosage, duration, comment, drugID, doctorID, patientID"
                        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL);
             InputStream is = DBConnection.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Prescription CSV not found: " + resourcePath);
                return;
            }
            Scanner sc = new Scanner(is, "UTF-8");
            if (sc.hasNextLine()) sc.nextLine(); // skip header

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(",", -1);
                if (cols.length < 8) {
                    System.err.println("Malformed prescription line: " + line);
                    continue;
                }
                ps.setInt(1, parseIntOrZero(cols[0]));
                ps.setDate(2, parseSqlDate(cols[1]));
                ps.setString(3, cols[2].trim());
                ps.setString(4, cols[3].trim());
                ps.setString(5, cols[4].trim());
                ps.setInt(6, parseIntOrZero(cols[5]));
                ps.setInt(7, parseIntOrZero(cols[6]));
                ps.setInt(8, parseIntOrZero(cols[7]));
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void fillSpecialistsFromDoctors(Connection conn) throws SQLException {
        String queryDocs = "SELECT doctor_id, specialization FROM doctors";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(queryDocs)) {

            String insertSpec = "INSERT IGNORE INTO specialists (specialist_id, experience) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSpec)) {
                Random rnd = new Random();
                while (rs.next()) {
                    int docId = rs.getInt("doctor_id");
                    String spec = rs.getString("specialization");
                    // if the CSV line had a specialization, let's treat them as a specialist
                    if (spec != null && !spec.trim().isEmpty()) {
                        int exp = 1 + rnd.nextInt(30); // random experience
                        ps.setInt(1, docId);
                        ps.setInt(2, exp);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
        }
    }

    private static void fillInsuredPatientsFromPatients(Connection conn) throws SQLException {
        String queryPats = "SELECT p.patient_id, p.insurance_id, i.company "
                + "FROM patients p "
                + "LEFT JOIN insurance i ON p.insurance_id = i.insurance_id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(queryPats)) {

            String insertInsPat = "INSERT IGNORE INTO insured_patients (patient_id, insurance_type, insurance_company_name, duration_of_insurance) "
                    + "VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertInsPat)) {
                Random rnd = new Random();
                String[] insTypes = {"Basic", "Gold", "Premium"};
                while (rs.next()) {
                    int patId = rs.getInt("patient_id");
                    int insId = rs.getInt("insurance_id");
                    String co = rs.getString("company"); // from insurance table
                    if (insId != 0) {
                        // that means the CSV row had some insurance ID => let's treat them as insured
                        String chosenType = insTypes[rnd.nextInt(insTypes.length)];
                        int dur = 6 + rnd.nextInt(24);  // 6..29 months
                        ps.setInt(1, patId);
                        ps.setString(2, chosenType);
                        // if the insurance table had "company", let's store it as insurance_company_name
                        ps.setString(3, (co == null || co.trim().isEmpty()) ? "PlaceholderInc" : co);
                        ps.setInt(4, dur);
                        ps.addBatch();
                    }
                }
                ps.executeBatch();
            }
        }
    }

    // parse helper
    private static int parseIntOrZero(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static java.sql.Date parseSqlDate(String s) {
        try {
            return java.sql.Date.valueOf(s.trim());
        } catch (Exception e) {
            return new java.sql.Date(System.currentTimeMillis());
        }
    }
}
