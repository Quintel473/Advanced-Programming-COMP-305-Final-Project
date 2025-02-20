package ui;

import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardUI extends JFrame {
    private User user;

    public DashboardUI(User user) {
        this.user = user;
        setTitle("Dashboard - " + user.getRole());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);

        JButton trackPackageButton = new JButton("Track Packages");
        JButton logoutButton = new JButton("Logout");

        add(trackPackageButton);
        add(logoutButton);

        // Event Listeners
        trackPackageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Feature coming soon!");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginUI();
                dispose();
            }
        });

        setVisible(true);
    }
}
