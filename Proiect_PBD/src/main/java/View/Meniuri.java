package View;

import Model.ConnectionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Meniuri extends JFrame {
    private JPanel panel1;
    public JPanel getPanel() {
        return panel1;
    }
    public Meniuri() {
        setTitle("Meniuri");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        panel1 = new JPanel(new BorderLayout());

        JTable meniuriTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel();
        meniuriTable.setModel(tableModel);

        // Add columns to the table model
        tableModel.addColumn("Tip");
        tableModel.addColumn("Ingrediente");
        tableModel.addColumn("Gluten");
        tableModel.addColumn("Alergeni");

        fetchData(tableModel);

        JScrollPane scrollPane = new JScrollPane(meniuriTable);
        panel1.add(scrollPane, BorderLayout.CENTER);
    }

    private void fetchData(DefaultTableModel tableModel) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionBD.getConnection();
            statement = connection.createStatement();
            String query = "SELECT Tip, Ingrediente, Gluten, Alergeni FROM meniu";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                // Retrieve data from each row
                String tip = resultSet.getString("Tip");
                String ingrediente = resultSet.getString("Ingrediente");
                String gluten = resultSet.getString("Gluten");
                String alergeni = resultSet.getString("Alergeni");
                if(Integer.valueOf(gluten)==1)
                    gluten="Da";
                else
                    gluten="Nu";
                tableModel.addRow(new Object[]{tip, ingrediente, gluten,alergeni});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage());
        } finally {
            ConnectionBD.close(resultSet);
            ConnectionBD.close(statement);
            ConnectionBD.close(connection);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Meniuri meniuriView = new Meniuri();
            meniuriView.setVisible(true);
        });
    }
}
