package models;

import java.sql.Timestamp;

public class Package {
    // Unique ID for the package
    private int id;

    // Tracking number of the package (e.g., "123ABC")
    private String trackingNumber;

    // ID of the sender (User who sent the package)
    private int senderId;

    // ID of the recipient (User receiving the package)
    private int recipientId;

    // Current status of the package (e.g., "Pending", "Shipped", "Delivered")
    private String status;

    // Timestamp of when the package was created/added
    private Timestamp createdAt;

    // Constructor to initialize all fields
    public Package(int id, String trackingNumber, int senderId, int recipientId, String status, Timestamp createdAt) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getter for package ID
    public int getId() { return id; }

    // Getter for tracking number
    public String getTrackingNumber() { return trackingNumber; }

    // Getter for sender ID
    public int getSenderId() { return senderId; }

    // Getter for recipient ID
    public int getRecipientId() { return recipientId; }

    // Getter for the current package status
    public String getStatus() { return status; }

    // Getter for the timestamp when the package was created
    public Timestamp getCreatedAt() { return createdAt; }
}
