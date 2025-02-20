package ui;

import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PackageManagementUI extends JFrame {
    private User user;
    private JTextArea packageListArea;

    public PackageManagementUI(User user) {
        this.user = user;
        setTitle("Package Management - " + user.getRole());
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        packageListArea = new JTextArea();
        packageListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPackages();
            }
        });

        add(refreshButton, BorderLayout.SOUTH);

        loadPackages();
        setVisible(true);
    }

    private void loadPackages() {
        List<Package> packages = PackageService.getUserPackages(user.getId());
        packageListArea.setText("");
        for (Package p : packages) {
            packageListArea.append("Tracking: " + p.getTrackingNumber() + " | Status: " + p.getStatus() + "\n");
        }
    }
}
