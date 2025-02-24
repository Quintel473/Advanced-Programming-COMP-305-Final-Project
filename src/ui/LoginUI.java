package ui;

import models.User;
import services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    public LoginUI() {
        setTitle("User Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterUI();
            }
        });
        add(registerButton);

        setVisible(true);
    }

    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = UserService.loginUser(email, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getName());
            new PackageManagementUI(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Email or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
