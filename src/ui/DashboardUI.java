package ui;

import models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardUI extends JFrame {
    private final User user;

    public DashboardUI(User user) {
        this.user = user; // Ensure user is used
        setTitle("Dashboard - " + user.getName());
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Welcome, Label
        JLabel titleLabel = new JLabel("Welcome, " + user.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Only 2 buttons now
        buttonPanel.setBackground(Color.WHITE);

        // Manage Packages Button
        JButton managePackagesButton = createStyledButton("📦 Manage Packages");
        managePackagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PackageManagementUI(user).setVisible(true);
            }
        });

        // Logout Button
        JButton logoutButton = createStyledButton("🚪 Logout");
        logoutButton.setBackground(new Color(220, 53, 69)); // Red Color for Logout
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true)); // Reopen login page
            }
        });

        // Add buttons to panel
        buttonPanel.add(managePackagesButton);
        buttonPanel.add(logoutButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        // Add panel to frame
        add(panel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        return button;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Use Nimbus for modern styling
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            User testUser = new User(1, "John Doe", "johndoe@example.com", "Admin", "Customer");
            DashboardUI dashboard = new DashboardUI(testUser);
            dashboard.setVisible(true);
        });
    }
}
