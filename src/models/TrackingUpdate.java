package models;

import java.sql.Timestamp;

public class TrackingUpdate {
    // Unique ID for the tracking update
    private int id;

    // ID of the package this update belongs to
    private int packageId;

    // Status message (e.g., "Shipped", "In Transit", "Delivered")
    private String status;

    // Timestamp of when the update occurred
    private Timestamp updateTime;

    // Constructor to initialize all fields
    public TrackingUpdate(int id, int packageId, String status, Timestamp updateTime) {
        this.id = id;
        this.packageId = packageId;
        this.status = status;
        this.updateTime = updateTime;
    }

    // Getter for tracking update ID
    public int getId() { return id; }

    // Getter for associated package ID
    public int getPackageId() { return packageId; }

    // Getter for current tracking status
    public String getStatus() { return status; }

    // Getter for the time the status was updated
    public Timestamp getUpdateTime() { return updateTime; }
}
