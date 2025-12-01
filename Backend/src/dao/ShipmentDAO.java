package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import clases.Envio;
import clases.Paquete;
import database.DBConnection;
import database.UserType;

public class ShipmentDAO {

    public static List<Envio> getAssignedShipments(String driverId) {
        List<Envio> envios = new ArrayList<>();
        String sql = "SELECT e.*, p.* FROM envios e JOIN paquetes p ON e.id_paquete = p.id_paquete WHERE e.transportista_cedula = ?";

        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    envios.add(mapResultSetToEnvio(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting assigned shipments: " + e.getMessage());
        }
        return envios;
    }

    public static boolean updateShipmentState(int shipmentId, String state) {
        String sql = "UPDATE envios SET estado = ? WHERE id_envio = ?";
        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, state);
            stmt.setInt(2, shipmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating shipment state: " + e.getMessage());
            return false;
        }
    }

    public static List<Envio> getAvailableShipments() {
        List<Envio> envios = new ArrayList<>();
        // Assuming available shipments have no transporter assigned or specific status
        String sql = "SELECT e.*, p.* FROM envios e JOIN paquetes p ON e.id_paquete = p.id_paquete WHERE e.transportista_cedula IS NULL OR e.estado = 'PENDIENTE'";

        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                envios.add(mapResultSetToEnvio(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available shipments: " + e.getMessage());
        }
        return envios;
    }

    public static boolean acceptAvailableShipment(int shipmentId, String driverId) {
        String sql = "UPDATE envios SET transportista_cedula = ?, estado = 'En recogida' WHERE id_envio = ?";
        try (Connection conn = DBConnection.connect(UserType.TRANSPORTISTA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, driverId);
            stmt.setInt(2, shipmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error accepting shipment: " + e.getMessage());
            return false;
        }
    }

    public static boolean createShipment(Envio envio) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(UserType.EMPRENDEDOR);
            conn.setAutoCommit(false);

            // 1. Create Package
            String sqlPackage = "INSERT INTO paquetes (descripcion, peso, tipo, dimension_x, dimension_y, dimension_z, requisitos) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int paqueteId = -1;
            try (PreparedStatement stmt = conn.prepareStatement(sqlPackage, Statement.RETURN_GENERATED_KEYS)) {
                Paquete p = envio.getPaquete();
                stmt.setString(1, p.getDescripcion());
                stmt.setDouble(2, p.getPeso());
                stmt.setString(3, p.getTipo());
                stmt.setDouble(4, p.getDimensionX());
                stmt.setDouble(5, p.getDimensionY());
                stmt.setDouble(6, p.getDimensionZ());
                stmt.setString(7, p.getRequisitos());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        paqueteId = generatedKeys.getInt(1);
                    }
                }
            }

            if (paqueteId == -1) {
                conn.rollback();
                return false;
            }

            // 2. Create Shipment
            String sqlShipment = "INSERT INTO envios (sucursal_RUC, sucursal_id_direccion, id_direccion_entrega, id_paquete, tarifa, estado) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlShipment)) {
                stmt.setString(1, envio.getSucursalRUC());
                stmt.setInt(2, envio.getSucursalIdDireccion());
                stmt.setInt(3, envio.getIdDireccionEntrega());
                stmt.setInt(4, paqueteId);
                stmt.setDouble(5, envio.getTarifa());
                stmt.setString(6, "En recogida"); // Default state
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error creating shipment: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Envio mapResultSetToEnvio(ResultSet rs) throws SQLException {
        Paquete p = new Paquete(
                rs.getInt("id_paquete"),
                rs.getString("descripcion"),
                rs.getDouble("peso"),
                rs.getString("tipo"),
                rs.getDouble("dimension_x"),
                rs.getDouble("dimension_y"),
                rs.getDouble("dimension_z"),
                rs.getString("requisitos"));

        // Note: Dates might need conversion depending on DB type (DATE vs DATETIME)
        LocalDate inicio = rs.getDate("fecha_hora_inicio") != null ? rs.getDate("fecha_hora_inicio").toLocalDate()
                : null;
        LocalDate fin = rs.getDate("fecha_hora_final") != null ? rs.getDate("fecha_hora_final").toLocalDate() : null;

        // Enum handling needs care, assuming string match for now or custom mapping
        // Estado estado = Estado.valueOf(rs.getString("estado").toUpperCase().replace("
        // ", "_"));
        // Simplified for now, passing null or default if enum mismatch is risky

        return new Envio(
                rs.getInt("id_envio"),
                rs.getString("sucursal_RUC"),
                rs.getInt("sucursal_id_direccion"),
                rs.getInt("id_direccion_entrega"),
                rs.getString("transportista_cedula"),
                rs.getInt("id_paquete"),
                inicio,
                fin,
                rs.getDouble("tarifa"),
                null, // Estado mapping to be refined
                p);
    }
}
