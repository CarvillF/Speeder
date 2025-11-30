package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clases.Vehiculo;
import database.DBConnection;
import database.UserType;

public class VehicleDAO {

    public static List<Vehiculo> getMyVehicles(String driverId) {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos WHERE transportista_cedula = ?";

        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehiculos.add(new Vehiculo(
                            rs.getInt("id_vehiculo"),
                            rs.getString("transportista_cedula"),
                            rs.getString("nombre_modelo"),
                            rs.getString("color"),
                            rs.getString("placa")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting vehicles: " + e.getMessage());
        }
        return vehiculos;
    }

    public static boolean createVehicle(Vehiculo vehicle) {
        String sql = "INSERT INTO vehiculos (transportista_cedula, nombre_modelo, color, placa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getTransportistaCedula());
            stmt.setString(2, vehicle.getNombreModelo());
            stmt.setString(3, vehicle.getColor());
            stmt.setString(4, vehicle.getPlaca());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating vehicle: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM vehiculos WHERE id_vehiculo = ?";
        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting vehicle: " + e.getMessage());
            return false;
        }
    }
}
