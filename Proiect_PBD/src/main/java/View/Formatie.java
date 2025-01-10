package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Formatie {
    private JButton echipamentButton;
    private JPanel panel1;
    private JButton detaliiFormatieButton;
    public JPanel getPanel() {
        return panel1;
    }
    public Formatie() {

        echipamentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deschidem fereastra Echipament
                JFrame echipamentFrame = new JFrame("Echipament");
                echipamentFrame.setContentPane(new Echipament().getPanel());
                echipamentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                echipamentFrame.pack();
                echipamentFrame.setVisible(true);
            }
        });

        // Adăugăm un ActionListener pentru detaliiFormatieButton
        detaliiFormatieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Deschidem fereastra Detalii_Formatie
                JFrame detaliiFormatieFrame = new JFrame("Detalii Formatie");
                detaliiFormatieFrame.setContentPane(new Detalii_Formatie().getPanel());
                detaliiFormatieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                detaliiFormatieFrame.pack();
                detaliiFormatieFrame.setVisible(true);
            }
        });
    }
}
