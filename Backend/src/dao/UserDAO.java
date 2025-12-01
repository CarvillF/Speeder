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

    /**
     * Registra un nuevo empresario en la base de datos.
     * 
     * @param empresario Objeto Empresario con los datos a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public static boolean registerBusiness(clases.Empresario empresario) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(UserType.ADMINISTRADOR);
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Registrar en tabla usuarios
            if (!registerUser(empresario, conn)) {
                conn.rollback();
                return false;
            }

            // 2. Registrar en tabla empresarios
            String sql = "INSERT INTO empresarios (usuario_cedula, cargo_empresa, correo_empresarial) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, empresario.getCedula());
                stmt.setString(2, empresario.getCargoEmpresa());
                stmt.setString(3, empresario.getCorreoEmpresarial());
                stmt.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
            return true;

        } catch (SQLException e) {
            System.err.println("Error registering business user: " + e.getMessage());
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

    /**
     * Registra un nuevo transportista en la base de datos.
     * 
     * @param transportista Objeto Transportistas con los datos a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public static boolean registerDriver(clases.Transportistas transportista) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(UserType.ADMINISTRADOR);
            conn.setAutoCommit(false);

            // 1. Registrar en tabla usuarios
            if (!registerUser(transportista, conn)) {
                conn.rollback();
                return false;
            }

            // 2. Registrar en tabla transportistas
            String sql = "INSERT INTO transportistas (usuario_cedula, numero_licencia, tipo_licencia, zona_cobertura, fondos, disponibilidad) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, transportista.getCedula());
                stmt.setString(2, transportista.getNumeroLicencia());
                stmt.setString(3, transportista.getTipoLicencia());
                stmt.setString(4, transportista.getZonaCobertura());
                stmt.setFloat(5, transportista.getFondos());
                stmt.setString(6, transportista.getDisponibilidad());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error registering driver: " + e.getMessage());
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

    /**
     * Registra un nuevo administrador en la base de datos.
     * 
     * @param admin Objeto Admin con los datos a registrar
     * @return true si el registro fue exitoso, false en caso contrario
     */
    public static boolean registerAdmin(clases.Admin admin) {
        Connection conn = null;
        try {
            conn = DBConnection.connect(UserType.ADMINISTRADOR);
            conn.setAutoCommit(false);

            // 1. Registrar en tabla usuarios
            if (!registerUser(admin, conn)) {
                conn.rollback();
                return false;
            }

            // 2. Registrar en tabla administradores
            String sql = "INSERT INTO administradores (usuario_cedula, codigo_empleado, activo) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, admin.getCedula());
                stmt.setString(2, admin.getCodigoEmpleado());
                stmt.setBoolean(3, admin.getActivo());
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error registering admin: " + e.getMessage());
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

    /**
     * Método auxiliar para registrar en la tabla usuarios.
     */
    private static boolean registerUser(clases.Usuario user, Connection conn) throws SQLException {
        String sql = "INSERT INTO usuarios (cedula, nombre, apellidos, correo, contrasena, numero_telefono, id_direccion_principal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getCedula());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getApellidos());
            stmt.setString(4, user.getCorreo());
            stmt.setString(5, user.getContrasena());
            stmt.setString(6, user.getNumeroTelefono());
            if (user.getIdDireccionPrincipal() > 0) {
                stmt.setInt(7, user.getIdDireccionPrincipal());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Actualiza los datos del perfil de un usuario.
     * 
     * @param user Objeto Usuario con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public static boolean updateProfile(clases.Usuario user) {
        String sql = "UPDATE usuarios SET nombre = ?, apellidos = ?, correo = ?, numero_telefono = ?, id_direccion_principal = ? WHERE cedula = ?";

        try (Connection conn = DBConnection.connect(UserType.ADMINISTRADOR);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNombre());
            stmt.setString(2, user.getApellidos());
            stmt.setString(3, user.getCorreo());
            stmt.setString(4, user.getNumeroTelefono());
            if (user.getIdDireccionPrincipal() > 0) {
                stmt.setInt(5, user.getIdDireccionPrincipal());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }
            stmt.setString(6, user.getCedula());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }
}
