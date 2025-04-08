package models;

public class User {
    // Fields representing user information
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;

    // Constructor to initialize all fields
    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getter for user ID
    public int getId() { return id; }

    // Getter for user name
    public String getName() { return name; }

    // Getter for user email
    public String getEmail() { return email; }

    // Getter for user password
    public String getPassword() { return password; }

    // Getter for user role (e.g., admin, customer, etc.)
    public String getRole() { return role; }

    // toString method for debugging or logging user details
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
