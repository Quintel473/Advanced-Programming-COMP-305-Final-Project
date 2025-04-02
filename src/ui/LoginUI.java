package ui;

import services.UserService;
import models.User;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginUI() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        JButton registerButton = createStyledButton("Register");
        registerButton.setBackground(new Color(40, 167, 69)); // Green for Register
        registerButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new RegisterUI().setVisible(true));
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        User user = UserService.loginUser(email, password); // Authenticate user
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

            // Directly redirect to the appropriate dashboard based on the user's role
            SwingUtilities.invokeLater(() -> {
                if ("Admin".equalsIgnoreCase(user.getRole())) { // Admin Role
                    new AdminDashboardUI(user).setVisible(true);
                } else if ("Customer".equalsIgnoreCase(user.getRole())) { // Customer Role
                    new CustomerDashboardUI(user).setVisible(true);
                } else if ("Delivery Company".equalsIgnoreCase(user.getRole())) { // Delivery Company Role
                    new DeliveryCompanyDashboardUI(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Role not recognized!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Modern Look
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}