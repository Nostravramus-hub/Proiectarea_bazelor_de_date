package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Invitati extends JFrame {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField menuTypeField;
    private JTextField locationField;
    private JButton addButton;

    public JPanel getPanel() {
        return panel1;
    }

    public Invitati() {
        setTitle("Invitați");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel1 = new JPanel(new BorderLayout());

        // Create a JTable
        JTable invitatiTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        invitatiTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Nume");
        tableModel.addColumn("Tip Meniu");
        tableModel.addColumn("Loc");

        // Fetch data from the "Invitati" table
        fetchData(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(invitatiTable);
        panel1.add(scrollPane, BorderLayout.CENTER);

        // Create input fields and add button
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Nume Invitat:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Tip Meniu:"));
        menuTypeField = new JTextField(15);
        inputPanel.add(menuTypeField);

        inputPanel.add(new JLabel("Loc:"));
        locationField = new JTextField(15);
        inputPanel.add(locationField);

        addButton = new JButton("Adaugă Invitat");
        inputPanel.add(addButton);

        // Add input panel to the layout
        panel1.add(inputPanel, BorderLayout.SOUTH);

        // Set action listener for the add button
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String menuType = menuTypeField.getText();
            String location = locationField.getText();

            if (name.isEmpty() || menuType.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(Invitati.this, "Te rog completează toate câmpurile!");
                return;
            }

            // Call the method to add the new guest
            addInvitat(name, menuType, location);

            // Clear input fields
            nameField.setText("");
            menuTypeField.setText("");
            locationField.setText("");

            // Clear the table and fetch the updated data
            ((DefaultTableModel) invitatiTable.getModel()).setRowCount(0); // Clear table
            fetchData((DefaultTableModel) invitatiTable.getModel()); // Fetch updated data
        });
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Nume, Tip_meniu, Loc FROM Invitat";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Retrieve data from each row
                String nume = resultSet.getString("Nume");
                String meniu = resultSet.getString("Tip_meniu");
                String preferinte = resultSet.getString("Loc");

                // Add the row to the table model
                tableModel.addRow(new Object[]{nume, meniu, preferinte});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    // Method to add a new guest to the database
    private void addInvitat(String name, String menuType, String location) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();

            // Query to insert the new guest into the database
            String query = "INSERT INTO Invitat (Nume, Tip_meniu, Loc) VALUES ('"
                    + name + "', '" + menuType + "', '" + location + "')";
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Invitat adăugat cu succes!");
            } else {
                JOptionPane.showMessageDialog(this, "Eroare la adăugarea invitatului.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la adăugarea invitatului: " + e.getMessage());
        } finally {
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Invitati invitatiView = new Invitati();
            invitatiView.setVisible(true);
        });
    }
}
