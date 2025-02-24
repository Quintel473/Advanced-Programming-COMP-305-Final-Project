package ui;

import services.UserService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUI extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton registerButton;

    public RegisterUI() {
        setTitle("User Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Customer", "Delivery Company", "Admin"});
        add(roleBox);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        add(registerButton);

        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (UserService.registerUser(name, email, password, role)) {
            JOptionPane.showMessageDialog(this, "Registration Successful!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
