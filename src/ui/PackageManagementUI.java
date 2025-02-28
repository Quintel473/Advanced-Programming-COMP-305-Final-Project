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
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Title Label
        JLabel titleLabel = new JLabel("Manage Packages", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Package List Area
        packageListArea = new JTextArea();
        packageListArea.setEditable(false);
        packageListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        packageListArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollPane = new JScrollPane(packageListArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setLayout(new FlowLayout());

        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPackages();
            }
        });

        JButton addButton = createStyledButton("Add Package");
        addButton.setBackground(new Color(40, 167, 69)); // Green color for Add Package
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Code to add a package (to be implemented)
            }
        });

        JButton backButton = createStyledButton("Dashboard");
        backButton.setBackground(new Color(220, 53, 69)); // Red color for Back to Dashboard
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new DashboardUI(user).setVisible(true)); // Reopen the dashboard
            }
        });

        // Add Buttons
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadPackages();
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void loadPackages() {
        List<Package> packages = PackageService.getUserPackages(user.getId());
        packageListArea.setText("");
        for (Package p : packages) {
            packageListArea.append(
                    "ID: " + p.getId() +
                            " | Tracking: " + p.getTrackingNumber() +
                            " | Sender ID: " + p.getSenderId() +
                            " | Recipient ID: " + p.getRecipientId() +
                            " | Status: " + p.getStatus() +
                            " | Created At: " + p.getCreatedAt() +
                            "\n"
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "John Doe", "johndoe@example.com", "Admin", "Customer");
            new PackageManagementUI(testUser).setVisible(true);
        });
    }
}
