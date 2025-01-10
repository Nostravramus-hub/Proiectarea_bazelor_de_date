package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView {
    private JButton formatieButton;
    private JPanel panel1;
    private JButton personalButton;
    private JButton clientButton;
    private JButton meniuriButton;
    private JButton invitatiButton;

    public MainView() {

        formatieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame formatieFrame = new JFrame("Formatie");
                formatieFrame.setContentPane(new Formatie().getPanel());
                formatieFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                formatieFrame.pack();
                formatieFrame.setVisible(true);
            }
        });

        personalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame personalFrame = new JFrame("Personal");
                personalFrame.setContentPane(new Personal().getPanel());
                personalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                personalFrame.pack();
                personalFrame.setVisible(true);
            }
        });

        clientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame clientFrame = new JFrame("Client");
                clientFrame.setContentPane(new Client().getPanel());
                clientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                clientFrame.pack();
                clientFrame.setVisible(true);
            }
        });

        meniuriButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame meniuriFrame = new JFrame("Meniuri");
                meniuriFrame.setContentPane(new Meniuri().getPanel());
                meniuriFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                meniuriFrame.pack();
                meniuriFrame.setVisible(true);
            }
        });

        invitatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame invitatiFrame = new JFrame("Invitati");
                invitatiFrame.setContentPane(new Invitati().getPanel());
                invitatiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                invitatiFrame.pack();
                invitatiFrame.setVisible(true);
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }
}
