package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Personal extends JFrame {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField roleField;
    private JTextField availabilityField;
    private JTextField tasksField;
    private JButton addButton;

    public JPanel getPanel() {
        return panel1;
    }

    public Personal() {
        setTitle("Personal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel1 = new JPanel(new BorderLayout());

        // Create a JTable
        JTable personalTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        personalTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Nume");
        tableModel.addColumn("Rol");
        tableModel.addColumn("Disponibil");
        tableModel.addColumn("Sarcini");

        // Fetch data from the "Personal" table
        fetchData(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(personalTable);
        panel1.add(scrollPane, BorderLayout.CENTER);

        // Create input fields and add button
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Nume Personal:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Rol:"));
        roleField = new JTextField(15);
        inputPanel.add(roleField);

        inputPanel.add(new JLabel("Disponibilitate (1 - Ocupat, 0 - Liber):"));
        availabilityField = new JTextField(15);
        inputPanel.add(availabilityField);

        inputPanel.add(new JLabel("Sarcini:"));
        tasksField = new JTextField(15);
        inputPanel.add(tasksField);

        addButton = new JButton("Adaugă Personal");
        inputPanel.add(addButton);

        // Add input panel to the layout
        panel1.add(inputPanel, BorderLayout.SOUTH);

        // Set action listener for the add button
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String role = roleField.getText();
            String availability = availabilityField.getText();
            String tasks = tasksField.getText();

            if (name.isEmpty() || role.isEmpty() || availability.isEmpty() || tasks.isEmpty()) {
                JOptionPane.showMessageDialog(Personal.this, "Te rog completează toate câmpurile!");
                return;
            }

            // Call the method to add the new personal member
            addPersonal(name, role, availability, tasks);

            // Clear input fields
            nameField.setText("");
            roleField.setText("");
            availabilityField.setText("");
            tasksField.setText("");

            // Clear the table and fetch the updated data
            ((DefaultTableModel) personalTable.getModel()).setRowCount(0); // Clear table
            fetchData((DefaultTableModel) personalTable.getModel()); // Fetch updated data
        });
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Nume, Rol, Disponibilitate, Sarcini_alocate FROM Personal";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Retrieve data from each row
                String nume = resultSet.getString("Nume");
                String rol = resultSet.getString("Rol");
                String disponibil = resultSet.getString("Disponibilitate");
                String sarcini = resultSet.getString("Sarcini_alocate");
                if (Integer.valueOf(disponibil) == 1)
                    disponibil = "Ocupat";
                else
                    disponibil = "Liber";
                // Add the row to the table model
                tableModel.addRow(new Object[]{nume, rol, disponibil, sarcini});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    // Method to add a new personal member to the database
    private void addPersonal(String name, String role, String availability, String tasks) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();

            // Query to insert the new personal member into the database
            String query = "INSERT INTO Personal (Nume, Rol, Disponibilitate, Sarcini_alocate) VALUES ('"
                    + name + "', '" + role + "', '" + availability + "', '" + tasks + "')";
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Personal adăugat cu succes!");
            } else {
                JOptionPane.showMessageDialog(this, "Eroare la adăugarea personalului.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la adăugarea personalului: " + e.getMessage());
        } finally {
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Personal personalView = new Personal();
            personalView.setVisible(true);
        });
    }
}
