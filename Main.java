import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.*;

public class Main {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/project";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "12345"; // Replace with your MySQL password

    public static void main(String[] args) {
        openLoginPage();
    }

    public static void openLoginPage() {
        JFrame frame = new JFrame("Login Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GREEN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        frame.add(panel);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JLabel categoryLabel = new JLabel("Category:");
        JRadioButton patientRadioButton = new JRadioButton("Patient");
        JRadioButton doctorRadioButton = new JRadioButton("Doctor");
        ButtonGroup categoryGroup = new ButtonGroup();
        categoryGroup.add(patientRadioButton);
        categoryGroup.add(doctorRadioButton);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.BLUE);
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.RED);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        JPanel categoryPanel = new JPanel();
        categoryPanel.add(patientRadioButton);
        categoryPanel.add(doctorRadioButton);
        panel.add(categoryPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        gbc.gridy = 4;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> openRegisterPage());
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            boolean isPatient = patientRadioButton.isSelected();
            boolean isDoctor = doctorRadioButton.isSelected();

            if (email.isEmpty() || password.isEmpty() || (!isPatient && !isDoctor)) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields and select a category.");
                return;
            }

            if (isDoctor) {
                openDoctorPanel(email, password);
            } else if (isPatient) {
                openPatientPanel(email);
            }
        });

        frame.setVisible(true);
    }

    private static void openRegisterPage() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.CYAN);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        registerFrame.add(panel);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField(15);
        JLabel categoryLabel = new JLabel("Category:");
        JRadioButton patientRadioButton = new JRadioButton("Patient");
        JRadioButton doctorRadioButton = new JRadioButton("Doctor");
        ButtonGroup categoryGroup = new ButtonGroup();
        categoryGroup.add(patientRadioButton);
        categoryGroup.add(doctorRadioButton);

        JLabel doctorLabel = new JLabel("Select Doctor:");
        JComboBox<String> doctorComboBox = new JComboBox<>(fetchDoctors());
        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.RED);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        JPanel categoryPanel = new JPanel();
        categoryPanel.add(patientRadioButton);
        categoryPanel.add(doctorRadioButton);
        panel.add(categoryPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(doctorLabel, gbc);

        gbc.gridx = 1;
        panel.add(doctorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Please fill all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(registerFrame, "Passwords do not match.");
                return;
            }

            if (patientRadioButton.isSelected()) {
                String doctorEmail = (String) doctorComboBox.getSelectedItem();
                registerPatient(name, email, password, doctorEmail);
            } else if (doctorRadioButton.isSelected()) {
                registerDoctor(name, email, password);
            }

            JOptionPane.showMessageDialog(registerFrame, "Registration successful! You can now log in.");
            registerFrame.dispose();
        });

        registerFrame.setVisible(true);
    }

    private static void registerDoctor(String name, String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO doctors (email, name, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerPatient(String name, String email, String password, String doctorEmail) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO patients (email, name, password, doctor_email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, password);
            stmt.setString(4, doctorEmail);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String[] fetchDoctors() {
        ArrayList<String> doctors = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT email FROM doctors";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                doctors.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors.toArray(new String[0]);
    }

    public static void openDoctorPanel(String doctorEmail, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM doctors WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, doctorEmail);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Doctor login failed.");
                return;
            }

            JFrame doctorFrame = new JFrame("Doctor Panel");
            doctorFrame.setSize(500, 400);

            JPanel panel = new JPanel(new BorderLayout());
            doctorFrame.add(panel);

            JButton viewRecordsButton = new JButton("View Patient Records");
            viewRecordsButton.addActionListener(e -> {
                String records = fetchPatientRecords(doctorEmail);
                JOptionPane.showMessageDialog(doctorFrame, records);
            });

            panel.add(viewRecordsButton, BorderLayout.CENTER);

            doctorFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String fetchPatientRecords(String doctorEmail) {
        StringBuilder records = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT name, disease FROM patients WHERE doctor_email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, doctorEmail);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                records.append(rs.getString("name")).append(" - ").append(rs.getString("disease")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records.toString();
    }

    private static void openPatientPanel(String patientEmail) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM patients WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, patientEmail);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "Patient not found.");
                return;
            }

            JFrame patientFrame = new JFrame("Patient Panel");
            patientFrame.setSize(500, 400);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            patientFrame.add(panel);

            JLabel selectDoctorLabel = new JLabel("Select a Doctor:");
            JComboBox<String> doctorComboBox = new JComboBox<>(fetchDoctors());

            JLabel selectDiseaseLabel = new JLabel("Select a Disease:");
            JComboBox<String> diseaseComboBox = new JComboBox<>(new String[]{"Diabetes", "Hypertension", "Cold and Flu"});

            JButton showRecommendationsButton = new JButton("Show Recommendations");

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(selectDoctorLabel, gbc);

            gbc.gridx = 1;
            panel.add(doctorComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(selectDiseaseLabel, gbc);

            gbc.gridx = 1;
            panel.add(diseaseComboBox, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            panel.add(showRecommendationsButton, gbc);

            showRecommendationsButton.addActionListener(e -> {
                String selectedDisease = (String) diseaseComboBox.getSelectedItem();
                updatePatientDisease(patientEmail, selectedDisease);
                showRecommendations(selectedDisease);
            });

            patientFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePatientDisease(String patientEmail, String disease) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE patients SET disease = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, disease);
            stmt.setString(2, patientEmail);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showRecommendations(String disease) {
        JFrame recommendationsFrame = new JFrame("Recommendations for " + disease);
        recommendationsFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(Color.CYAN);
        recommendationsFrame.add(panel);

        String[] medicines = getMedicinesForDisease(disease);
        JPanel medicinePanel = createTitledPanel("Recommended Medicines", medicines);
        panel.add(medicinePanel);

        String[] foods = getFoodsForDisease(disease);
        JPanel foodPanel = createTitledPanel("Recommended Foods", foods);
        panel.add(foodPanel);

        String[] fruits = getFruitsForDisease(disease);
        JPanel fruitPanel = createTitledPanel("Recommended Fruits", fruits);
        panel.add(fruitPanel);

        JButton showPrecautionsButton = new JButton("Show Precautions");
        showPrecautionsButton.addActionListener(e -> showPrecautions(disease));
        panel.add(showPrecautionsButton);

        recommendationsFrame.setVisible(true);
    }

    private static void showPrecautions(String disease) {
        JFrame precautionsFrame = new JFrame("Precautions for " + disease);
        precautionsFrame.setSize(500, 400);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(Color.PINK);
        precautionsFrame.add(panel);

        ArrayList<String> precautions = getPrecautionsForDisease(disease);
        for (String precaution : precautions) {
            panel.add(new JLabel(precaution));
        }

        precautionsFrame.setVisible(true);
    }

    private static JPanel createTitledPanel(String title, String[] items) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        for (String item : items) {
            panel.add(new JLabel(item));
        }
        return panel;
    }

    private static String[] getMedicinesForDisease(String disease) {
        switch (disease) {
            case "Diabetes":
                return new String[]{"Metformin", "Insulin", "Glipizide"};
            case "Hypertension":
                return new String[]{"Amlodipine", "Losartan", "Hydrochlorothiazide"};
            case "Cold and Flu":
                return new String[]{"Panadol", "Cough Syrups", "Paracetamol"};
            default:
                return new String[]{};
        }
    }

    private static String[] getFoodsForDisease(String disease) {
        switch (disease) {
            case "Diabetes":
                return new String[]{"Whole Grains", "Leafy Greens", "Garlic"};
            case "Hypertension":
                return new String[]{"Unsalted Nuts", "Honey", "Warm Soups"};
            case "Cold and Flu":
                return new String[]{"Bananas", "Oats", "Green Vegetables"};
            default:
                return new String[]{};
        }
    }

    private static String[] getFruitsForDisease(String disease) {
        switch (disease) {
            case "Diabetes":
                return new String[]{"Oranges", "Bananas", "Strawberries"};
            case "Hypertension":
                return new String[]{"Lemon", "Apples", "Berries"};
            case "Cold and Flu":
                return new String[]{"Cherries", "Bananas", "Watermelon"};
            default:
                return new String[]{};
        }
    }

    private static ArrayList<String> getPrecautionsForDisease(String disease) {
        ArrayList<String> precautions = new ArrayList<>();
        switch (disease) {
            case "Diabetes":
                precautions.add("1. Rest well.");
                precautions.add("2. Drink plenty of fluids.");
                precautions.add("3. Avoid contact with others.");
                precautions.add("4. Eat nutritious food.");
                break;
            case "Hypertension":
                precautions.add("1. Keep warm.");
                precautions.add("2. Stay hydrated.");
                precautions.add("3. Drink herbal teas.");
                precautions.add("4. Limit Sodium intake.");
                break;
            case "Cold and Flu":
                precautions.add("1. Get adequate rest.");
                precautions.add("2. Avoid bright lights.");
                precautions.add("3. Avoid close touch with others.");
                precautions.add("4. Cover coughs and sneezes.");
                break;
            default:
                break;
        }
        return precautions;
    }
}