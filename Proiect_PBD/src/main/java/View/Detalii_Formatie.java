package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class Detalii_Formatie extends JFrame {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField genreField;
    private JTextField requirementsField;
    private JTextField scheduleField;
    private JButton addButton;

    public JPanel getPanel() {
        return panel1;
    }

    public Detalii_Formatie() {
        setTitle("Detalii Formatie");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel1 = new JPanel(new BorderLayout());

        // Create a JTable
        JTable detaliiFormatieTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        detaliiFormatieTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Nume");
        tableModel.addColumn("Gen");
        tableModel.addColumn("Cerinte Tehnice");
        tableModel.addColumn("Interval Orar");

        // Fetch data from the "formatie" table
        fetchData(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(detaliiFormatieTable);
        panel1.add(scrollPane, BorderLayout.CENTER);

        // Create input fields and add button
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Nume Formatie:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Gen:"));
        genreField = new JTextField(15);
        inputPanel.add(genreField);

        inputPanel.add(new JLabel("Cerinte Tehnice:"));
        requirementsField = new JTextField(15);
        inputPanel.add(requirementsField);

        inputPanel.add(new JLabel("Interval Orar (ex: '2025-01-07 14:00:00'):"));
        scheduleField = new JTextField(15);
        inputPanel.add(scheduleField);

        addButton = new JButton("Adaugă Formatie");
        inputPanel.add(addButton);

        // Add input panel to the layout
        panel1.add(inputPanel, BorderLayout.SOUTH);

        // Set action listener for the add button
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String genre = genreField.getText();
            String requirements = requirementsField.getText();
            String schedule = scheduleField.getText();

            if (name.isEmpty() || genre.isEmpty() || requirements.isEmpty() || schedule.isEmpty()) {
                JOptionPane.showMessageDialog(Detalii_Formatie.this, "Te rog completează toate câmpurile!");
                return;
            }

            // Call the method to add a new formation
            addFormation(name, genre, requirements, schedule);

            // Clear input fields
            nameField.setText("");
            genreField.setText("");
            requirementsField.setText("");
            scheduleField.setText("");

            // Clear the table and fetch the updated data
            ((DefaultTableModel) detaliiFormatieTable.getModel()).setRowCount(0); // Clear table
            fetchData((DefaultTableModel) detaliiFormatieTable.getModel()); // Fetch updated data
        });
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Nume, Gen, Cerinte_Tehnice, Interval_Orar FROM formatie";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Retrieve data from each row
                String nume = resultSet.getString("Nume");
                String gen = resultSet.getString("Gen");
                String cerinte = resultSet.getString("Cerinte_Tehnice");
                Timestamp interval = resultSet.getTimestamp("Interval_Orar");

                // Add the row to the table model
                tableModel.addRow(new Object[]{nume, gen, cerinte, interval});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    // Method to add a new formation to the database
    private void addFormation(String name, String genre, String requirements, String schedule) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();

            // Query to insert the new formation into the database
            String query = "INSERT INTO formatie (Nume, Gen, Cerinte_Tehnice, Interval_Orar) VALUES ('"
                    + name + "', '" + genre + "', '" + requirements + "', '" + schedule + "')";
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Formatie adăugată cu succes!");
            } else {
                JOptionPane.showMessageDialog(this, "Eroare la adăugarea formației.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la adăugarea formației: " + e.getMessage());
        } finally {
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Detalii_Formatie detaliiFormatieView = new Detalii_Formatie();
            detaliiFormatieView.setVisible(true);
        });
    }
}
