package services;

import database.DatabaseConnection;
import models.TrackingUpdate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackingService {

    public static boolean addTrackingUpdate(int packageId, String status) {
        String query = "INSERT INTO tracking_updates (package_id, status) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            stmt.setString(2, status);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<TrackingUpdate> getTrackingHistory(int packageId) {
        List<TrackingUpdate> updates = new ArrayList<>();
        String query = "SELECT * FROM tracking_updates WHERE package_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, packageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                updates.add(new TrackingUpdate(
                        rs.getInt("id"),
                        rs.getInt("package_id"),
                        rs.getString("status"),
                        rs.getTimestamp("update_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updates;
    }
}
