package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import clases.MetodoPago;
import database.DBConnection;
import database.UserType;

public class PaymentDAO {

    public static List<MetodoPago> getPaymentMethods(String userId) {
        List<MetodoPago> methods = new ArrayList<>();
        String sql = "SELECT * FROM metodos_pago WHERE usuario_cedula = ?";

        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR); // Or generic user if possible
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    methods.add(new MetodoPago(
                            rs.getInt("id_metodo_pago"),
                            rs.getString("usuario_cedula"),
                            rs.getString("tipo"),
                            rs.getString("datos"),
                            rs.getBoolean("predeterminado")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting payment methods: " + e.getMessage());
        }
        return methods;
    }

    public static boolean createPaymentMethod(MetodoPago method) {
        String sql = "INSERT INTO metodos_pago (usuario_cedula, tipo, datos, predeterminado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, method.getUsuarioCedula());
            stmt.setString(2, method.getTipo());
            stmt.setString(3, method.getDatos());
            stmt.setBoolean(4, method.isPredeterminado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating payment method: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePaymentMethod(int methodId) {
        String sql = "DELETE FROM metodos_pago WHERE id_metodo_pago = ?";
        try (Connection conn = DBConnection.connect(UserType.EMPRENDEDOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, methodId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting payment method: " + e.getMessage());
            return false;
        }
    }
}
