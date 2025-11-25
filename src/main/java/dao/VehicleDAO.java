package dao;

import model.VehicleType;
import util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // Get all vehicle types from DB, ordered by price (Small -> Medium -> SUV)
    public List<VehicleType> getAllVehicles() {
        List<VehicleType> list = new ArrayList<>();
        // Modified SQL: Added ORDER BY to fix the dropdown order
        String sql = "SELECT * FROM rental_vehicles ORDER BY daily_rate ASC";

        try (Connection conn = DBConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleType v = new VehicleType(
                        rs.getString("type"),
                        rs.getDouble("daily_rate"),
                        rs.getString("image_path")
                );
                list.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}