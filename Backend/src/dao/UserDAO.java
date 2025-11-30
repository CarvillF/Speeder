package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DBConnection;
import database.UserType;

/**
 * Data Access Object para operaciones relacionadas con usuarios.
 * Maneja autenticación y validación de credenciales.
 */
public class UserDAO {

    /**
     * Valida las credenciales de login de un usuario.
     * 
     * @param correo     Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public static boolean login(String correo, String contrasena) {
        String sql = "SELECT cedula FROM usuarios WHERE correo = ? AND contrasena = ? LIMIT 1";

        // Intentar con conexión de administrador para validar login
        try (Connection conn = DBConnection.connect(UserType.ADMINISTRADOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true si encontró el usuario
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida las credenciales de login y retorna el tipo de usuario.
     * 
     * @param correo     Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return UserType correspondiente al usuario, o null si las credenciales son
     *         inválidas
     */
    public static UserType loginAndGetUserType(String correo, String contrasena) {
        String sql = "SELECT u.cedula, " +
                "e.usuario_cedula as es_empresario, " +
                "t.usuario_cedula as es_transportista, " +
                "a.usuario_cedula as es_administrador " +
                "FROM usuarios u " +
                "LEFT JOIN empresarios e ON u.cedula = e.usuario_cedula " +
                "LEFT JOIN transportistas t ON u.cedula = t.usuario_cedula " +
                "LEFT JOIN administradores a ON u.cedula = a.usuario_cedula " +
                "WHERE u.correo = ? AND u.contrasena = ? LIMIT 1";

        try (Connection conn = DBConnection.connect(UserType.ADMINISTRADOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Determinar el tipo de usuario basado en las tablas relacionadas
                    if (rs.getString("es_administrador") != null) {
                        return UserType.ADMINISTRADOR;
                    } else if (rs.getString("es_empresario") != null) {
                        return UserType.EMPRENDEDOR;
                    } else if (rs.getString("es_transportista") != null) {
                        return UserType.TRANSPORTISTA;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during login and user type detection: " + e.getMessage());
        }

        return null; // Credenciales inválidas o usuario no encontrado
    }

    /**
     * Verifica si existe un usuario con el correo especificado.
     * 
     * @param correo Correo electrónico a buscar
     * @return true si el correo existe, false en caso contrario
     */
    public static boolean correoExiste(String correo) {
        String sql = "SELECT 1 FROM usuarios WHERE correo = ? LIMIT 1";

        try (Connection conn = DBConnection.connect(UserType.ADMINISTRADOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si existe un usuario con la cédula especificada.
     * 
     * @param cedula Cédula a buscar
     * @return true si la cédula existe, false en caso contrario
     */
    public static boolean cedulaExiste(String cedula) {
        String sql = "SELECT 1 FROM usuarios WHERE cedula = ? LIMIT 1";

        try (Connection conn = DBConnection.connect(UserType.ADMINISTRADOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error checking cedula existence: " + e.getMessage());
            return false;
        }
    }
}
