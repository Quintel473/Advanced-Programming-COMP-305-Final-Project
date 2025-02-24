package models;

import java.sql.Timestamp;

public class Package {
    private int id;
    private String trackingNumber;
    private int senderId;
    private int recipientId;
    private String status;
    private Timestamp createdAt;

    public Package(int id, String trackingNumber, int senderId, int recipientId, String status, Timestamp createdAt) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public int getSenderId() { return senderId; }
    public int getRecipientId() { return recipientId; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
}
