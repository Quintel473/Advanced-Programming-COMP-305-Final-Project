package ui;

import models.Package;
import models.User;
import services.PackageService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerPackageManagementUI extends JFrame {
    private User user;
    private JTextArea packageListArea;

    public CustomerPackageManagementUI(User user) {
        this.user = user;
        setTitle("Package Management - " + user.getRole());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
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
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> loadPackages());

        JButton addButton = createStyledButton("Add Package");
        addButton.setBackground(new Color(40, 167, 69)); // Green color for Add Package
        addButton.addActionListener(e -> {
            String trackingNumber = JOptionPane.showInputDialog(this, "Enter Tracking Number:");
            int recipientId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Recipient ID:"));
            if (trackingNumber != null && !trackingNumber.trim().isEmpty()) {
                boolean success = PackageService.addPackage(trackingNumber, user.getId(), recipientId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Package added successfully!");
                    loadPackages(); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add package.");
                }
            }
        });

        JButton backButton = createStyledButton("Dashboard");
        backButton.setBackground(new Color(220, 53, 69)); // Red color for Back to Dashboard
        backButton.addActionListener(e -> {
            openDashboard();
            dispose(); // Close the current window
        });

        // Add Buttons
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backButton); // Ensure the Dashboard button is added and visible
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
                            " | Created At: " + p.getCreatedAt() + "\n"
            );
        }
    }

    private void openDashboard() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof CustomerDashboardUI) {
                window.toFront();
                return;
            }
        }
        if (user.getRole().equals("Admin")) {
            new AdminDashboardUI(user);
        } else if (user.getRole().equals("Delivery Company")) {
            new DeliveryCompanyDashboardUI(user);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "John Doe", "johndoe@example.com", "Admin", "Customer");
            new CustomerPackageManagementUI(testUser);
        });
    }
}
