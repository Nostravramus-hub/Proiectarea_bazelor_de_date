package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Client extends JFrame {
    private JPanel panel1;
    private JTextField numeField;
    private JTextField dateContactField;
    private JTextField preferinteField;
    private JTable clientTable;

    public JPanel getPanel() {
        return panel1;
    }

    public Client() {
        setTitle("Client");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel1 = new JPanel(new BorderLayout());

        // Create input fields for adding a new client
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JLabel numeLabel = new JLabel("Nume:");
        JLabel dateContactLabel = new JLabel("Date Contact:");
        JLabel preferinteLabel = new JLabel("Preferinte:");

        numeField = new JTextField();
        dateContactField = new JTextField();
        preferinteField = new JTextField();

        inputPanel.add(numeLabel);
        inputPanel.add(numeField);
        inputPanel.add(dateContactLabel);
        inputPanel.add(dateContactField);
        inputPanel.add(preferinteLabel);
        inputPanel.add(preferinteField);

        // Create an "Insert" button
        JButton insertButton = new JButton("Adaugă Client");
        inputPanel.add(insertButton);

        // Add the input panel to the main panel
        panel1.add(inputPanel, BorderLayout.NORTH);

        // Create a JTable for displaying clients
        clientTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        clientTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Nume");
        tableModel.addColumn("Date Contact");
        tableModel.addColumn("Preferinte");

        // Fetch data from the "client" table
        fetchData(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(clientTable);
        panel1.add(scrollPane, BorderLayout.CENTER);

        // Action for the insert button
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the input data
                String nume = numeField.getText();
                String dateContactStr = dateContactField.getText();
                String preferinte = preferinteField.getText();

                if (nume.isEmpty() || dateContactStr.isEmpty() || preferinte.isEmpty()) {
                    JOptionPane.showMessageDialog(Client.this, "Toate câmpurile sunt obligatorii!");
                    return;
                }

                try {
                    int dateContact = Integer.parseInt(dateContactStr);

                    // Insert data into the database
                    insertData(nume, dateContact, preferinte);

                    // Update the table with new data
                    ((DefaultTableModel) clientTable.getModel()).setRowCount(0); // Clear existing rows
                    fetchData((DefaultTableModel) clientTable.getModel()); // Fetch updated data
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Client.this, "Date contact trebuie să fie un număr valid!");
                }
            }
        });
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Nume, Date_contact, Preferinte FROM client";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String nume = resultSet.getString("Nume");
                int dateContact = resultSet.getInt("Date_contact");
                String preferinte = resultSet.getString("Preferinte");

                // Add the row to the table model
                tableModel.addRow(new Object[]{nume, dateContact, preferinte});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    private void insertData(String nume, int dateContact, String preferinte) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "INSERT INTO client (Nume, Date_contact, Preferinte) VALUES ('" + nume + "', " + dateContact + ", '" + preferinte + "')";
            statement.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Client adăugat cu succes!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eroare la adăugarea clientului: " + e.getMessage());
        } finally {
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client clientView = new Client();
            clientView.setVisible(true);
        });
    }
}
