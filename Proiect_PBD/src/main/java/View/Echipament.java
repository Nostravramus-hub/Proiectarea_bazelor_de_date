package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Echipament extends JFrame {
    private JPanel panel1;
    private JTextField listaField;
    private JComboBox<String> disponibilitateComboBox;
    private JButton addButton;

    public JPanel getPanel() {
        return panel1;
    }

    public Echipament() {
        setTitle("Echipament");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel1 = new JPanel(new BorderLayout());

        // Create a JTable
        JTable echipamentTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        echipamentTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Lista");
        tableModel.addColumn("Disponibilitate");

        // Fetch data from the "echipament" table
        fetchData(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(echipamentTable);
        panel1.add(scrollPane, BorderLayout.CENTER);

        // Create input fields and add button
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Echipament:"));
        listaField = new JTextField(15);
        inputPanel.add(listaField);

        inputPanel.add(new JLabel("Disponibilitate:"));
        disponibilitateComboBox = new JComboBox<>(new String[]{"Liber", "Ocupat"});
        inputPanel.add(disponibilitateComboBox);

        addButton = new JButton("Adaugă Echipament");
        inputPanel.add(addButton);

        // Add input panel to the layout
        panel1.add(inputPanel, BorderLayout.SOUTH);

        // Set action listener for the add button
        addButton.addActionListener(e -> {
            String lista = listaField.getText();
            String disponibilitate = (String) disponibilitateComboBox.getSelectedItem();

            if (lista.isEmpty() || disponibilitate == null) {
                JOptionPane.showMessageDialog(Echipament.this, "Te rog completează toate câmpurile!");
                return;
            }

            // Call the method to add the new equipment
            addEchipament(lista, disponibilitate);

            // Clear input fields
            listaField.setText("");
            disponibilitateComboBox.setSelectedIndex(0);

            // Clear the table and fetch the updated data
            ((DefaultTableModel) echipamentTable.getModel()).setRowCount(0); // Clear table
            fetchData((DefaultTableModel) echipamentTable.getModel()); // Fetch updated data
        });
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Lista, Disponibilitate FROM echipament";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Retrieve data from each row
                String lista = resultSet.getString("Lista");
                String disponibilitate = resultSet.getString("Disponibilitate");

                if (Integer.valueOf(disponibilitate) == 1)
                    disponibilitate = "Ocupat";
                else
                    disponibilitate = "Liber";

                // Add the row to the table model
                tableModel.addRow(new Object[]{lista, disponibilitate});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    // Method to add a new equipment to the database
    private void addEchipament(String lista, String disponibilitate) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();

            // Convert "Liber" to 0 and "Ocupat" to 1
            int disponibilitateInt = "Ocupat".equals(disponibilitate) ? 1 : 0;

            // Query to insert the new equipment into the database
            String query = "INSERT INTO echipament (Lista, Disponibilitate) VALUES ('"
                    + lista + "', " + disponibilitateInt + ")";
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Echipament adăugat cu succes!");
            } else {
                JOptionPane.showMessageDialog(this, "Eroare la adăugarea echipamentului.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la adăugarea echipamentului: " + e.getMessage());
        } finally {
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Echipament echipamentView = new Echipament();
            echipamentView.setVisible(true);
        });
    }
}
