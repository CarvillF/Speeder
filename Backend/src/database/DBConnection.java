package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para gestionar conexiones a la base de datos MySQL.
 * Soporta múltiples tipos de usuarios con diferentes permisos.
 */
public class DBConnection {

    /**
     * Establece una conexión a la base de datos usando las credenciales del tipo de
     * usuario especificado.
     * 
     * @param userType Tipo de usuario (EMPRENDEDOR, TRANSPORTISTA, ADMINISTRADOR)
     * @return Conexión a la base de datos, o null si hay un error
     * @throws SQLException si no se puede establecer la conexión
     */
    public static Connection connect(UserType userType) throws SQLException {
        try {
            // Registrar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Obtener credenciales desde DatabaseConfig según el tipo de usuario
            String jdbcUrl = DatabaseConfig.getDbUrl(userType);
            String user = DatabaseConfig.getDbUsername(userType);
            String password = DatabaseConfig.getDbPassword(userType);

            System.out.println("Connecting to database as " + userType + " user...");

            // Abrir conexión
            Connection conn = DriverManager.getConnection(jdbcUrl, user, password);

            if (conn != null) {
                System.out.println("Successfully connected as " + userType);
            }

            return conn;

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error connecting to database as " + userType + ": " + e.getMessage());
            throw new SQLException("Failed to connect as " + userType, e);
        }
    }
}
