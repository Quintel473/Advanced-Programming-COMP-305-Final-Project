package models;

import java.sql.Timestamp;

public class TrackingUpdate {
    private int id;
    private int packageId;
    private String status;
    private Timestamp updateTime;

    public TrackingUpdate(int id, int packageId, String status, Timestamp updateTime) {
        this.id = id;
        this.packageId = packageId;
        this.status = status;
        this.updateTime = updateTime;
    }

    // Getters
    public int getId() { return id; }
    public int getPackageId() { return packageId; }
    public String getStatus() { return status; }
    public Timestamp getUpdateTime() { return updateTime; }
}
